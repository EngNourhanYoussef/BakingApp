package com.example.lap.bakingapp.Dependency_Injection.Modules;

import com.example.lap.bakingapp.DataBase.DAO.IngredientDao;
import com.example.lap.bakingapp.DataBase.DAO.RecipeDao;
import com.example.lap.bakingapp.DataBase.DAO.StepDao;
import com.example.lap.bakingapp.DataBase.RecipeDataBase;
import com.example.lap.bakingapp.Dependency_Injection.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class DbModule {
    private final MyApplication app;

    public DbModule(MyApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public RecipeDataBase provideRecipeDatabase() {
        return RecipeDataBase.getDatabase(app);
    }

    @Provides
    @Singleton
    public StepDao provideStepDao() {
        return provideRecipeDatabase().stepDao();
    }

    @Provides
    @Singleton
    public IngredientDao provideIngredientDao() {
        return provideRecipeDatabase().ingredientDao();
    }

    @Provides
    @Singleton
    public RecipeDao provideRecipeDao() {
        return provideRecipeDatabase().recipeDao();
    }
}


