
package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.Filter;
import com.lishuaihua.album.ui.AlbumActivity;

import java.util.ArrayList;


public class AlbumMultipleWrapper extends BasicChoiceWrapper<AlbumMultipleWrapper, ArrayList<AlbumFile>, String, ArrayList<AlbumFile>> {

    @IntRange(from = 1, to = Integer.MAX_VALUE)
    private int mLimitCount = Integer.MAX_VALUE;

    private Filter<Long> mDurationFilter;

    public AlbumMultipleWrapper(@NonNull Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     */
    public final AlbumMultipleWrapper checkedList(ArrayList<AlbumFile> checked) {
        this.mChecked = checked;
        return this;
    }

    /**
     * Set the maximum number to be selected.
     */
    public AlbumMultipleWrapper selectCount(@IntRange(from = 1, to = Integer.MAX_VALUE) int count) {
        this.mLimitCount = count;
        return this;
    }

    /**
     * Filter video duration.
     */
    public AlbumMultipleWrapper filterDuration(Filter<Long> filter) {
        this.mDurationFilter = filter;
        return this;
    }

    @Override
    public void start() {
        AlbumActivity.mSizeFilter = mSizeFilter;
        AlbumActivity.mMimeFilter = mMimeTypeFilter;
        AlbumActivity.mDurationFilter = mDurationFilter;
        AlbumActivity.sResult = mResult;
        AlbumActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, AlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_ALBUM);
        intent.putExtra(Album.KEY_INPUT_CHOICE_MODE, Album.MODE_MULTIPLE);
        intent.putExtra(Album.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Album.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Album.KEY_INPUT_LIMIT_COUNT, mLimitCount);
        intent.putExtra(Album.KEY_INPUT_FILTER_VISIBILITY, mFilterVisibility);
        mContext.startActivity(intent);
    }
}