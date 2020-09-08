package com.example.lap.bakingapp.UI.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.lap.bakingapp.Dependency_Injection.MyApplication;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.Repository.RecipeRepository;
import com.example.lap.bakingapp.UI.RecipeDetail.RecipeDetailActivity;
import com.example.lap.bakingapp.UI.RecipeMainActivity.RecipeListActivity;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class RecipeWidget extends AppWidgetProvider {

    @Inject
    RecipeRepository repository;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        MyApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyApplication.getInstance().getAppComponent().inject(this);
        String title = repository.getWidgetRecipeTitle();
        String ingredients = repository.getWidgetRecipeIngredients();
        long id = repository.getWidgetRecipeId();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id, title, ingredients, appWidgetId);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           long id, String recipeTitle, String ingredients, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id, recipeTitle, ingredients, appWidgetId);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        long id, String recipeTitle, String ingredients, int appWidgetId) {
        RemoteViews rv;
        rv = getRemoteView(context, id, recipeTitle, ingredients);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getRemoteView(Context context, long id, String recipeTitle, String ingredients) {
        Intent intent;
        if (id < 0) {
            // if the id is not set, then launch RecipeListActivity instead
            intent = new Intent(context, RecipeListActivity.class);
        } else {
            intent = RecipeDetailActivity.getStarterIntent(context, id);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Update image and text
        views.setTextViewText(R.id.tv_title, recipeTitle);
        views.setTextViewText(R.id.tv_ingredients, ingredients);
        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        return views;
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}
}

