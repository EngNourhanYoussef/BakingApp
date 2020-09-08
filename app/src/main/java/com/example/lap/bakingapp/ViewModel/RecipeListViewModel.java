package com.example.lap.bakingapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.Repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeListViewModel extends ViewModel {
    private LiveData<List<Recipe>> recipe;

    @Inject
    RecipeRepository recipeRepository;

    public RecipeListViewModel() {
        MyApplication.getInstance().getAppComponent().inject(this);
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (recipe == null) {
            recipe = recipeRepository.getRecipes();
        }
        return recipe;
    }
}
