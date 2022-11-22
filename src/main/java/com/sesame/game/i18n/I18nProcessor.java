package com.sesame.game.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Getter;
import lombok.Setter;

/**
 * Introduction: I18N
 *
 * @author sesame 2022/11/2
 */
public class I18nProcessor {

    private static final ResourceBundle rbEn;
    private static final ResourceBundle rbZh;
    @Getter
    @Setter
    private static boolean isChinese;

    static {
        Locale localeEn = new Locale("en", "US");
        Locale localeZh = new Locale("zh", "CN");
        rbEn = ResourceBundle.getBundle("i18n.content", localeEn);
        rbZh = ResourceBundle.getBundle("i18n.content", localeZh);
        String s = Locale.getDefault().toString();
        isChinese = "zh_CN".equals(s) || "zh_TW".equals(s);

    }

    public static String getValue(String key) {
        if (isChinese) {
            return rbZh.getString(key);
        } else {
            return rbEn.getString(key);
        }
    }

    public static String getAppendValue(String key, Object... argu) {
        MessageFormat messageFormat = new MessageFormat(getValue(key));
        return messageFormat.format(argu);
    }

}
