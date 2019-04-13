package com.edgar.language.lib;

import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/**
 * Created by Edgar on 2019/4/12.
 */
class LocaleCompatUtils {

    static Locale getLocale(Configuration configuration) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return configuration.getLocales().get(0);
        } else {
            return configuration.locale;
        }
    }
}
