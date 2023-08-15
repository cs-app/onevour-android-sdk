package com.onevour.sdk.impl;

import android.app.Application;

import com.onevour.sdk.impl.modules.dinjection.injections.AppComponent;
import com.onevour.sdk.impl.modules.dinjection.injections.AppModule;
import com.onevour.sdk.impl.modules.dinjection.injections.DaggerAppComponent;

public class MainApplication extends Application {

    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
