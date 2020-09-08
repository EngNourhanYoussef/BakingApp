package com.example.lap.bakingapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.Repository.RecipeRepository;

import javax.inject.Inject;

public class RecipeDetailViewModel extends ViewModel {
    private LiveData<Recipe> recipe;
    private long recipeId;

    @SuppressWarnings("WeakerAccess")
    @Inject
    RecipeRepository repository;

    public RecipeDetailViewModel() {
        MyApplication.getInstance().getAppComponent().inject(this);
        recipeId = -1;
    }

    public void init(long recipeId) {
        if (this.recipeId != -1) {
            return;
        }
        this.recipeId = recipeId;
    }

    public LiveData<Recipe> getRecipe() {
        if (recipeId == -1) {
            return null;
        }
        if (recipe == null) {
            recipe = repository.getRecipe(recipeId);
        }
        return recipe;
    }

    public void setToWidget(){
        repository.setWidget(recipeId);
    }
}
