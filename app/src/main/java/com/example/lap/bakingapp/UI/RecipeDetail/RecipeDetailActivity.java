package com.example.lap.bakingapp.UI.RecipeDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;

import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.example.lap.bakingapp.EspressoIdlingResource;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.UI.Steps.StepActivity;
import com.example.lap.bakingapp.UI.Steps.StepFragment;
import com.example.lap.bakingapp.ViewModel.RecipeDetailViewModel;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.StepItemClickListener {

    public static Intent getStarterIntent(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    private static final String EXTRA_RECIPE_ID = "extra_recipe_data";
    private static final String TAG_RECIPE_DATA_FRAGMENT = "tag_recipe_data_fragment";
    private long recipeId;
    private boolean isTwoPane;
    private RecipeDetaiFragment recipeDataFragment;

    @Nullable
    private EspressoIdlingResource espressoIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recipe);
        recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, 0);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.step_fragment_container) != null) {
            isTwoPane = true;
        }

        setupDataFragment(savedInstanceState);
        setupViewModel(recipeId);
    }

    private void setupDataFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            recipeDataFragment = RecipeDetaiFragment.newInstance(recipeId, isTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, recipeDataFragment,TAG_RECIPE_DATA_FRAGMENT)
                    .commit();
        } else {
            recipeDataFragment = (RecipeDetaiFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_RECIPE_DATA_FRAGMENT);
        }
    }

    private void setupViewModel(long id) {
        RecipeDetailViewModel viewModel =
                ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        viewModel.init(id);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    //noinspection ConstantConditions
                    getSupportActionBar().setTitle(recipe.getName());
                }
            }
        });
    }

    @Override
    public void onStepItemClicked(StepEntity step, int position) {
        recipeDataFragment.setSelectedItemPosition(position);
        if (isTwoPane) {
            StepFragment fragment = StepFragment.newInstance(step.getStepId(), true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_container, fragment)
                    .commit();
        } else {
            Intent intent = StepActivity.getStarterIntent(this, recipeId, position);
            startActivity(intent);
        }
    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (espressoIdlingResource == null) {
            espressoIdlingResource = new EspressoIdlingResource();
        }
        return espressoIdlingResource;
    }
}

