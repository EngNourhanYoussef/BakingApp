package com.example.lap.bakingapp.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.lap.bakingapp.DataBase.DAO.IngredientDao;
import com.example.lap.bakingapp.DataBase.DAO.RecipeDao;
import com.example.lap.bakingapp.DataBase.DAO.StepDao;
import com.example.lap.bakingapp.DataBase.ENTITY.IngredientEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.RecipeEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;


import javax.inject.Singleton;

@Singleton
@Database(entities = {IngredientEntity.class , RecipeEntity.class , StepEntity.class},
        version = 2, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase {

    private static RecipeDataBase INASTANCE ;
    private static final String DATABASE_NAME = "Recipe_Database.db";

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract StepDao stepDao();

    public static RecipeDataBase getDatabase( final Context context){
        if(INASTANCE == null){
            INASTANCE = Room.databaseBuilder(context.getApplicationContext(),RecipeDataBase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INASTANCE;

    }

}
