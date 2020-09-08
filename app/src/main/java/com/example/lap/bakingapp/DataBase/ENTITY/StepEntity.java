package com.example.lap.bakingapp.DataBase.ENTITY;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "step_table",
        foreignKeys =
        @ForeignKey(entity = RecipeEntity.class,
                parentColumns = "recipe_id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE),
        indices = {
                @Index(value = "recipe_id"),
                @Index(value = "step_id")})
public class StepEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "step_id")
    private long stepId;

    @ColumnInfo(name = "recipe_id")
    private long recipeId;

    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "step_number")
    private int stepNumber;

    @SerializedName("shortDescription")
    @Expose
    @ColumnInfo(name = "shortDescription")
    private String shortDescription;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("videoURL")
    @Expose
    @ColumnInfo(name = "videoURL")
    private String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    @ColumnInfo(name = "thumbnailURL")
    private String thumbnailURL;

    public StepEntity() {
    }

    @Ignore
    public StepEntity(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}