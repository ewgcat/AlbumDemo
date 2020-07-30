package com.lishuaihua.durban.callback;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.lishuaihua.durban.model.ExifInfo;


public interface BitmapLoadCallback {

    void onSuccessfully(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo);

    void onFailure();

}