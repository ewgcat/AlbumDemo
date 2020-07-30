package com.lishuaihua.album.provider;

import android.content.Context;

import androidx.core.content.FileProvider;

public class CameraFileProvider extends FileProvider {

    /**
     * Get the provider of the external file path.
     *
     * @param context context.
     * @return provider.
     */
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".album.camera.provider";
    }

}
