package com.rovictoro.giphy.gifsearch;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rovictoro.giphy.R;
import com.rovictoro.giphy.dagger.GifApplication;
import com.rovictoro.giphy.models.GifModel;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class GifAdapter extends RecyclerView.Adapter<GifAdapter.ViewHolder> {

    @Inject
    Glide glide;
    private Activity mActivity;
    private List<GifModel> mGifModels;
    private ViewDialog viewDialog;


    public GifAdapter(Activity activity) {
        mActivity = activity;
        mGifModels = new ArrayList<GifModel>();
        ((GifApplication) mActivity.getApplication()).getActivityComponent().inject(this);
    }

    public void deleteGifs(){
        mGifModels.clear();
        notifyDataSetChanged();
    }

    public void addGifs(List<GifModel> gifModels){
        //mGifModels.clear();
        int rangeStart = mGifModels.size();
        int rangeEnd = rangeStart + gifModels.size();
        mGifModels.addAll(gifModels);

        //notifyItemInserted(gifModels.size());
        notifyItemRangeInserted(rangeStart, rangeEnd);
        //notifyDataSetChanged();
    }
    public void addGif(GifModel gifModel){
        //mGifModels.clear();
        //int rangeStart = mGifModels.size();
        //int rangeEnd = rangeStart + 1;
        if(!mGifModels.contains(gifModel)) {
            mGifModels.add(gifModel);
            notifyItemInserted(mGifModels.size());
        } else {
            notifyItemChanged(mGifModels.indexOf(gifModel));
            //notifyDataSetChanged();
        }
        //notifyItemRangeInserted(rangeStart, rangeEnd);
        //notifyDataSetChanged();
    }
    public void updateGif(GifModel gifModel){
        if(mGifModels.contains(gifModel)) {
            notifyItemChanged(mGifModels.indexOf(gifModel));
        }
            //notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewDialog = new ViewDialog(mActivity);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View contactView = inflater.inflate(R.layout.gif_item, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final GifModel gifModel = mGifModels.get(position);
        loadGifGlide(mActivity, gifModel, holder.gifImage);
        holder.gifTitle.setText(gifModel.getTitle());
        holder.gifImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.showDialog(gifModel);
            }
        });
        holder.gifImage.requestLayout();
    }

    @Override
    public int getItemCount() {
        return mGifModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView gifImage;
        private final TextView gifTitle;

        private ViewHolder(View itemView) {
            super(itemView);
            gifImage = (ImageView) itemView.findViewById(R.id.gif_image);
            gifTitle = (TextView) itemView.findViewById(R.id.gif_title);
        }
    }

    private void loadGifGlide(final Activity activity, final GifModel gifModel, final ImageView view){

        Glide.with(activity)
                .asGif()
                //.centerInside()
                .load(gifModel.getFixedWidthDownsampledUrl())
                .fitCenter()
                //  .thumbnail(1.0f)

                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        //Log.e("onLoadFailed: ", "gif: " + "title: " + gifModel.getTitle() + " size: " + gifModel.getFixedWidthDownsampledSize() );
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))// NONE RESOURCE AUTOMATIC
                .onlyRetrieveFromCache(true)
                .into(view);
    }


}
