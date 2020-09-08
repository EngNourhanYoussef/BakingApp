package com.example.lap.bakingapp.UI.RecipeDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.example.lap.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.StepItemViewHolder> {

    private final List<StepEntity> items = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final StepItemClickListener listener;
    private final boolean isTwoPane;
    private final Context context;

    private int selectedItemPosition = -1;

    RecipeDetailAdapter(Context context, LayoutInflater layoutInflater,
                        StepItemClickListener listener, boolean isTwoPane) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.isTwoPane = isTwoPane;

    }

    void updateItems(final List<StepEntity> newItems) {
        final List<StepEntity> oldItems = new ArrayList<>(this.items);
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
    public StepItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_steps, parent, false);
        return new StepItemViewHolder(context, v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StepItemViewHolder holder, int position) {
        holder.setItem(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    class StepItemViewHolder extends RecyclerView.ViewHolder {

        private final StepItemClickListener stepItemClickListener;
        private final Context context;
        private StepEntity step;
        private int position;
        @BindView(R.id.tv_title)
        TextView titleTextView;

        final View itemView;


        StepItemViewHolder(Context context, View itemView,
                           final StepItemClickListener stepItemClickListener) {
            super(itemView);
            this.context = context;
            this.stepItemClickListener = stepItemClickListener;
            this.itemView = itemView;

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cl_recipe_item)
        void recipeItemClicked() {
            selectedItemPosition = position;
            notifyDataSetChanged();
            stepItemClickListener.onStepItemClicked(step, position);
        }


        private void setItem(StepEntity step, int position) {
            this.step = step;
            this.position = position;
            if (isTwoPane) {
                if (selectedItemPosition == position) {

                  itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                } else {
                  itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                }
            }
            titleTextView.setText(step.getShortDescription());
        }
    }

    public interface StepItemClickListener {
        void onStepItemClicked(StepEntity step, int position);
    }
}
