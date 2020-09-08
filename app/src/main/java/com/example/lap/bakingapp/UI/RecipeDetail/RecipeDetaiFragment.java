package com.example.lap.bakingapp.UI.RecipeDetail;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lap.bakingapp.EspressoIdlingResource;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;
import com.example.lap.bakingapp.UI.Widget.RecipeWidget;
import com.example.lap.bakingapp.ViewModel.RecipeDetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.LinearLayout.VERTICAL;

@SuppressWarnings("WeakerAccess")
public class RecipeDetaiFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "arg_recipe_id";
    private static final String ARG_IS_TWO_PANE = "arg_is_two_pane";
    private static final String KEY_LAYOUTMANAGER_STATE = "key_layoutmanager_state";
    private static final String KEY_SELECTED_ITEM_POSITION = "key_selected_item_position";
    private int selectedItemPosition = -1;
    private long recipeId;
    private boolean isTwoPane;
    private RecipeDetailAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable layoutManagerState;
    private RecipeDetailViewModel viewModel;

    @BindView(R.id.tv_ingredients)
    TextView ingredientsTextView;
    @BindView(R.id.rv_steps)
    RecyclerView stepsRecyclerView;

    private EspressoIdlingResource espressoIdlingResource;

    public RecipeDetaiFragment() {
    }

    public static RecipeDetaiFragment newInstance(long recipeId, boolean isTwoPane) {
        RecipeDetaiFragment fragment = new RecipeDetaiFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putBoolean(ARG_IS_TWO_PANE, isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("VisibleForTests")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            recipeId = getArguments().getLong(ARG_RECIPE_ID);
            isTwoPane = getArguments().getBoolean(ARG_IS_TWO_PANE);
        }
//noinspection ConstantConditions
        espressoIdlingResource = (EspressoIdlingResource)
                ((RecipeDetailActivity) getActivity()).getIdlingResource();
        espressoIdlingResource.setIdleState(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_detail, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        setupViewModel(recipeId);
        return view;
    }

    @SuppressLint("WrongConstant")
    private void setupRecyclerView() {
        //noinspection ConstantConditions
        adapter = new RecipeDetailAdapter(getContext(),
                getActivity().getLayoutInflater(),
                (RecipeDetailAdapter.StepItemClickListener) getActivity(),
                isTwoPane);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(VERTICAL);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.setAdapter(adapter);

        @SuppressWarnings("ConstantConditions")
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setupViewModel(long id) {
        //noinspection ConstantConditions
        viewModel =
                ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
        viewModel.init(id);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {

                    if (recipe.getIngredients() != null) {
                        ingredientsTextView.setText(recipe.getIngredientsAsString());
                    }
                    if (recipe.getSteps() != null) {
                        adapter.updateItems(recipe.getSteps());
                    }

                    if (espressoIdlingResource != null) {
                        espressoIdlingResource.setIdleState(true);
                    }
                }
            }
        });
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAYOUTMANAGER_STATE, linearLayoutManager.onSaveInstanceState());
        outState.putInt(KEY_SELECTED_ITEM_POSITION, selectedItemPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUTMANAGER_STATE);
            selectedItemPosition = savedInstanceState.getInt(KEY_SELECTED_ITEM_POSITION);
            if (selectedItemPosition >= 0 && isTwoPane) {
                adapter.setSelectedItemPosition(selectedItemPosition);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (layoutManagerState != null) {
            linearLayoutManager.onRestoreInstanceState(layoutManagerState);
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_to_widget) {
            viewModel.setToWidget();
            viewModel.getRecipe().observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable Recipe recipe) {
                    if (recipe != null) {
                        @SuppressWarnings("ConstantConditions")
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
                                getContext().getApplicationContext());
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                                getContext().getApplicationContext(), RecipeWidget.class));

                        RecipeWidget.updateRecipeWidgets(getContext(),
                                appWidgetManager,
                                recipe.getId(),
                                recipe.getName(),
                                recipe.getIngredientsAsString(),
                                appWidgetIds);
                        Toast.makeText(getContext(),
                                getString(R.string.toast_widget_added),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
