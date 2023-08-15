package com.onevour.sdk.impl.modules.dinjection.injections;

import android.app.Application;

import com.onevour.core.utilities.commons.RefSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

// 1
@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    public RefSession session() {
        return new RefSession();
    }
}
