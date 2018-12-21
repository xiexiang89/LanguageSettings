package com.edgar.language;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by Edgar on 2018/12/21.
 */
class LocaleFactory {

    static Locale getLocale(String language, String country) {
        String localeName = TextUtils.isEmpty(country) ? language : language + "_"+ country;
        switch (localeName) {
            case LocaleProvider.ZH:
            case LocaleProvider.ZH_CN:
                return Locale.SIMPLIFIED_CHINESE;
            case LocaleProvider.ZH_HK:
            case LocaleProvider.ZH_TW:
                return Locale.TRADITIONAL_CHINESE;
            case LocaleProvider.EN:
                return Locale.ENGLISH;
            case LocaleProvider.JA:
                return Locale.JAPANESE;
            case LocaleProvider.KO:
                return Locale.KOREAN;
            default:
                return new Locale(language, country);
        }
    }
}