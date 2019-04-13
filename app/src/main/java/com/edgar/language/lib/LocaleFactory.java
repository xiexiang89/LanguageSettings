package com.edgar.language.lib;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.edgar.language.lib.Utils.generateLocaleId;

/**
 * Created by Edgar on 2018/12/21.
 */
class LocaleFactory {

    private static final Map<String, Locale> LOCALE_MAP = new HashMap<String, Locale>() {
        {
            put(LocaleManager.ZH_CN, Locale.SIMPLIFIED_CHINESE);
            put(LocaleManager.ZH_TW, Locale.TRADITIONAL_CHINESE);
            put(LocaleManager.ZH_HK, Locale.TRADITIONAL_CHINESE);
            put(LocaleManager.EN, Locale.ENGLISH);
            put(LocaleManager.JA, Locale.JAPAN);
            put(LocaleManager.KO, Locale.KOREA);
        }
    };

    static Locale createLocale(String language, String country) {
        country = country == null ? "" : country.toUpperCase();
        final String localeId = generateLocaleId(language, country);
        Locale locale = LOCALE_MAP.get(localeId);
        if (locale == null) {
            locale = new Locale(language,country);
            LOCALE_MAP.put(localeId, locale);
        }
        return locale;
    }
}