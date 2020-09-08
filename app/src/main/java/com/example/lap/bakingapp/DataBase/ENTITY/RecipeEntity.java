package com.example.lap.bakingapp.DataBase.ENTITY;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Recipe_Table" , indices ={ @Index(value = "recipe_id")})
public class RecipeEntity {
    @SerializedName("id")
    @Expose
    @PrimaryKey()
    @ColumnInfo(name = "recipe_id")
    private int id;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name ;

    @SerializedName("image")
    @Expose
    @ColumnInfo( name = "image")
    private String image;

    public RecipeEntity(){}

    public int getId (){
        return id;
    }
    public void setId( int id){
        this.id = id;
    }
    public  String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;

    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }



}
