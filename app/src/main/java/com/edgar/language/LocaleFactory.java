package com.edgar.language;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Edgar on 2018/12/21.
 */
class LocaleFactory {

    private static final Map<String, Locale> LOCALE_MAP = new HashMap<String, Locale>() {
        {
            put(LocaleProvider.ZH, Locale.SIMPLIFIED_CHINESE);
            put(LocaleProvider.ZH_CN, Locale.SIMPLIFIED_CHINESE);
            put(LocaleProvider.ZH_TW, Locale.TRADITIONAL_CHINESE);
            put(LocaleProvider.ZH_HK, Locale.TRADITIONAL_CHINESE);
            put(LocaleProvider.EN, Locale.ENGLISH);
            put(LocaleProvider.JA, Locale.JAPAN);
            put(LocaleProvider.KO, Locale.KOREA);
        }
    };

    static Locale createLocale(String language, String country) {
        country = TextUtils.isEmpty(country) ? "" : country.toUpperCase();
        final String localeName = generateLocaleName(language, country);
        Locale locale = LOCALE_MAP.get(localeName);
        if (locale == null) {
            locale = new Locale(language,country);
            LOCALE_MAP.put(localeName, locale);
        }
        return locale;
    }

    private static String generateLocaleName(String language, String country) {
        return country.length() == 0 ? language : language + "_" + country;
    }
}