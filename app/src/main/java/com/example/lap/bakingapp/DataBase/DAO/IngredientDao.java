package com.example.lap.bakingapp.DataBase.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.lap.bakingapp.DataBase.ENTITY.IngredientEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface IngredientDao {

    @Insert(onConflict = REPLACE)
    void insertAll(List<IngredientEntity> ingredientEntities);
}

