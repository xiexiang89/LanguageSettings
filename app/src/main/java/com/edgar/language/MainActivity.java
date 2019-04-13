package com.edgar.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends LocaleActivity {

    private Button mSettingLanguageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingLanguageBtn = findViewById(R.id.settings_language);
        setTitle(R.string.settings_language);
        mSettingLanguageBtn.setText(getString(R.string.settings_language));
    }

    public void onClick(View view) {
        startActivity(new Intent(this,TestActivity.class));
    }

    @Override
    public void onLocaleChanged(Locale locale) {
        setTitle(R.string.settings_language);
        mSettingLanguageBtn.setTextLocale(locale);
        mSettingLanguageBtn.setText(R.string.settings_language);
    }
}
