package com.farast.utu_apibased;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class UnitsUtil {
    public static int dpToPx(int dp)
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
