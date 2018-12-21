package com.edgar.language;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Edgar on 2018/12/19.
 */
public class LanguageListActivity extends LocaleActivity {

    private Toolbar mToolBar;
    private ListView mLanguageListView;
    private LocaleListAdapter mLocaleListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_list_activity);
        setupToolBar();
        mLanguageListView = findViewById(R.id.language_list);
        mLocaleListAdapter = new LocaleListAdapter(this);
        mLanguageListView.setAdapter(mLocaleListAdapter);
        mLanguageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocaleProvider.LocaleInfo localeInfo = mLocaleListAdapter.getItem(position);
                LocaleProvider.getInstance().updateLocale(localeInfo.locale);
            }
        });
    }

    private void setupToolBar() {
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.settings_language);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToolBar.setTitle(getString(R.string.settings_language));
        mLocaleListAdapter.notifyDataSetChanged();
    }

    private class LocaleListAdapter extends ArrayAdapter<LocaleProvider.LocaleInfo> {

        private LocaleListAdapter(Context context) {
            super(context, 0);
            addAll(LocaleProvider.getInstance().getLocaleList());
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
            TextView textView = view.findViewById(R.id.language_name);
            textView.setText(getItem(position).name);
            return view;
        }
    }
}