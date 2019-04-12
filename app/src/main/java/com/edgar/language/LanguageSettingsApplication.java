package com.edgar.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by Edgar on 2018/12/20.
 */
public class LanguageSettingsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocaleProvider.getInstance().initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleProvider.getInstance().attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleProvider.getInstance().onConfigurationChanged(newConfig);
    }
}