package com.sesame.game.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Introduction: I18N
 *
 * @author sesame 2022/11/2
 */
public class I18nProcessor {

    private static final String BUNDLE_NAME = "i18n.content";

    public static String getValue(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        return bundle.getString(key);
    }

    public static String getAppendValue(String key, Locale locale, Object... argu) {
        String pattern = getValue(key, locale);
        MessageFormat messageFormat = new MessageFormat(pattern, locale);
        return messageFormat.format(argu);
    }

    public static Locale getLocale(String lang) {
        if ("zh_CN".equalsIgnoreCase(lang)) {
            return Locale.CHINA;
        }
        return Locale.US;
    }
}
