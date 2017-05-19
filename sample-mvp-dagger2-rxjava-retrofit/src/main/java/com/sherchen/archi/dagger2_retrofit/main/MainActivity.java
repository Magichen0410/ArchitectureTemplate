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
package com.sherchen.archi.dagger2_retrofit.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.sherchen.archi.dagger2_retrofit.MyApplication;
import com.sherchen.archi.dagger2_retrofit.R;
import com.sherchen.archi.dagger2_retrofit.network.GithubService;
import com.sherchen.archi.dagger2_retrofit.rxjava.RxBgCallListener;
import com.sherchen.archi.dagger2_retrofit.rxjava.RxCallListener;
import com.sherchen.archi.dagger2_retrofit.rxjava.RxMvpActivity;
import com.sherchen.archi.dagger2_retrofit.rxjava.RxMvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sherchen on 2017/5/19.
 */

public class MainActivity extends RxMvpActivity<MainView, MainPresent> implements MainView {

    @Inject
    GithubService github;

    TextView mTvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvDisplay = (TextView) findViewById(R.id.textView);
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        presenter.uiShowList(github);
    }

    @NonNull
    @Override
    public MainPresent createPresenter() {
        return new MainPresent(this);
    }

    @Override
    public void showUsers(List<GithubUser> users, Throwable throwable) {
        if(users == null) {
            mTvDisplay.setText("result:" + throwable);
        }else{
            int size = users.size();
            int displaySize = size >= 5 ? 5 : size;
            StringBuilder sb = new StringBuilder();
            sb.append("result:\n");
            for(int i = 0;i<displaySize;i++){
                sb.append(i + ":" + users.get(i).getLogin());
                sb.append("\n");
            }
            mTvDisplay.setText(sb.toString());
        }
    }
}

/**
 * Created by Sherchen on 2017/5/19.
 */

class MainPresent extends RxMvpBasePresenter<MainView> {

    public MainPresent(Activity activity) {
        super(activity);
    }

    public void uiShowList(GithubService githubService){
        subscribe(
                m_Activity,
                githubService.getUserList(),
                new RxBgCallListener<List<GithubUser>>() {
                    @Override
                    public void callOnNext(List<GithubUser> githubUsers, boolean needToast) {
                        if(isViewAttached()) {
                            getView().showUsers(githubUsers, null);
                        }
                    }

                    @Override
                    public void callOnError(Throwable throwable, boolean needToast) {
                        if(isViewAttached()) {
                            getView().showUsers(null, throwable);
                        }
                    }

                    @Override
                    public boolean callOnStart() {
                        if(!isConnected()) {
                            Log.d("sherchen", "no network is connected");
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void callOnCancel() {
                        Log.d("sherchen", "user cancel the dialog");
                    }
                },
                true
        );
    }

    private boolean isConnected() {
        NetworkInfo info = ((ConnectivityManager) m_Activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}

/**
 * Created by Sherchen on 2017/5/19.
 */

interface MainView extends MvpView {
    void showUsers(List<GithubUser> users, Throwable error);
}
