package com.example.lap.bakingapp.DataBase.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface StepDao {
    @Query("SELECT * FROM step_table WHERE step_id = :stepId")
    LiveData<StepEntity> loadById(long stepId);

    @Insert(onConflict = REPLACE)
    void insertAll(List<StepEntity> steps);
}
