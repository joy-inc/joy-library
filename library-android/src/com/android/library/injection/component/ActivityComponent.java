package com.android.library.injection.component;

import android.app.Activity;

import com.android.library.injection.ActivityScope;
import com.android.library.injection.module.ActivityModule;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {

    // Expose the activity to sub-graphs.
    Activity activity();
}
