package com.lishuaihua.album.api.choice;

import android.content.Context;

import com.lishuaihua.album.api.ImageMultipleWrapper;
import com.lishuaihua.album.api.ImageSingleWrapper;


public final class ImageChoice implements Choice<ImageMultipleWrapper, ImageSingleWrapper> {

    private Context mContext;

    public ImageChoice(Context context) {
        mContext = context;
    }

    @Override
    public ImageMultipleWrapper multipleChoice() {
        return new ImageMultipleWrapper(mContext);
    }

    @Override
    public ImageSingleWrapper singleChoice() {
        return new ImageSingleWrapper(mContext);
    }

}