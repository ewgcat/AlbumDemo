
package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.ui.AlbumActivity;

import java.util.ArrayList;


public final class ImageSingleWrapper extends BasicChoiceWrapper<ImageSingleWrapper, ArrayList<AlbumFile>, String, AlbumFile> {

    public ImageSingleWrapper(@NonNull Context context) {
        super(context);
    }

    @Override
    public void start() {
        AlbumActivity.mSizeFilter = mSizeFilter;
        AlbumActivity.mMimeFilter = mMimeTypeFilter;
        AlbumActivity.sResult = mResult;
        AlbumActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, AlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_IMAGE);
        intent.putExtra(Album.KEY_INPUT_CHOICE_MODE, Album.MODE_SINGLE);
        intent.putExtra(Album.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Album.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Album.KEY_INPUT_LIMIT_COUNT, 1);
        intent.putExtra(Album.KEY_INPUT_FILTER_VISIBILITY, mFilterVisibility);
        mContext.startActivity(intent);
    }
}