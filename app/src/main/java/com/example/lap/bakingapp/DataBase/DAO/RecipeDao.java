package com.example.lap.bakingapp.DataBase.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.lap.bakingapp.DataBase.ENTITY.RecipeEntity;
import com.example.lap.bakingapp.Repository.Recipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {
    @Insert(onConflict = REPLACE)
    long save(RecipeEntity recipeEntity);

     @Query("SELECT* FROM recipe_table WHERE recipe_id= :recipeid" )
     @Transaction
     Recipe loadSync(long recipeid);
    @Query("SELECT * FROM recipe_table WHERE recipe_id = :recipeId")
    @Transaction
    LiveData<Recipe> loadRecipe(long recipeId);

    @Query("SELECT * FROM recipe_table WHERE name = :name")
    RecipeEntity loadByNameSync(String name);

    @Query("SELECT * FROM recipe_table")
    @Transaction
    LiveData<List<Recipe>> loadAllRecipe();
}



