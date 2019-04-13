package com.edgar.language;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edgar.language.lib.LocaleInfo;
import com.edgar.language.lib.LocaleManager;

/**
 * Created by Edgar on 2018/12/19.
 */
public class LanguageListActivity extends LocaleActivity {

    private static final String TAG = "LocaleProvider";
    private Toolbar mToolBar;
    private ListView mLanguageListView;
    private LocaleListAdapter mLocaleListAdapter;
    private LocaleInfo mSelectLocaleInfo;
    private LocaleManager mLocaleManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_list_activity);
        setupToolBar();
        mLocaleManager = LocaleManager.getInstance();
        mSelectLocaleInfo = mLocaleManager.getLocaleInfo();
        mLanguageListView = findViewById(R.id.language_list);
        mLocaleListAdapter = new LocaleListAdapter(this);
        mLanguageListView.setAdapter(mLocaleListAdapter);
        mLanguageListView.setItemChecked(0,true);
        mLanguageListView.setItemChecked(mLocaleListAdapter.getPosition(mSelectLocaleInfo),true);
        mLanguageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectLocaleInfo = mLocaleListAdapter.getItem(mLanguageListView.getCheckedItemPosition());
//                startActivity(new Intent(LanguageListActivity.this
//                        , MainActivity.class));
            }
        });
    }

    private void setupToolBar() {
        Log.d(TAG,"language list:"+getResources().getConfiguration().locale.toString());
        setTitle(R.string.settings_language);
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_language:
                if (!mLocaleManager.equalsLocale(mSelectLocaleInfo)) {
                    mLocaleManager.setLocale(mSelectLocaleInfo);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LocaleListAdapter extends ArrayAdapter<LocaleInfo> {

        private LocaleListAdapter(Context context) {
            super(context, 0);
            addAll(mLocaleManager.getLocaleList());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.language_list_item,parent,false);
            } else {
                view = convertView;
            }
            AppCompatCheckedTextView textView = view.findViewById(R.id.language_name);
            LocaleInfo localeInfo = getItem(position);
            textView.setText(localeInfo.name);
            return view;
        }
    }
}