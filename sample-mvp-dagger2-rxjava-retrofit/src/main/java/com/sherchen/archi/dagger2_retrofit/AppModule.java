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
package com.sherchen.archi.dagger2_retrofit;

import com.sherchen.archi.dagger2_retrofit.network.GithubService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sherchen on 2017/5/19.
 */

@Module
public class AppModule {

    private final MyApplication mApp;

    public AppModule(MyApplication app) {
        this.mApp = app;
    }

    @Provides @Singleton
    MyApplication provideAppInstance(){
        return mApp;
    }

    public static final int DEFAULT_TIMEOUT_SECONDS = 20;
    private static final int TIMEOUT_CONNECT_SECONDS = DEFAULT_TIMEOUT_SECONDS;
    private static final int TIMEOUT_READ_SECONDS = DEFAULT_TIMEOUT_SECONDS;

    @Provides @Singleton
    OkHttpClient provideOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ_SECONDS, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    @Provides @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides @Singleton
    GithubService provideGithub(Retrofit retrofit){
        return retrofit.create(GithubService.class);
    }
}
