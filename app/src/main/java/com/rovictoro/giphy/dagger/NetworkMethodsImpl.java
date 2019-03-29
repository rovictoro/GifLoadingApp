package com.rovictoro.giphy.dagger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



import java.lang.ref.WeakReference;

public class NetworkMethodsImpl /*implements NetworkMethods*/ {

    private boolean isNetworkReachable;
    private ConnectivityManager manager;

    public NetworkMethodsImpl(Application application/*In*/){
        //final WeakReference<Application> mReference =  new WeakReference<Application>(applicationIn);
        //final Application application = mReference.get();
        manager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
     }

    //@Override
    public boolean isNetworkAvailable(){
        isNetworkReachable = false;
        if(manager == null) return isNetworkReachable;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkReachable = true;
        }
        return isNetworkReachable;
    }

}
