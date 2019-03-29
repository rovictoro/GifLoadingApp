package com.rovictoro.giphy.gifsearch;

import com.rovictoro.giphy.models.GifModel;
import com.rovictoro.giphy.models.GiphyClient;
import com.rovictoro.giphy.utils.EspressoTestingIdlingResource;

import java.util.List;

public class GifPresenter implements GifContract.Presenter {

    private GifContract.View view;

    private GiphyClient mGiphyClient;

    public GifPresenter(GifContract.View view,GiphyClient client) {
        this.view = view;
        mGiphyClient = client;
    }

    @Override
    public void loadGifList(String searchWord){
        view.showProgress();
        //searchWord = "cost";
        mGiphyClient.searchQueueGPHApi(callback, searchWord);
        EspressoTestingIdlingResource.increment();
    }

    @Override
    public void dropView(){
        view = null;
    }

    // callback mechanism , onResponse will be triggered with response
    // by simulatemovieclient(or your network or database process) and pass the response to view
    private final GifContract.OnResponseCallback callback = new GifContract.OnResponseCallback() {
        @Override
        public void onResponse(List<GifModel> gifModels) {
            view.showGifList(gifModels);
            view.hideProgress();
            EspressoTestingIdlingResource.decrement();
        }

        @Override
        public void onError(String errMsg) {
            view.hideProgress();
            view.showLoadingError(errMsg);
            EspressoTestingIdlingResource.decrement();
        }
    };



}
