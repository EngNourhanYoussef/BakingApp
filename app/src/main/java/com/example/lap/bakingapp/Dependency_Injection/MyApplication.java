package com.example.lap.bakingapp.Dependency_Injection;

import android.app.Application;

import com.example.lap.bakingapp.Dependency_Injection.Modules.ApiModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.AppModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.DbModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.RepositoryModule;



public class MyApplication extends Application {

    private static final MyApplication instance = new MyApplication();
    private static AppComponent appComponent;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getAppComponent();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .repositoryModule(new RepositoryModule())
                    .dbModule (new DbModule(this))
                    .apiModule(new ApiModule())
                    .build();
        }
        return appComponent;
    }
}
