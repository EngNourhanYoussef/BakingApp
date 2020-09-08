package com.example.lap.bakingapp.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;


import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.example.lap.bakingapp.DataBase.DAO.IngredientDao;
import com.example.lap.bakingapp.DataBase.DAO.RecipeDao;
import com.example.lap.bakingapp.DataBase.DAO.StepDao;
import com.example.lap.bakingapp.DataBase.ENTITY.IngredientEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.RecipeEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.NetworkUtils.RecipeService;
import com.example.lap.bakingapp.R;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@SuppressWarnings("WeakerAccess")
@Singleton
public class RecipeRepository {
    @Inject
    RecipeDao recipeDao;
    @Inject
    IngredientDao ingredientDao;
    @Inject
    StepDao stepDao;
    @Inject
    @Named("disc")
    Executor diskIoExecutor;
    @Inject
    RecipeService recipeSevice;
    @Inject
    Context context;

   @Inject
    public RecipeRepository() {
            MyApplication.getInstance().getAppComponent().inject(this);
        }

        public LiveData<List<Recipe>> getRecipes () {
            storeRecipesFromApi();
            return recipeDao.loadAllRecipe();
        }

        public LiveData<Recipe> getRecipe ( long id){
            return recipeDao.loadRecipe(id);
        }

        public LiveData<StepEntity> getStep ( long id){
            return stepDao.loadById(id);
        }

    public String getWidgetRecipeTitle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(context.getString(R.string.pref_key_widget_recipe_title),
                context.getString(R.string.pref_value_widget_not_set_title));
    }

    public String getWidgetRecipeIngredients() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_key_widget_recipe_ingredients),
                context.getString(R.string.pref_value_widget_not_set_description));
    }

    public long getWidgetRecipeId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(context.getString(R.string.pref_key_widget_recipe_id), -1);
    }

    @WorkerThread
    public void setWidget(final long recipeId) {
        diskIoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Recipe recipe = recipeDao.loadSync(recipeId);
                setWidgetRecipe(recipe);
            }
        });
    }

    private void setWidgetRecipe(Recipe recipe) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_key_widget_recipe_title),
                recipe.getName());
        editor.putString(context.getString(R.string.pref_key_widget_recipe_ingredients),
                recipe.getIngredientsAsString());
        editor.putLong(context.getString(R.string.pref_key_widget_recipe_id), recipe.getId());
        editor.apply();
    }

    private void storeRecipesFromApi() {
        recipeSevice.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                if (response.isSuccessful()) {
                    saveRecipesToDb(response.body());
                } else {
                    @SuppressWarnings("unused") int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
            }
        });
    }

    private void saveRecipesToDb(final List<Recipe> recipes) {
        diskIoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (Recipe recipe : recipes) {
                    RecipeEntity recipeInDb = recipeDao.loadByNameSync(recipe.getName());
                    if (recipeInDb == null) {
                        // only save to db, if not already exist
                        long recipeId = recipeDao.save(recipe);
                        for (StepEntity step : recipe.getSteps()) {
                            step.setRecipeId(recipeId);
                        }
                        for (IngredientEntity ingredient : recipe.getIngredients()) {
                            ingredient.setRecipeId(recipeId);
                        }
                        stepDao.insertAll(recipe.getSteps());
                        ingredientDao.insertAll(recipe.getIngredients());
                    }
                }
            }
        });
    }
    }

