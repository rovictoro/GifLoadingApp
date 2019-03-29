package com.rovictoro.giphy.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.rovictoro.giphy.dagger.GifApplication;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

import static android.content.Context.ACTIVITY_SERVICE;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Inject OkHttpClient client;
    //private int percent = 100;
    //private int memCacheSize = 0;

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

       /* ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager)
                context.getSystemService(ACTIVITY_SERVICE)).getMemoryInfo(mi);

        memCacheSize = (int)(percent*mi.availMem/100);

        LruResourceCache lruMemCache = new LruResourceCache(memCacheSize);
        builder.setMemoryCache(lruMemCache);

        //set disk cache 300 mb
        InternalCacheDiskCacheFactory diskCacheFactory =
                new InternalCacheDiskCacheFactory(context, 300);
        builder.setDiskCache(diskCacheFactory);

        //set BitmapPool with 1/10th of memory cache's size
        LruBitmapPool bitmapPool = new LruBitmapPool(memCacheSize/10);
        builder.setBitmapPool(bitmapPool);


        //set custom Glide as global singleton
        Glide.init(context, builder);
        */
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        ((GifApplication) context.getApplicationContext()).getActivityComponent().inject(this);

        /*OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
                */

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);

        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }

}
