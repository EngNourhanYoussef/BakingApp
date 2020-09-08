package com.example.lap.bakingapp;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import android.os.Bundle;

import java.util.concurrent.atomic.AtomicBoolean;

public class EspressoIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;

    // Idleness is controlled with this boolean.
    private final AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     *
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && callback != null) {
            //noinspection ConstantConditions
            callback.onTransitionToIdle();
        }
    }
}