package com.android.library.injection.component;

import com.android.library.BaseApplication;
import com.android.library.httptask.RetroRequestQueue;
import com.android.library.injection.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {

    // Exported for child-components.
    BaseApplication application();

    RetroRequestQueue requestQueue();
}
