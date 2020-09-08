package com.example.lap.bakingapp.Dependency_Injection.Modules;

import com.example.lap.bakingapp.Repository.RecipeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public RecipeRepository provideRecipeRepository() {return new RecipeRepository();}
}
