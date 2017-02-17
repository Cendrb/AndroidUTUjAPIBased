package com.farast.utu_apibased.util;

/**
 * Created by cendr on 13/02/2017.
 */

public class TextUtil {
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.equals("");
    }
}
