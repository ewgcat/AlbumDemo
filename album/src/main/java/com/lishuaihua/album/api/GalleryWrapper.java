
package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.ui.GalleryActivity;

public class GalleryWrapper extends BasicGalleryWrapper<GalleryWrapper, String, String, String> {

    public GalleryWrapper(Context context) {
        super(context);
    }

    @Override
    public void start() {
        GalleryActivity.sResult = mResult;
        GalleryActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, GalleryActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putStringArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);
        intent.putExtra(Album.KEY_INPUT_CURRENT_POSITION, mCurrentPosition);
        intent.putExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, mCheckable);
        intent.putExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, mNavigationAlpha);
        mContext.startActivity(intent);
    }
}
