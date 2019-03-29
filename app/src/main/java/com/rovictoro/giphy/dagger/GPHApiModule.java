package com.rovictoro.giphy.dagger;

import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.rovictoro.giphy.models.GifModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GPHApiModule {

    String mGphApiKey;
    GPHApiClient gphApiClient;


    // Constructor needs one parameter to instantiate.
    public GPHApiModule(String mGphApiKey) {
        this.mGphApiKey = mGphApiKey;

    }

    @Provides
    @Singleton
    GPHApiClient providesGPHApiClient(){
        gphApiClient = new GPHApiClient(mGphApiKey);
        return gphApiClient;}





    //Application providesApplication() {
    //    return mApplication;
    //}

}
