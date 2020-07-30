package com.lishuaihua.album.util;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayUtils {

    private static boolean isInitialize = false;

    public static int sScreenWidth;
    public static int sScreenHeight;
    private static float mDensity;
    private static float mScaledDensity;

    public static void initScreen(AppCompatActivity activity) {
        if (isInitialize) return;
        isInitialize = true;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        if (VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }

        sScreenWidth = metrics.widthPixels;
        sScreenHeight = metrics.heightPixels;
        mDensity = metrics.density;
        mScaledDensity = metrics.scaledDensity;
    }

    public static int px2dip(float inParam) {
        return (int) (inParam / mDensity + 0.5F);
    }

    public static int dip2px(float inParam) {
        return (int) (inParam * mDensity + 0.5F);
    }

    public static int px2sp(float inParam) {
        return (int) (inParam / mScaledDensity + 0.5F);
    }

    public static int sp2px(float inParam) {
        return (int) (inParam * mScaledDensity + 0.5F);
    }
}