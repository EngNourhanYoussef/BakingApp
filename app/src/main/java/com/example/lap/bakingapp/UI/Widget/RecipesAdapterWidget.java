package com.example.lap.bakingapp.UI.Widget;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipesAdapterWidget
        extends RecyclerView.Adapter<RecipesAdapterWidget.RecipeItemViewHolder> {

    private final List<Recipe> items = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final RecipeItemClickListener listener;

    RecipesAdapterWidget(LayoutInflater inflater, RecipeItemClickListener listener) {
        layoutInflater = inflater;
        this.listener = listener;
    }

    void updateItems(final List<Recipe> newItems) {
        final List<Recipe> oldItems = new ArrayList<>(this.items);
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_recipe_widget_list, parent, false);
        return new RecipeItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        private final RecipeItemClickListener recipeItemClickListener;
        private Recipe recipe;
        @BindView(R.id.tv_title)
        TextView titleTextView;


        RecipeItemViewHolder(View itemView,
                             final RecipeItemClickListener recipeItemClickListener) {
            super(itemView);
            this.recipeItemClickListener = recipeItemClickListener;

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cl_recipe_item)
        void recipeItemClicked() {
            recipeItemClickListener.onRecipeItemClicked(recipe);
        }

        private void setItem(Recipe recipe) {
            this.recipe = recipe;
            titleTextView.setText(recipe.getName());
        }
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClicked(Recipe data);
    }
}
