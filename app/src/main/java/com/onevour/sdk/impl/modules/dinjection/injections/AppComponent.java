package com.onevour.sdk.impl.modules.dinjection.injections;

import com.onevour.sdk.impl.modules.dinjection.controllers.DInjectionActivity;

import javax.inject.Singleton;

import dagger.Component;

// 2
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(DInjectionActivity activity);
}
