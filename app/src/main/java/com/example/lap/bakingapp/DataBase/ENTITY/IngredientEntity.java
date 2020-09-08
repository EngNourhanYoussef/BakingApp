package com.example.lap.bakingapp.DataBase.ENTITY;


import android.renderscript.Sampler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Ingredient_Table" , foreignKeys = @ForeignKey(entity = RecipeEntity.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = ForeignKey.CASCADE), indices = {@Index(value = "recipe_id"), @Index(value = "ingredient_id")})


    public class IngredientEntity {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "ingredient_id")
        private long ingredientId;

        @ColumnInfo(name = "recipe_id")
        private long recipeId;

        @SerializedName("quantity")
        @Expose
        @ColumnInfo(name = "quantity")
        private double quantity;

        @SerializedName("measure")
        @Expose
        @ColumnInfo(name = "measure")
        private String measure;

        @SerializedName("ingredient")
        @Expose
        @ColumnInfo(name = "ingredient")
        private String ingredient;

        public IngredientEntity() {
        }

        @Ignore
        public IngredientEntity(long recipeId) {
            this.recipeId = recipeId;
        }

        public long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(long recipeId) {
            this.recipeId = recipeId;
        }

        public long getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }


    }


