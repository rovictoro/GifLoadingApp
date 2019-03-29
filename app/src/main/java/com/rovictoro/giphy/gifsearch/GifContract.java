package com.rovictoro.giphy.gifsearch;

import com.rovictoro.giphy.models.GifModel;

import java.util.List;

public interface GifContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showGifList(List<GifModel> gifs);
        void showLoadingError(String errMsg);

    }

    interface Presenter{
        void loadGifList(String searchWord);
        void dropView();
    }

    interface OnResponseCallback{
        void onResponse(List<GifModel> gifModels);
        void onError(String errMsg);
    }


}
