package com.example.lap.bakingapp.UI.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcelable;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.ViewModel.RecipeDetailViewModel;
import com.example.lap.bakingapp.ViewModel.RecipeListViewModel;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class WidgetConfigureActivity extends AppCompatActivity implements RecipesAdapterWidget.RecipeItemClickListener {

    private static final String KEY_LAYOUT_MANAGER = "key_layout_manager";
    private RecipesAdapterWidget adapter;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable layoutState;
    @BindView(R.id.rv_recipes)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_configure);
        MyApplication.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        adapter = new RecipesAdapterWidget(getLayoutInflater(), this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
    }

    private void setupViewModel() {
       RecipeListViewModel viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>()
         {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size() > 0) {
                    adapter.updateItems(recipes);
                }
            }
        });
    }

    @Override
    public void onRecipeItemClicked(Recipe recipeData) {
        RecipeDetailViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        recipeViewModel.init(recipeData.getId());
        recipeViewModel.setToWidget();
        recipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
                            getApplicationContext());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                            getApplicationContext(), RecipeWidget.class));

                    RecipeWidget.updateRecipeWidgets(getApplicationContext(),
                            appWidgetManager,
                            recipe.getId(),
                            recipe.getName(),
                            recipe.getIngredientsAsString(),
                            appWidgetIds);

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAYOUT_MANAGER, linearLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            layoutState = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (layoutState != null) {
            linearLayoutManager.onRestoreInstanceState(layoutState);
        }
    }
}

