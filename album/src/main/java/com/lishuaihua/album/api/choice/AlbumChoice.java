
package com.lishuaihua.album.api.choice;

import android.content.Context;

import com.lishuaihua.album.api.AlbumMultipleWrapper;
import com.lishuaihua.album.api.AlbumSingleWrapper;


public final class AlbumChoice implements Choice<AlbumMultipleWrapper, AlbumSingleWrapper> {

    private Context mContext;

    public AlbumChoice(Context context) {
        mContext = context;
    }

    @Override
    public AlbumMultipleWrapper multipleChoice() {
        return new AlbumMultipleWrapper(mContext);
    }

    @Override
    public AlbumSingleWrapper singleChoice() {
        return new AlbumSingleWrapper(mContext);
    }
}