package com.rovictoro.giphy.dagger;

//import com.giphy.sdk.core.network.api.GPHApi;
//import com.giphy.sdk.core.network.api.GPHApiClient;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.giphy.sdk.analytics.GiphyPingbacks;
import com.giphy.sdk.core.GiphyCore;
import com.giphy.sdk.core.GiphyCoreUtils;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.rovictoro.giphy.models.GifModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GPHApiModule {

    String mGphApiKey;
    GPHApiClient gphApiClient;
    Context mContext;


    // Constructor needs one parameter to instantiate.
    public GPHApiModule(Context context, String mGphApiKey) {
        this.mGphApiKey = mGphApiKey;
        this.mContext = context;

    }

    @Provides
    @Singleton
    GPHApiClient providesGPHApiClient(){
        GiphyPingbacks.INSTANCE.configure(mContext,mGphApiKey);
        GiphyPingbacks.INSTANCE.setSharedPref(new SharedPreferences() {
            @Override
            public Map<String, ?> getAll() {
                return null;
            }

            @Nullable
            @Override
            public String getString(String s, @Nullable String s1) {
                return null;
            }

            @Nullable
            @Override
            public Set<String> getStringSet(String s, @Nullable Set<String> set) {
                return null;
            }

            @Override
            public int getInt(String s, int i) {
                return 0;
            }

            @Override
            public long getLong(String s, long l) {
                return 0;
            }

            @Override
            public float getFloat(String s, float v) {
                return 0;
            }

            @Override
            public boolean getBoolean(String s, boolean b) {
                return false;
            }

            @Override
            public boolean contains(String s) {
                return false;
            }

            @Override
            public Editor edit() {
                return null;
            }

            @Override
            public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

            }

            @Override
            public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

            }
        });
        gphApiClient = new GPHApiClient(mGphApiKey);
        return gphApiClient;}





    //Application providesApplication() {
    //    return mApplication;
    //}

}
