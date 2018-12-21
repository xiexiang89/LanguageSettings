package com.edgar.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends LocaleActivity {

    private Button mSettingLanguageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingLanguageBtn = findViewById(R.id.settings_language);
    }

    public void onClick(View view) {
        startActivity(new Intent(this,LanguageListActivity.class));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSettingLanguageBtn.setText(getString(R.string.settings_language));
    }
}
