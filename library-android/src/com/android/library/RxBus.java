package com.android.library;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Daisw on 16/6/19.
 */
public class RxBus {

    private static volatile RxBus mRxBus;
    private final Subject<Object, Object> mSubject;

    private RxBus() {

        mSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus get() {

        if (mRxBus == null) {

            synchronized (RxBus.class) {

                if (mRxBus == null) {

                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    public void postCompleted() {

        mSubject.onCompleted();
    }

    public void postError(Throwable e) {

        mSubject.onError(e);
    }

    public void postNext(Object event) {

        mSubject.onNext(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {

        return mSubject.ofType(eventType);
    }
}
