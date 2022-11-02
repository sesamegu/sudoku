package com.sesame.game.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Introduction:
 *
 * @author sesame 2022/11/2
 */
public class I18nProcessor {

    private static ResourceBundle rbEn;
    private static ResourceBundle rbZh;
    private static boolean isChinese;

    static {
        Locale localeEn = new Locale("en", "US");
        Locale localeZh = new Locale("zh", "CN");
        rbEn = ResourceBundle.getBundle("i18n.content", localeEn);
        rbZh = ResourceBundle.getBundle("i18n.content", localeZh);
        String s = Locale.getDefault().toString();
        if ("zh_CN".equals(s) || "zh_TW".equals(s)) {
            isChinese = true;
        } else {
            isChinese = false;
        }

    }

    public static String getValue(String key) {
        //return rbEn.getString(key);
        if (isChinese) {
            return rbZh.getString(key);
        } else {
            return rbEn.getString(key);
        }
    }

}
