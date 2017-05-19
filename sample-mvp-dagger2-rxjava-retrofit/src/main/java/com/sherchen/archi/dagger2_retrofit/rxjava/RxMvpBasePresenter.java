/*
 * Copyright 2017 Sherchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sherchen.archi.dagger2_retrofit.rxjava;

import android.app.Activity;
import android.content.DialogInterface;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.sherchen.archi.dagger2_retrofit.SystemInfo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Sherchen on 2017/5/19.
 */

public class RxMvpBasePresenter<V extends MvpView> extends MvpBasePresenter<V> {

    protected CompositeDisposable compositeDisposable;
    private Map<Observable, Disposable> disposableMap;

    public RxMvpBasePresenter(Activity activity) {
        super(activity);
    }

    public Activity getActivity(){
        return m_Activity;
    }

    public void callOnCreate(){
        compositeDisposable = RxUtils.getNewCompositeSubIfUnsubscribed(compositeDisposable);
    }

    public void callOnResume(){}

    public void callOnPause(){}

    public void callOnDestroy() {
        unsubscribe();
    }

    protected void unsubscribe(){
        RxUtils.unsubscribeIfNotNull(compositeDisposable);
    }

    /**
     * @param activity 上下文
     * @param observable 观察者
     * @param listener 监听者
     * @param showDialog 是否显示对话框
     * @param <T> 解析实体类
     * @return
     */
    public <M> Disposable subscribe(
            final Activity activity,
            final Observable<M> observable,
            final RxCallListener<M> listener,
            final boolean showDialog,
            final int... seconds
            ) {
        if(listener != null && ! listener.callOnStart()) {
            return null;
        }
        int length = seconds.length;
        Observable<M> toObservable;
        if(length >= 1) {
            toObservable = RxUtils.applyScheduler(observable, seconds[0]);
        } else{
            toObservable = RxUtils.applyScheduler(observable);
        }
        final DisposableObserver<M> disposable = new DisposableObserver<M>() {
            @Override
            public void onNext(M m) {
                if (listener != null) {
                    listener.callOnNext(m, false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(showDialog){
                    SystemInfo.getInstance().dismissDialog(activity);
                }
                rmSubscription(this);
                if(listener != null){
                    listener.callOnError(e, true);
                }
            }

            @Override
            public void onComplete() {
                if(showDialog){
                    SystemInfo.getInstance().dismissDialog(activity);
                }
                rmSubscription(this);
                if(listener != null){
                    listener.callOnComplete();
                }
            }
        };
        if(showDialog){
            SystemInfo.getInstance().showDialog(m_Activity, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(listener != null) {
                        listener.callOnCancel();
                    }
                    rmSubscription(disposable);
                }
            });
        }
        toObservable.subscribe(disposable);
        addSubscription(disposable);
        return disposable;
    }

    public void addSubscription(Observable observable, Disposable disposable){
        if(compositeDisposable != null){
            compositeDisposable.add(disposable);
        }

        if(disposableMap == null) {
            disposableMap = new HashMap<>();
        }
        disposableMap.put(observable, disposable);
    }

    public void addSubscription(DisposableObserver disposable){
        if(compositeDisposable != null){
            compositeDisposable.add(disposable);
        }
    }

    public void rmSubscription(Observable observable) {
        if(disposableMap == null) return;
        Disposable disposable = disposableMap.get(observable);
        if(compositeDisposable != null) {
            compositeDisposable.remove(disposable);
        }
    }

    public void rmSubscription(DisposableObserver observable) {
        if(disposableMap == null) return;
        if(compositeDisposable != null) {
            compositeDisposable.remove(observable);
        }
    }
}

