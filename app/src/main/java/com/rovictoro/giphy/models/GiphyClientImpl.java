package com.rovictoro.giphy.models;

import android.util.Log;

import com.giphy.sdk.analytics.models.enums.EventType;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.LangType;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.models.enums.RatingType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.rovictoro.giphy.gifsearch.GifContract;

import java.util.ArrayList;
import java.util.List;

public class GiphyClientImpl implements GiphyClient {

    private GPHApiClient mGphApiClient;
    private List<GifModel> searchResultGifModel;

    public GiphyClientImpl(GPHApiClient gphApiClient){
        this.mGphApiClient = gphApiClient;
        this.searchResultGifModel = new ArrayList<GifModel>();
    }

    @Override
    public void searchQueueGPHApi(GifContract.OnResponseCallback callback, String searchWord){

        if(mGphApiClient != null && callback != null && searchWord != null) {
            mGphApiClient.search(searchWord, MediaType.gif, null, null, RatingType.pg13, LangType.english, new CompletionHandler<ListMediaResponse>() {
                @Override
                public void onComplete(ListMediaResponse result, Throwable e) {
                    if (result == null) {
                        // Do what you want to do with the error
                        callback.onResponse(searchResultGifModel);
                    } else {
                        if (result.getData() != null) {
                            int position = 0;
                            for (Media gif : result.getData()) {
                                GifModel mNewGifModel = new GifModel(gif, position);
                                searchResultGifModel.add(mNewGifModel);
                                position++;
                                //Log.e("searchGPHApi", " " + gif.getImages().getOriginal().getGifUrl()
                                //        + " size: " + gif.getImages().getOriginal().getGifSize()
                                //        + " downsized: " + gif.getImages().getFixedWidthDownsampled().getGifUrl() + " FixedWidthDownsampledSize: " + gif.getImages().getFixedWidthDownsampled().getGifSize());
                            }

                            Log.e("searchGPHApi", "size: " + searchResultGifModel.size());

                            callback.onResponse(searchResultGifModel);
                            searchResultGifModel.clear();
                        }
                    }
                }
            });
        }
    }


}
