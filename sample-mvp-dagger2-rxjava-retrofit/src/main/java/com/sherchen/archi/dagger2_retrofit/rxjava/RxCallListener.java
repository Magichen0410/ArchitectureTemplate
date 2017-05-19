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

/**
 * Created by Sherchen on 2017/5/19.
 */
public interface RxCallListener<T> {
    /**
     * @param desc      描述信息[正确/错误]
     * @param t         实体类 不等于null时，@param desc 是正确信息，否则为错误信息
     * @param needToast 是否需要使用toast提示
     */
    void callOnNext(T t, boolean needToast);

    /**
     * 当onError时调用
     * 有可能是在主线程，也有可能是普通线程
     * @param throwable
     * @param needToast
     */
    void callOnError(Throwable throwable, boolean needToast);

    /**
     * 当onComplete时调用
     */
    void callOnComplete();

    /**
     * 当任务开始时调用
     * @return true 继续调用API，false 停止调用API
     */
    boolean callOnStart();

//    /**
//     * 当无网络时调用
//     */
//    void callOnNoNetwork();
//
//    /**
//     * 当返回的data等于null时调用
//     * @param desc
//     */
//    void callOnNoData(String desc);

    /**
     * 当取消对话框时返回
     */
    void callOnCancel();
}
