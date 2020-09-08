package com.example.lap.bakingapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.Repository.RecipeRepository;

import javax.inject.Inject;

public class StepViewModel extends ViewModel {private LiveData<StepEntity> step;
    private long stepId;

    @SuppressWarnings("WeakerAccess")
    @Inject
    RecipeRepository repository;

    public StepViewModel() {
   MyApplication.getInstance().getAppComponent().inject(this);
        stepId = -1;
    }

    public void init(long stepId) {
        if (this.stepId != -1) {
            return;
        }
        this.stepId = stepId;
    }

    public LiveData<StepEntity> getStep() {
        if (stepId == -1) {
            return null;
        }
        if (step == null) {
            step = repository.getStep(stepId);
        }
        return step;
    }
}


