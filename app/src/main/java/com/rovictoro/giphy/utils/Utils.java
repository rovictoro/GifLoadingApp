package com.rovictoro.giphy.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.rovictoro.giphy.gifsearch.GifAdapter;
import com.rovictoro.giphy.models.GifModel;
import java.lang.ref.WeakReference;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public interface Utils {

    //private Utils() {}

    public static void hideSoftKeyboard(final Activity activityIn) {
        final WeakReference<Activity> mReference =  new WeakReference<Activity>(activityIn);
        final Activity activity = mReference.get();
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null && activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//0  InputMethodManager.RESULT_HIDDEN
                    }
                    activity.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN); //SOFT_INPUT_ADJUST_PAN  SOFT_INPUT_ADJUST_RESIZE
                }
            }},100);
    }

    public static void loadGifToCashGlide(final Activity activityIn, final GifModel gifModel, final GifAdapter gifAdapterIn) {
        final WeakReference<Activity> mReference =  new WeakReference<Activity>(activityIn);
        final Activity activity = mReference.get();

        final WeakReference<GifAdapter> mReferenceGifAdapter =  new WeakReference<GifAdapter>(gifAdapterIn);
        final GifAdapter gifAdapter = mReferenceGifAdapter.get();

        final LruCache<String, GifDrawable> memCache = new LruCache<String, GifDrawable>(300) {
            @Override
            protected int sizeOf(String key, GifDrawable image) {
                return image.getSize();//.getByteCount()/1024;
            }
        };

         Glide.with(activity)
                .asGif()
                .centerInside()
                .load(gifModel.getFixedWidthDownsampledUrl())
                .fitCenter()
        //      .thumbnail(1.0f)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))// NONE RESOURCE AUTOMATIC
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        loadGifToCashGlide(activity, gifModel,gifAdapter);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("onResourceReady: ", "gif: " + "title: " + gifModel.getTitle() + " size: " + gifModel.getFixedWidthDownsampledSize() + " original size: " + gifModel.getSize() /*resource.getSize()*/);
                        Single.just(gifModel)
                                .subscribe(new SingleObserver<GifModel>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                        d.dispose();
                                        Log.d("SingleObserver", " onSubscribe : " + d.isDisposed());
                                    }

                                    @Override
                                    public void onSuccess(GifModel value) {

                                        //gifAdapter.addGif(value);
                                        gifAdapter.updateGif(value);
                                        Log.e("SingleObserver", " onNext value : " + value.getTitle());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                        Log.d("SingleObserver", " onError : " + e.getMessage());
                                    }
                                });
                        return true;
                    }
                })

                .into(new SimpleTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
                        memCache.put(gifModel.getFixedWidthDownsampledUrl(), resource);
                        Log.e("loadGifToCashGlide: ", "gif: " + "title: " + gifModel.getTitle() + " size: " + gifModel.getFixedWidthDownsampledSize() ); //resource.getSize()
                    }
                });
    }




}
