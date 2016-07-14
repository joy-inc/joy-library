package com.android.library.presenter;

/**
 * Created by KEVIN.DAI on 16/1/18.
 */
public class PresenterImpl<V> implements Presenter<V> {

    private V v;

    @Override
    public void attachView(V v) {

        this.v = v;
    }

    @Override
    public void detachView() {

        this.v = null;
    }

    public boolean isViewAttached() {

        return this.v != null;
    }

    public V getBaseView() {

        checkViewAttached();

        return this.v;
    }

    public void checkViewAttached() {

        if (!isViewAttached())
            throw new BaseViewNotAttachedException();
    }

    public static class BaseViewNotAttachedException extends RuntimeException {

        public BaseViewNotAttachedException() {

            super("Please call Presenter.attachView(BaseView) before requesting data to the Presenter");
        }
    }
}
