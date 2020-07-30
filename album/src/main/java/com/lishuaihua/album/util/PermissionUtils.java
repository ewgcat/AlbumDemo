package com.lishuaihua.album.util;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtils {

    public static String[] getDeniedPermissions(Context context, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(2);
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permission);
        return deniedList.toArray(new String[deniedList.size()]);
    }

    public static boolean isGrantedResult(int... permissionResult) {
        for (int result : permissionResult) {
            if (result == PackageManager.PERMISSION_DENIED) return false;
        }
        return true;
    }

}
