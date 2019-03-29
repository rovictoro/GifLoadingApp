package com.rovictoro.giphy.gifsearch;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.rovictoro.giphy.R;
import com.rovictoro.giphy.glide.GlideApp;
import com.rovictoro.giphy.models.GifModel;


public class ViewDialog {
    Activity activity;
    Dialog dialog;
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(GifModel gifModel) {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_loading_layout);


        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        //DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget (gifImageView);
        GlideApp.with(activity).asGif()
                //.centerInside()
                .load(gifModel.getFixedWidthDownsampledUrl())
                .fitCenter()
                //  .thumbnail(1.0f)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        Log.e("onLoadFailed: ", "gif: " + "title: " + gifModel.getTitle() + " size: " + gifModel.getFixedWidthDownsampledSize() );
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))// NONE RESOURCE AUTOMATIC  DATA
                .onlyRetrieveFromCache(true)
                .into(gifImageView);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
