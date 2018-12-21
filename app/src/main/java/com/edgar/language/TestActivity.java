package com.edgar.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Edgar on 2018/12/21.
 */
public class TestActivity extends LocaleActivity {

    private Button mSettingLanguageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingLanguageBtn = findViewById(R.id.settings_language);
        setTitle(R.string.settings_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        startActivity(new Intent(this,LanguageListActivity.class));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTitle(R.string.settings_language);
        mSettingLanguageBtn.setText(getString(R.string.settings_language));
    }
}
