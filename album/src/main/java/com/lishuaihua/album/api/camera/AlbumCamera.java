
package com.lishuaihua.album.api.camera;

import android.content.Context;

import com.lishuaihua.album.api.ImageCameraWrapper;
import com.lishuaihua.album.api.VideoCameraWrapper;


public class AlbumCamera implements Camera<ImageCameraWrapper, VideoCameraWrapper> {

    private Context mContext;

    public AlbumCamera(Context context) {
        mContext = context;
    }

    @Override
    public ImageCameraWrapper image() {
        return new ImageCameraWrapper(mContext);
    }

    @Override
    public VideoCameraWrapper video() {
        return new VideoCameraWrapper(mContext);
    }

}
