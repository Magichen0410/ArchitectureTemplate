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


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    private static final long DEFAULT_TIMEOUT_SECONDS = 30;

    public static void unsubscribeIfNotNull(CompositeDisposable compositeDisposable) {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public static CompositeDisposable getNewCompositeSubIfUnsubscribed(CompositeDisposable compositeDisposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            return new CompositeDisposable();
        }
        return compositeDisposable;
    }

    /**
     * Called in {@link #(Observable, boolean)} to set  <code>subscribeOn()</code> and
     * <code>observeOn()</code>. As default it uses {@link AndroidSchedulerTransformer}. Override
     * this
     * method if you want to provide your own scheduling implementation.
     */
//    public static <A> Observable<GenericObj<A>> applyScheduler(Observable<HttpResultWrap<A>> observable) {
////        observable.compose(new AndroidSchedulerTransformer<HttpResultWrap<A>>()
//        return map(composeAndroid(observable)).timeout(RestClient.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
//    }

    public static <M> Observable<M> applyScheduler(Observable<M> observable) {
//        observable.compose(new AndroidSchedulerTransformer<HttpResultWrap<A>>()
        return composeAndroid(observable).timeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

//    /**
//     * Called in {@link #(Observable, boolean)} to set  <code>subscribeOn()</code> and
//     * <code>observeOn()</code>. As default it uses {@link AndroidSchedulerTransformer}. Override
//     * this
//     * method if you want to provide your own scheduling implementation.
//     */
//    public static <A> Observable<GenericObj<A>> applyScheduler(Observable<HttpResultWrap<A>> observable, int timeoutInSeconds) {
////        observable.compose(new AndroidSchedulerTransformer<HttpResultWrap<A>>()
//        return map(composeAndroid(observable)).timeout(timeoutInSeconds, TimeUnit.SECONDS);
//    }

    public static <M> Observable<M> applyScheduler(Observable<M> observable, int timeoutInSeconds) {
//        observable.compose(new AndroidSchedulerTransformer<HttpResultWrap<A>>()
        return composeAndroid(observable).timeout(timeoutInSeconds, TimeUnit.SECONDS);
    }

    public static <T> Observable<T> composeAndroid(Observable<T> observable){
        return observable.compose(new AndroidSchedulerTransformer<T>());
    }

//    public static <A, M extends HttpResultWrap<A>> Observable<GenericObj<A>> map(Observable<M> observable){
//        return observable.map(new Func1<HttpResultWrap<A>, GenericObj<A>>() {
//            @Override
//            public GenericObj<A> call(HttpResultWrap<A> m) {
//                KLog.d("onNext >> " + m);
//                if (m == null) {
//                    throw new RuntimeException("Entity is null");
//                }
//                HttpStatus status = m.getStatus();
//                if (status == null) {
//                    throw new RuntimeException("status is null");
//                }
//                KLog.d(status.getRespdesc());
//                if ("0000".equals(status.getRespcode())) {
//                    A data = m.getData();
//                    if(data == null){
//                        throw new RuntimeException("Data is null");
//                    }
//                    return new GenericObj(data, status.getRespdesc());
//                }else{
//                    throw new ToastException(status.getRespdesc());
//                }
//            }
//        });
//    }

//    public static Observable<List<MultipartBody.Part>> toMultipart(Observable<List<Compressor.ImageBytesInfo>> observable, final String type){
//        return observable.map(new Func1<List<Compressor.ImageBytesInfo>, List<MultipartBody.Part>>() {
//            @Override
//            public List<MultipartBody.Part> call(List<Compressor.ImageBytesInfo> imageBytesInfos) {
//                int size = ObjectUtils.getSize(imageBytesInfos);
//                List<MultipartBody.Part> parts = new ArrayList<>(size);
//                for(int i=0;i<size;i++){
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), imageBytesInfos.get(i).getBytes());
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("pics", imageBytesInfos.get(i).getFilename(), requestBody);
//                    parts.add(part);
//                    parts.add(MultipartBody.Part.createFormData("whs",
//                            imageBytesInfos.get(i).getWidth() + URLS.DIVIDER_WIDTH_HEIGHT + imageBytesInfos.get(i).getHeight()));
//                }
//                parts.add(MultipartBody.Part.createFormData("type", type));
//                return parts;
//            }
//        });
//    }

    /**
     * The default {@link SchedulerTransformer} that subscrubes on {@link Schedulers#newThread()} and
     * observes on Android's main Thread.
     *
     * @author Hannes Dorfmann
     * @since 1.0.0
     */
     private static class AndroidSchedulerTransformer<T> implements ObservableTransformer<T, T> {

      @Override public Observable<T> apply(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    }
}
