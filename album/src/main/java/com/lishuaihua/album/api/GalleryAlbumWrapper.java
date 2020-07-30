package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.ui.GalleryAlbumActivity;


public class GalleryAlbumWrapper extends BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> {

    public GalleryAlbumWrapper(Context context) {
        super(context);
    }

    @Override
    public void start() {
        GalleryAlbumActivity.sResult = mResult;
        GalleryAlbumActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, GalleryAlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);
        intent.putExtra(Album.KEY_INPUT_CURRENT_POSITION, mCurrentPosition);
        intent.putExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, mCheckable);
        intent.putExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, mNavigationAlpha);
        mContext.startActivity(intent);
    }
}
