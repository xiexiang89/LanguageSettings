package com.edgar.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Edgar on 2018/12/19.
 */
public class LocaleProvider {

    private static final String TAG = "LocaleProvider";
    private static final String LOCALE_SETTINGS_NAME = "locale_settings";
    private static final String LOCALE_LANGUAGE = "language";
    private static final String LOCALE_COUNTRY = "country";

    private static SharedPreferences sLocalePreferences;

    private Resources mResource;
    private List<LocaleInfo> mLocaleList;
    private final ArrayList<OnLocaleChangedListener> mListeners = new ArrayList<>();
    private Locale mLocale;

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
        mLocale = getCacheLocale(context);
    }

    private static Locale getCacheLocale(Context context) {
        ensureLocalePreferences(context);
        String language = sLocalePreferences.getString(LOCALE_LANGUAGE,"");
        String country = sLocalePreferences.getString(LOCALE_COUNTRY,"");
        if (TextUtils.isEmpty(language)) return getSystemLocale();
        return LocaleFactory.getLocale(language,country);
    }

    private static void saveLocaleInfo(Locale locale) {
        final boolean isSystemLocale = locale == getSystemLocale();
        final String language = isSystemLocale ? "" : locale.getLanguage();
        final String country = isSystemLocale ? "" : locale.getCountry();
        sLocalePreferences.edit().putString(LOCALE_LANGUAGE, language)
                .putString(LOCALE_COUNTRY, country).apply();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return base.createConfigurationContext(configuration);
        } else {
            res.updateConfiguration(configuration,res.getDisplayMetrics());
            return base;
        }
    }

    private void initLanguageList() {
        mLocaleList = new ArrayList<>();
        addLocaleInfo(R.string.follow_system, getSystemLocale());
        addLocaleInfo(R.string.chinese_simplified, Locale.SIMPLIFIED_CHINESE);
        addLocaleInfo(R.string.chinese_traditional, Locale.TRADITIONAL_CHINESE);
        addLocaleInfo(R.string.english, Locale.ENGLISH);
        addLocaleInfo(R.string.japanese, Locale.JAPANESE);
        addLocaleInfo(R.string.korean, Locale.KOREA);
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

    public Locale getLocale() {
        return mLocale;
    }

    public void updateLocale(Locale locale) {
        if (mLocale != null && mLocale.equals(locale)) {
            return;
        }
        mLocale = locale;
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