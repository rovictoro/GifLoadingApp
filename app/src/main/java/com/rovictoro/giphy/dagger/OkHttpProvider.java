package com.rovictoro.giphy.dagger;

//import interview.legacy.pinterest.api.AuthInterceptor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class OkHttpProvider {
    private static OkHttpClient instance = null;

    public static OkHttpClient getOkHttpInstance() {
        if (instance == null) {
            //synchronized(instance) {
                instance = new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        //.addInterceptor(new AuthInterceptor())
                        .build();
            //}
        }
        return instance;
    }
}
