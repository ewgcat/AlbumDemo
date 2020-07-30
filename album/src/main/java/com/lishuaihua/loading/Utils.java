package com.lishuaihua.loading;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

    public static float dip2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) ((displayMetrics.density + 0.5) * dp);
    }

    public static float px2dip(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) (px / (displayMetrics.density + 0.5));
    }

}
