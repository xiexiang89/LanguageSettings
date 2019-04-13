package com.edgar.language;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by Edgar on 2018/12/21.
 */
public class TestActivity extends LocaleActivity {

    private Button mSettingLanguageBtn;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSettingLanguageBtn = findViewById(R.id.settings_language);
        setTitle(getString(R.string.settings_language));
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
        switch (view.getId()) {
            case R.id.settings_language:
                startActivity(new Intent(this,LanguageListActivity.class));
                break;
            case R.id.show_dialog:
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this).create();
                }
                mDialog.setMessage(getString(R.string.settings_language));
                mDialog.show();
                break;
        }
    }

    @Override
    public void onLocaleChanged(Locale locale) {
        setTitle(getString(R.string.settings_language));
        mSettingLanguageBtn.setTextLocale(locale);
        mSettingLanguageBtn.setText(R.string.settings_language);
    }
}
