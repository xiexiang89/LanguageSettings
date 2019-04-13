package com.edgar.language;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.edgar.language.lib.LocaleManager;

import java.util.Locale;

/**
 * Created by Edgar on 2018/12/20.
 */
public abstract class LocaleActivity extends AppCompatActivity implements LocaleManager.OnLocaleChangedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleManager.getInstance().registerOnLocaleChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocaleManager.getInstance().unregisterOnLocaleChangedListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.getInstance().attachBaseContext(newBase));
    }

    @Override
    public void onLocaleChanged(Locale locale) { }
}
