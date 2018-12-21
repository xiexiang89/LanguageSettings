package com.edgar.language;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Edgar on 2018/12/20.
 */
public abstract class LocaleActivity extends AppCompatActivity {

    private ComponentLocaleObserver mComponentLocaleObserver = new ComponentLocaleObserver(this, getLifecycle());

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleProvider.attachBaseContext(newBase));
    }
}
