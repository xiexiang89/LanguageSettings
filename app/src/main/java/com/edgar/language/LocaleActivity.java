package com.edgar.language;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Edgar on 2018/12/20.
 */
public abstract class LocaleActivity extends AppCompatActivity {

    private ComponentLocaleObserver mComponentLocaleObserver = new ComponentLocaleObserver(this, getLifecycle());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleProvider.attachBaseContext(newBase));
    }
}
