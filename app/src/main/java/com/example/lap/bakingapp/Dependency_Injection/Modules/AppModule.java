package com.example.lap.bakingapp.Dependency_Injection.Modules;

import android.content.Context;

import com.example.lap.bakingapp.AppExecutors;
import com.example.lap.bakingapp.Dependency_Injection.MyApplication;

import java.util.concurrent.Executor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final MyApplication app;
    private final AppExecutors executors;

    public AppModule(MyApplication app) {
        this.app = app;
        executors = new AppExecutors();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    public AppExecutors provideAppExecutors() {
        return executors;
    }

    @Provides
    @Named("disc")
    @Singleton
    public Executor provideDiscIOExecutor() {
        return executors.diskIO();
    }

    @Provides
    @Named("network")
    @Singleton
    public Executor provideNetworkIOExecutor() {
        return executors.networkIO();
    }

}
