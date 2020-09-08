package com.example.lap.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import android.support.test.runner.AndroidJUnit4;


import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.lap.bakingapp.UI.RecipeDetail.RecipeDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private IdlingResource idlingResource;

    @Rule
    public final ActivityTestRule<RecipeDetailActivity> activityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

    @Before
    public void registerIdlingResource() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = RecipeDetailActivity.getStarterIntent(context, 1);
        activityTestRule.launchActivity(intent);

        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void onStepClick_StepDetailFragmentLaunch() {
        onView(ViewMatchers.withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        onView(ViewMatchers.withId(R.id.tv_step_no)).check(matches(isDisplayed()));
    }
}
