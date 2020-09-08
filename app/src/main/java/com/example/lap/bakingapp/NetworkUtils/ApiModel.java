package com.example.lap.bakingapp.NetworkUtils;

public class ApiModel {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static RecipeService getRecipeService() {
        return RetrofitClinet.getClient(BASE_URL).create(RecipeService.class);
    }
}
