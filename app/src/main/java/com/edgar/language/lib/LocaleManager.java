package com.edgar.language.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.edgar.language.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.edgar.language.lib.Utils.SEPARATOR;
import static com.edgar.language.lib.Utils.generateLocaleId;

/**
 * Created by Edgar on 2018/12/19.
 */
public class LocaleManager {

    private static final String TAG = "LocaleProvider";

    private static final String LOCALE_SETTINGS_NAME = "locale_settings";
    private static final String LOCALE_LANGUAGE = "language";
    private static final String LOCALE_COUNTRY = "country";
    private static final String FOLLOW_SYSTEM = "follow_system";

    static final String ZH_CN = "zh_CN";
    static final String ZH_HK = "zh_HK";
    static final String ZH_TW = "zh_TW";
    static final String EN = "en";
    static final String JA = "ja_JP";
    static final String KO = "ko_KR";

    private SharedPreferences mLocalePreferences;
    private Resources mResource;
    private List<LocaleInfo> mLocaleList;
    private final ArrayList<OnLocaleChangedListener> mListeners = new ArrayList<>();
    private LocaleInfo mLocaleInfo;

    public static LocaleManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final LocaleManager INSTANCE = new LocaleManager();
    }

    public void initialize(Context context) {
        mResource = context.getApplicationContext().getResources();
    }

    private void ensureLocalePreferences(Context context) {
        if (mLocalePreferences == null) {
            mLocalePreferences = context.getSharedPreferences(LOCALE_SETTINGS_NAME, Context.MODE_PRIVATE);
        }
    }

    private LocaleManager() { }

    private LocaleInfo getLocaleInfoFromCache(Context context) {
        if (mLocaleInfo == null) {
            ensureLocalePreferences(context);
            String language = mLocalePreferences.getString(LOCALE_LANGUAGE,"");
            String country = mLocalePreferences.getString(LOCALE_COUNTRY,"");
            if (TextUtils.isEmpty(language)) {
                mLocaleInfo = createSystemLocaleInfo(getSystemLocale());
            } else {
                mLocaleInfo = createLocaleInfo(language,country);
            }
        }
        return mLocaleInfo;
    }

    private void saveLocaleInfoToCache(LocaleInfo localeInfo) {
        final String language = localeInfo.isFollowSystem ? "" : localeInfo.getLanguage();
        final String country = localeInfo.isFollowSystem ? "" : localeInfo.getCountry();
        mLocalePreferences.edit().putString(LOCALE_LANGUAGE, language)
                .putString(LOCALE_COUNTRY, country).apply();
    }

    private LocaleInfo createLocaleInfo(String language, String country) {
        String id = generateLocaleId(language,country);
        return new LocaleInfo(id, Utils.getStringId(id),LocaleFactory.createLocale(language,country),false);
    }

    private LocaleInfo createSystemLocaleInfo(Locale locale) {
        return new LocaleInfo(FOLLOW_SYSTEM, R.string.follow_system, locale,true);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG,"System locale:"+newConfig.locale.toString());
        Log.d(TAG,"App locale:"+mResource.getConfiguration().locale.toString());
        Resources.getSystem().getConfiguration().setTo(newConfig);
        if (!mLocaleInfo.isFollowSystem) {
            updateResourceLocale(mResource, mLocaleInfo.locale);
        }
    }

    private static Locale getSystemLocale() {
        return Resources.getSystem().getConfiguration().locale;
    }

    private static void updateResourceLocale(Resources res, Locale locale) {
        final Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }

    public Context attachBaseContext(Context base) {
        LocaleInfo localeInfo = getLocaleInfoFromCache(base);
        if (localeInfo.isFollowSystem) return base;
        Log.d(TAG,"locale info: "+localeInfo.localeId);
        updateResourceLocale(base.getResources(),localeInfo.locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            base =  base.createConfigurationContext(base.getResources().getConfiguration());
        }
        updateResourceLocale(base.getResources(),localeInfo.locale);
        return base;
    }

    private void initLanguageList() {
        mLocaleList = new ArrayList<>();
        final String[] codes = mResource.getStringArray(R.array.locale_code);
        for (String code : codes) {
            final String[] array = code.split(SEPARATOR);
            final String language = array[0];
            final String country = array.length == 2 ? array[1] : "";
            mLocaleList.add(createLocaleInfo(language,country));
        }
        mLocaleList = Collections.unmodifiableList(mLocaleList);
    }

    public List<LocaleInfo> getLocaleList() {
        synchronized (this) {
            if (mLocaleList == null) {
                initLanguageList();
            }
        }
        List<LocaleInfo> localeInfoList = new ArrayList<>();
        localeInfoList.add(createSystemLocaleInfo(getSystemLocale()));
        localeInfoList.addAll(mLocaleList);
        return localeInfoList;
    }

    public boolean equalsLocale(LocaleInfo localeInfo) {
        return mLocaleInfo.equals(localeInfo);
    }

    public LocaleInfo getLocaleInfo() {
        return mLocaleInfo;
    }

    public void setLocale(LocaleInfo localeInfo) {
        if (localeInfo.equals(mLocaleInfo)) return;
        final Locale locale = localeInfo.locale;
        mLocaleInfo = localeInfo;
        saveLocaleInfoToCache(localeInfo);
        updateResourceLocale(mResource, locale);
        dispatchOnLocaleChanged(locale);
    }

    public void registerOnLocaleChangedListener(OnLocaleChangedListener listener) {
        synchronized (mListeners) {
            if (listener == null || mListeners.contains(listener)) return;
            mListeners.add(listener);
        }
    }

    public void unregisterOnLocaleChangedListener(OnLocaleChangedListener listener) {
        if (listener == null) return;
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    private void dispatchOnLocaleChanged(Locale locale) {
        synchronized (mListeners) {
            if (mListeners.isEmpty()) return;
            for (int i=0, size=mListeners.size(); i<size; i++) {
                OnLocaleChangedListener listener = mListeners.get(i);
                updateResourceLocale(listener.getResources(),locale);
                listener.onLocaleChanged(locale);
            }
        }
    }

    public interface OnLocaleChangedListener {
        void onLocaleChanged(Locale locale);
        Resources getResources();
    }
}