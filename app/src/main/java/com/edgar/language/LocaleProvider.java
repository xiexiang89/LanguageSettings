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
import java.util.Collections;
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

    private SharedPreferences mLocalePreferences;
    private static final Map<String,Integer> LOCALE_NAME_ARRAY = new HashMap<String, Integer>() {
        {
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
    private Locale mLocale;
    private boolean isFollowSystem;

    private static volatile LocaleProvider sInstance;

    public static LocaleProvider getInstance() {
        if (sInstance == null) {
            synchronized (LocaleProvider.class) {
                if (sInstance == null) {
                    sInstance = new LocaleProvider();
                }
            }
        }
        return sInstance;
    }

    public void initialize(Context context) {
        mResource = context.getApplicationContext().getResources();
    }

    private void ensureLocalePreferences(Context context) {
        if (mLocalePreferences == null) {
            mLocalePreferences = context.getSharedPreferences(LOCALE_SETTINGS_NAME, Context.MODE_PRIVATE);
        }
    }

    private LocaleProvider() { }

    private Locale getCacheLocale(Context context) {
        if (mLocale == null) {
            ensureLocalePreferences(context);
            String language = mLocalePreferences.getString(LOCALE_LANGUAGE,"");
            String country = mLocalePreferences.getString(LOCALE_COUNTRY,"");
            if (TextUtils.isEmpty(language)) {
                mLocale = getSystemLocale();
                isFollowSystem = true;
            } else {
                mLocale = LocaleFactory.createLocale(language,country);
            }
        }
        return mLocale;
    }

    private void saveLocaleInfo(Locale locale, boolean isFollowSystem) {
        final String language = isFollowSystem ? "" : locale.getLanguage();
        final String country = isFollowSystem ? "" : locale.getCountry();
        mLocalePreferences.edit().putString(LOCALE_LANGUAGE, language)
                .putString(LOCALE_COUNTRY, country).apply();
    }

    public Locale getLocale() {
        return mLocale;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG,"System switch locale:"+newConfig.locale.toString());
        Locale locale = CompatUtils.getLocale(newConfig);
        updateResourceLocale(Resources.getSystem(),locale);
        if (isFollowSystem) {
            mLocale = locale;
            updateResourceLocale(mResource,locale);
        }
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

    public Context attachBaseContext(Context base) {
        Locale locale = getCacheLocale(base);
        Log.d(TAG,"locale info: "+locale.toString());
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
            Locale locale = LocaleFactory.createLocale(array[0], array.length == 2 ? array[1] : null);
            mLocaleList.add(new LocaleInfo(LOCALE_NAME_ARRAY.get(code), locale,false));
        }
        mLocaleList = Collections.unmodifiableList(mLocaleList);
    }

    public List<LocaleInfo> getLocaleList() {
        synchronized (this) {
            if (mLocaleList == null) {
                initLanguageList();
            }
        }
        List<LocaleInfo> localeInfos = new ArrayList<>();
        localeInfos.add(new LocaleInfo(R.string.follow_system,getSystemLocale(),true));
        localeInfos.addAll(mLocaleList);
        return localeInfos;
    }

    public void updateLocale(LocaleInfo localeInfo) {
        final Locale locale = localeInfo.locale;
        isFollowSystem = localeInfo.isFollowSystem;
        if (locale != getSystemLocale() || locale != mLocale) {
            mLocale = locale;
            saveLocaleInfo(locale,localeInfo.isFollowSystem);
            updateResourceLocale(mResource, locale);
            dispatchOnLocaleChanged(locale);
        }
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
        public final boolean isFollowSystem;

        private LocaleInfo(@StringRes int name, Locale locale,boolean isFollowSystem) {
            this.name = name;
            this.locale = locale;
            this.isFollowSystem = isFollowSystem;
        }
    }

    public interface OnLocaleChangedListener {
        void onLocaleChanged(Locale locale);
    }
}