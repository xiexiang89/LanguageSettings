package com.edgar.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Edgar on 2018/12/19.
 */
public class LocaleProvider {

    private static final String TAG = "LocaleProvider";
    private static final String FOLLOW_SYSTEM = "0";
    private static final String LOCALE_SETTINGS_NAME = "locale_settings";
    private static final String LOCALE_LANGUAGE = "language";
    private static final String LOCALE_COUNTRY = "country";
    static final String ZH = "zh";
    static final String ZH_CN = "zh_CN";
    static final String ZH_HK = "zh_HK";
    static final String ZH_TW = "zh_TW";
    static final String EN = "en";
    static final String JA = "ja_JP";
    static final String KO = "ko_KR";

    private static SharedPreferences sLocalePreferences;
    private static final Map<String,Integer> LOCALE_NAME_ARRAY = new HashMap<String, Integer>() {
        {
            put(FOLLOW_SYSTEM, R.string.follow_system);
            put(ZH, R.string.chinese_simplified);
            put(ZH_CN, R.string.chinese_simplified);
            put(ZH_HK, R.string.chinese_traditional);
            put(ZH_TW, R.string.chinese_traditional);
            put(EN, R.string.english);
            put(JA, R.string.japanese);
            put(KO, R.string.korean);
        }
    };

    private Resources mResource;
    private List<LocaleInfo> mLocaleList;
    private final ArrayList<OnLocaleChangedListener> mListeners = new ArrayList<>();
    private static Locale sLocale;

    private static LocaleProvider sInstance;

    public static LocaleProvider getInstance() {
        synchronized (LocaleProvider.class) {
            return sInstance;
        }
    }

    public static void initialize(Context context) {
        synchronized (LocaleProvider.class) {
            if (sInstance == null) {
                sInstance = new LocaleProvider(context);
            }
        }
    }

    private static void ensureLocalePreferences(Context context) {
        if (sLocalePreferences == null) {
            sLocalePreferences = context.getSharedPreferences(LOCALE_SETTINGS_NAME, Context.MODE_PRIVATE);
        }
    }

    private LocaleProvider(Context context) {
        mResource = context.getApplicationContext().getResources();
    }

    private static Locale getCacheLocale(Context context) {
        if (sLocale == null) {
            ensureLocalePreferences(context);
            String language = sLocalePreferences.getString(LOCALE_LANGUAGE,"");
            String country = sLocalePreferences.getString(LOCALE_COUNTRY,"");
            if (TextUtils.isEmpty(language)) return getSystemLocale();
            sLocale = LocaleFactory.getLocale(language,country);
        }
        return sLocale;
    }

    private static void saveLocaleInfo(Locale locale) {
        final boolean isSystemLocale = locale == getSystemLocale();
        final String language = isSystemLocale ? "" : locale.getLanguage();
        final String country = isSystemLocale ? "" : locale.getCountry();
        sLocalePreferences.edit().putString(LOCALE_LANGUAGE, language)
                .putString(LOCALE_COUNTRY, country).apply();
    }

    public static Locale getLocale() {
        return sLocale;
    }

    public static Locale getSystemLocale() {
        return Resources.getSystem().getConfiguration().locale;
    }

    public static void updateResourceLocale(Resources res, Locale locale) {
        final Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }

    public static Context attachBaseContext(Context base) {
        Locale locale = getCacheLocale(base);
        Log.d(TAG,"locale info: "+locale.toString());
        if (locale == getSystemLocale()) {
            return base;
        }
        final Resources res = base.getResources();
        Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        res.updateConfiguration(configuration,res.getDisplayMetrics());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            base =  base.createConfigurationContext(configuration);
        }
        return base;
    }

    private void initLanguageList() {
        mLocaleList = new ArrayList<>();
        final String[] codes = mResource.getStringArray(R.array.locale_code);
        for (final String code : codes) {
            final String[] array = code.split("_");
            Locale locale = FOLLOW_SYSTEM.equals(code) ? getSystemLocale() : LocaleFactory.getLocale(array[0], array.length == 2 ? array[1] : "");
            addLocaleInfo(LOCALE_NAME_ARRAY.get(code).intValue(), locale);
        }
    }

    private void addLocaleInfo(@StringRes int name, Locale locale) {
        mLocaleList.add(new LocaleInfo(name, locale));
    }

    public List<LocaleInfo> getLocaleList() {
        synchronized (this) {
            if (mLocaleList == null) {
                initLanguageList();
            }
        }
        return new ArrayList<>(mLocaleList);
    }

    public void updateLocale(Locale locale) {
        if (sLocale != null && locale != getSystemLocale() && sLocale != getSystemLocale() && sLocale.equals(locale)) {
            return;
        }
        sLocale = locale;
        saveLocaleInfo(locale);
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
                mListeners.get(i).onLocaleChanged(locale);
            }
        }
    }

    public static class LocaleInfo {

        public final @StringRes int name;
        public final Locale locale;

        private LocaleInfo(@StringRes int name, Locale locale) {
            this.name = name;
            this.locale = locale;
        }
    }

    public interface OnLocaleChangedListener {
        void onLocaleChanged(Locale locale);
    }
}