package com.edgar.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.edgar.language.lib.LocaleManager;

/**
 * Created by Edgar on 2018/12/20.
 */
public class LanguageSettingsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocaleManager.getInstance().initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.getInstance().attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.getInstance().onConfigurationChanged(newConfig);
    }
}