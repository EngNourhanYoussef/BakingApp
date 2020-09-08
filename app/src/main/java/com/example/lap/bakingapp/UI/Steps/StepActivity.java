package com.example.lap.bakingapp.UI.Steps;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.ViewModel.RecipeDetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("WeakerAccess")
public class StepActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "extra_name";
    private static final String EXTRA_STEP_NUMBER = "extra_actual_step";
    private int stepNumber;
    private Recipe recipe;
    StepFragment fragment;
    @BindView(R.id.tv_page)
    TextView pageTextView;
    @BindView(R.id.btn_prev)
    Button prevButton;
    @BindView(R.id.btn_next)
    Button nextButton;

    public static Intent getStarterIntent(Context context, long recipeId, int stepNumber) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_STEP_NUMBER, stepNumber);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);
        ButterKnife.bind(this);

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, 0);
        stepNumber = getIntent().getIntExtra(EXTRA_STEP_NUMBER, 0);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel(recipeId, savedInstanceState);
    }

    private void setupStepFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragment = StepFragment.newInstance(recipe.getSteps().get(stepNumber).getStepId(), false);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_fragment_container, fragment)
                    .commit();
        }
    }

    private void setupViewModel(long id, final Bundle savedInstanceState) {
        RecipeDetailViewModel viewModel =
                ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        viewModel.init(id);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    getSupportActionBar().setTitle(recipe.getName());
                    StepActivity.this.recipe = recipe;
                    getSupportActionBar().setTitle(recipe.getName());

                    setupStepFragment(savedInstanceState);
                    setupBottomBar();
                }
            }
        });
    }

    private void setupBottomBar() {
        int maxStepNumber = recipe.getSteps().get(recipe.getSteps().size() - 1).getStepNumber();

        if (stepNumber == 0) {
            prevButton.setEnabled(false);
            prevButton.setAlpha(0.3f);

        } else if (0 < stepNumber && stepNumber < (maxStepNumber)) {
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);
            prevButton.setAlpha(1f);
            nextButton.setAlpha(1f);
        } else if (stepNumber >= maxStepNumber) {
            nextButton.setEnabled(false);
            nextButton.setAlpha(0.3f);
        }
        pageTextView.setText(getString(R.string.steps,
                String.valueOf(recipe.getSteps().get(stepNumber).getStepNumber()),
                String.valueOf(maxStepNumber)));

    }

    private void changeStepFragment() {
        fragment = StepFragment.newInstance(recipe.getSteps().get(stepNumber).getStepId(), false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_fragment_container, fragment)
                .commit();
    }

    @OnClick(R.id.btn_prev)
    void onPrevButtonClicked() {
        stepNumber--;
        setupBottomBar();
        changeStepFragment();
    }

    @OnClick(R.id.btn_next)
    void onNextButtonClicked() {
        stepNumber++;
        setupBottomBar();
        changeStepFragment();
    }
}
