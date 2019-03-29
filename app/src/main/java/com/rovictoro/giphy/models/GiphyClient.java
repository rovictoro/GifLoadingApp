package com.rovictoro.giphy.models;

import com.rovictoro.giphy.gifsearch.GifContract;

public interface GiphyClient {
    void searchQueueGPHApi(GifContract.OnResponseCallback callback, String searchWord);
}
