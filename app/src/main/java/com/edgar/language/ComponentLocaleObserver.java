package com.edgar.language;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by Edgar on 2018/12/21.
 */
public class ComponentLocaleObserver implements LocaleProvider.OnLocaleChangedListener, LifecycleObserver {

    private Component<?> mComponent;
    private Resources mResources;

    public ComponentLocaleObserver(Context context, Lifecycle lifecycle) {
        mComponent = context instanceof Activity ? new ActivityComponent((Activity) context) : new ServiceComponent((Service) context);
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        mResources = mComponent.getResources();
        LocaleProvider.getInstance().registerOnLocaleChangedListener(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        LocaleProvider.getInstance().unregisterOnLocaleChangedListener(this);
    }

    @Override
    public void onLocaleChanged(Locale locale) {
        LocaleProvider.updateResourceLocale(mResources,locale);
        mComponent.onConfigurationChanged(mResources.getConfiguration());
    }

    private interface Component<T> {
        Resources getResources();
        void onConfigurationChanged(Configuration newConfiguration);
    }

    private class ActivityComponent implements Component<Activity> {

        private WeakReference<Activity> mActivityRef;

        @Override
        public Resources getResources() {
            return mActivityRef.get().getResources();
        }

        private ActivityComponent(Activity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfiguration) {
            mActivityRef.get().onConfigurationChanged(newConfiguration);
        }
    }

    private class ServiceComponent implements Component<Service> {
        private WeakReference<Service> mServiceRef;

        private ServiceComponent(Service service) {
            mServiceRef = new WeakReference<>(service);
        }

        @Override
        public Resources getResources() {
            return mServiceRef.get().getResources();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfiguration) {
            mServiceRef.get().onConfigurationChanged(newConfiguration);
        }
    }
}