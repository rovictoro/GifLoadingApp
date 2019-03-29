package com.rovictoro.giphy.dagger;

import android.app.Application;

import com.rovictoro.giphy.dagger.ActivityComponent;
import com.rovictoro.giphy.dagger.AppModule;

import com.rovictoro.giphy.dagger.DaggerActivityComponent;
import com.rovictoro.giphy.dagger.GPHApiModule;
import com.rovictoro.giphy.dagger.NetModule;

public class GifApplication extends Application {

    private static final String GPH_API_KEY = "DzbDBcffvbP0W2C2XAshCmIxsKVnj7mu";
    private ActivityComponent _activityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        _activityComponent = createComponent();
    }

    protected ActivityComponent createComponent() {

        return DaggerActivityComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .gPHApiModule(new GPHApiModule(GPH_API_KEY))
                .build();

    }

    public ActivityComponent getActivityComponent() {
        return _activityComponent;
    }

}
