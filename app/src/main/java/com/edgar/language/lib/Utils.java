package com.edgar.language.lib;

import android.support.v4.util.ArrayMap;

import com.edgar.language.R;

import java.util.HashMap;
import java.util.Map;

import static com.edgar.language.lib.LocaleManager.*;

/**
 * Created by Edgar on 2019/4/13.
 */
class ResourceUtils {

    private ResourceUtils() {}

    private static final Map<String,Integer> LOCALE_NAME_ARRAY = new ArrayMap<String, Integer>() {
        {
            put(ZH_CN, R.string.chinese_simplified);
            put(ZH_HK, R.string.chinese_traditional);
            put(ZH_TW, R.string.chinese_traditional);
            put(EN, R.string.english);
            put(JA, R.string.japanese);
            put(KO, R.string.korean);
        }
    };

    static int getStringId(String name) {
        return LOCALE_NAME_ARRAY.get(name);
    }
}
