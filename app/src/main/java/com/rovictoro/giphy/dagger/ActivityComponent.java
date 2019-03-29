package com.rovictoro.giphy.dagger;

import com.rovictoro.giphy.gifsearch.MainActivity;
import com.rovictoro.giphy.gifsearch.GifAdapter;
import com.rovictoro.giphy.glide.MyAppGlideModule;

import javax.inject.Singleton;
import dagger.Component;


@Singleton
@Component(modules={AppModule.class, NetModule.class, GPHApiModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(GifApplication application);
    void inject(MyAppGlideModule myGlideModule);
    void inject(GifAdapter myGifAdapter);

}
