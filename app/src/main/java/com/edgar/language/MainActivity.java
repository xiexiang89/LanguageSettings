package com.edgar.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends LocaleActivity implements LocaleProvider.OnLocaleChangedListener {

    private Button mSettingLanguageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingLanguageBtn = findViewById(R.id.settings_language);
        LocaleProvider.getInstance().registerOnLocaleChangedListener(this);
    }

    public void onClick(View view) {
        startActivity(new Intent(this,TestActivity.class));
    }

    @Override
    protected void onDestroy() {
        LocaleProvider.getInstance().unregisterOnLocaleChangedListener(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSettingLanguageBtn.setText(getString(R.string.settings_language));
    }

    @Override
    public void onLocaleChanged(Locale locale) {
        LocaleProvider.updateResourceLocale(getResources(),locale);
        onConfigurationChanged(getResources().getConfiguration());
    }
}
