package com.android.library.ui.webview;

import com.android.library.injection.ActivityScope;
import com.android.library.injection.component.AppComponent;

import dagger.Component;

/**
 * Created by Daisw on 16/8/16.
 */

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = BaseWebViewModule.class
)
public interface BaseWebViewComponent {

    void inject(BaseWebViewActivity activity);
    void inject(BaseWebViewActivityNoTitle activity);
}
