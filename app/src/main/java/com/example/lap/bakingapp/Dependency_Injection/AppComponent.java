package com.example.lap.bakingapp.Dependency_Injection;

import com.example.lap.bakingapp.Dependency_Injection.Modules.ApiModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.AppModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.DbModule;
import com.example.lap.bakingapp.Dependency_Injection.Modules.RepositoryModule;
import com.example.lap.bakingapp.Repository.RecipeRepository;
import com.example.lap.bakingapp.UI.RecipeMainActivity.RecipeListActivity;
import com.example.lap.bakingapp.UI.Widget.RecipeWidget;
import com.example.lap.bakingapp.UI.Widget.WidgetConfigureActivity;
import com.example.lap.bakingapp.ViewModel.RecipeDetailViewModel;
import com.example.lap.bakingapp.ViewModel.RecipeListViewModel;
import com.example.lap.bakingapp.ViewModel.StepViewModel;
import dagger.android.AndroidInjectionModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AndroidInjectionModule.class ,AppModule.class, DbModule.class, RepositoryModule.class, ApiModule.class})
public interface AppComponent {

    void inject(RecipeRepository recipeRepository);

    void inject (RecipeListActivity recipeListActivity);

    void inject (WidgetConfigureActivity widgetConfigureActivity);

    void inject (RecipeListViewModel recipesViewModel);

    void inject (RecipeDetailViewModel recipeViewModel);

    void inject (StepViewModel stepViewModel);

    void inject (RecipeWidget widget);
}
