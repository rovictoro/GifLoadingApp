package com.rovictoro.giphy.dagger;

//import interview.legacy.pinterest.api.AuthInterceptor;
import android.app.Application;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;


public class GlideProvider {
    private static Glide instance = null;

    public static Glide getGlideInstance(Application application) {
        if (instance == null) {
            //synchronized(instance) {
                instance = Glide.get(application);
            //}
        }
        return instance;
    }
}
