package com.example.lap.bakingapp.Dependency_Injection.Modules;

import com.example.lap.bakingapp.NetworkUtils.ApiModel;
import com.example.lap.bakingapp.NetworkUtils.RecipeService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {
    @Provides
    @Singleton
    public RecipeService provideRecipeService (){
        return ApiModel.getRecipeService();
    }


}
