package com.edgar.language;

import android.app.Application;
import android.content.Context;

/**
 * Created by Edgar on 2018/12/20.
 */
public class LanguageSettingsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocaleProvider.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleProvider.attachBaseContext(base));
    }
}