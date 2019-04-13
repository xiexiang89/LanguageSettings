package com.edgar.language.lib;

import android.support.annotation.StringRes;

import java.util.Locale;

/**
 * Created by Edgar on 2019/4/13.
 */
public class LocaleInfo {

    public final String localeId; //language_country
    public final @StringRes int name;
    public final Locale locale;
    public final boolean isFollowSystem;

    LocaleInfo(String localeId, @StringRes int name, Locale locale,boolean isFollowSystem) {
        this.localeId = localeId;
        this.name = name;
        this.locale = locale;
        this.isFollowSystem = isFollowSystem;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public String getCountry() {
        return locale.getCountry();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocaleInfo && localeId.equals(((LocaleInfo) obj).localeId);
    }
}
