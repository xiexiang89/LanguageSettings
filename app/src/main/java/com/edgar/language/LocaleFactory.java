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
            case "zh":
            case "zh_CN":
                return Locale.SIMPLIFIED_CHINESE;
            case "zh_HK":
            case "zh_TW":
                return Locale.TRADITIONAL_CHINESE;
            case "en":
                return Locale.ENGLISH;
            case "ja":
                return Locale.JAPANESE;
            default:
                return new Locale(language, country);
        }
    }
}