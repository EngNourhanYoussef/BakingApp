package com.example.lap.bakingapp.Repository;

import androidx.room.Relation;

import com.example.lap.bakingapp.DataBase.ENTITY.IngredientEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.RecipeEntity;
import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe extends RecipeEntity {
    @SerializedName("ingredients")
    @Expose
    @Relation(parentColumn = "recipe_id", entityColumn = "recipe_id")
    private List<IngredientEntity> ingredients = null;

    @SerializedName("steps")
    @Expose
    @Relation(parentColumn = "recipe_id", entityColumn = "recipe_id")
    private List<StepEntity> steps = null;

    public Recipe() {
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntity> steps) {
        this.steps = steps;
    }

    public String getIngredientsAsString() {

        StringBuilder ingredientsBuilder = new StringBuilder();
        for (IngredientEntity ingredient : getIngredients()) {
            if (ingredient != null) {
                ingredientsBuilder.append(ingredient.getQuantity());
                ingredientsBuilder.append(" ");
                ingredientsBuilder.append(ingredient.getMeasure());
                ingredientsBuilder.append(" ");
                ingredientsBuilder.append(ingredient.getIngredient());
                ingredientsBuilder.append("\n");
            }
        }
        return ingredientsBuilder.toString();

    }
}
