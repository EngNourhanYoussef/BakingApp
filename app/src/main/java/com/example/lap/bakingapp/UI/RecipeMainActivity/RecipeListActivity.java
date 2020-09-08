package com.example.lap.bakingapp.UI.RecipeMainActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.EspressoIdlingResource;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.UI.RecipeDetail.RecipeDetailActivity;
import com.example.lap.bakingapp.ViewModel.RecipeListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

@SuppressWarnings("WeakerAccess")
public class RecipeListActivity  extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    private static final String KEY_LAYOUT_MANAGER = "key_layout_manager";
    private RecipeAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable layoutState;
    @BindView(R.id.rv_recipe_list)
    RecyclerView rv;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    private EspressoIdlingResource espressoIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        MyApplication.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this);

        if (espressoIdlingResource != null) {
            espressoIdlingResource.setIdleState(false);
        }

        setSupportActionBar(toolbar);
        setupRecyclerView();
        setupViewModel();
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }


    @SuppressWarnings("ConstantConditions")
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_recipe_list);
        adapter = new RecipeAdapter(this, getLayoutInflater(), this);
        if (isTablet()) {
            gridLayoutManager = new GridLayoutManager(this, 3);
            rv.setLayoutManager(gridLayoutManager);
            rv.setAdapter(adapter);
        } else {
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    private void setupViewModel() {
        RecipeListViewModel viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size() > 0) {

                    if (espressoIdlingResource != null) {
                       espressoIdlingResource.setIdleState(true);
                    }
                    adapter.updateItems(recipes);
                }
            }
        });
    }

    @Override
    public void onRecipeItemClicked(Recipe recipeData) {
        Intent intent = RecipeDetailActivity.getStarterIntent(this, recipeData.getId());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (linearLayoutManager != null) {
            outState.putParcelable(KEY_LAYOUT_MANAGER, linearLayoutManager.onSaveInstanceState());
        } else {
            outState.putParcelable(KEY_LAYOUT_MANAGER, gridLayoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null){
            layoutState = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (layoutState != null) {
            if(linearLayoutManager !=null) {
                linearLayoutManager.onRestoreInstanceState(layoutState);
            } else {
                gridLayoutManager.onRestoreInstanceState(layoutState);
            }
        }
    }

    @NonNull
    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (espressoIdlingResource == null) {
           espressoIdlingResource = new EspressoIdlingResource();
        }

        return espressoIdlingResource;
    }
}

