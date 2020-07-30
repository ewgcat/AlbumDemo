package com.lishuaihua.durban.callback;


import androidx.annotation.NonNull;

public interface BitmapCropCallback {

    void onBitmapCropped(@NonNull String imagePath, int imageWidth, int imageHeight);

    void onCropFailure(@NonNull Throwable t);
}