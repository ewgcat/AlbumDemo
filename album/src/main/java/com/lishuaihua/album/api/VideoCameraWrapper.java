package com.lishuaihua.album.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.ui.CameraActivity;


public class VideoCameraWrapper extends BasicCameraWrapper<VideoCameraWrapper> {

    @IntRange(from = 0, to = 1)
    private int mQuality = 1;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitDuration = Long.MAX_VALUE;
    private long mLimitBytes = Long.MAX_VALUE;

    public VideoCameraWrapper(@NonNull Context context) {
        super(context);
    }

    /**
     * Currently value 0 means low quality, suitable for MMS messages, and  value 1 means high quality.
     */
    public VideoCameraWrapper quality(@IntRange(from = 0, to = 1) int quality) {
        this.mQuality = quality;
        return this;
    }

    /**
     * Specify the maximum allowed recording duration in seconds.
     */
    public VideoCameraWrapper limitDuration(@IntRange(from = 1, to = Long.MAX_VALUE) long duration) {
        this.mLimitDuration = duration;
        return this;
    }

    /**
     * Specify the maximum allowed size.
     */
    public VideoCameraWrapper limitBytes(@IntRange(from = 1, to = Long.MAX_VALUE) long bytes) {
        this.mLimitBytes = bytes;
        return this;
    }

    public void start() {
        CameraActivity.sResult = mResult;
        CameraActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CAMERA_VIDEO);
        intent.putExtra(Album.KEY_INPUT_FILE_PATH, mFilePath);
        intent.putExtra(Album.KEY_INPUT_CAMERA_QUALITY, mQuality);
        intent.putExtra(Album.KEY_INPUT_CAMERA_DURATION, mLimitDuration);
        intent.putExtra(Album.KEY_INPUT_CAMERA_BYTES, mLimitBytes);
        mContext.startActivity(intent);
    }
}
