package com.edgar.language;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by Edgar on 2018/12/20.
 */
public abstract class LocaleActivity extends AppCompatActivity implements LocaleProvider.OnLocaleChangedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleProvider.getInstance().registerOnLocaleChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocaleProvider.getInstance().unregisterOnLocaleChangedListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleProvider.getInstance().attachBaseContext(newBase));
    }

    @CallSuper
    @Override
    public void onLocaleChanged(Locale locale) {
        LocaleProvider.updateResourceLocale(getResources(),locale);
    }
}
