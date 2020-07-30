package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.ui.CameraActivity;

public class ImageCameraWrapper extends BasicCameraWrapper<ImageCameraWrapper> {

    public ImageCameraWrapper(@NonNull Context context) {
        super(context);
    }

    public void start() {
        CameraActivity.sResult = mResult;
        CameraActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CAMERA_IMAGE);
        intent.putExtra(Album.KEY_INPUT_FILE_PATH, mFilePath);
        mContext.startActivity(intent);
    }
}
