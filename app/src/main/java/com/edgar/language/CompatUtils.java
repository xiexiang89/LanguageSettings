package com.edgar.language;

import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Edgar on 2019/4/12.
 */
public class CompatUtils {

    public static Locale getLocale(Configuration configuration) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return configuration.getLocales().get(0);
        } else {
            return configuration.locale;
        }
    }
}
