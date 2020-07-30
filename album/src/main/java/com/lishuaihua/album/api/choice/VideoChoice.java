
package com.lishuaihua.album.api.choice;

import android.content.Context;

import com.lishuaihua.album.api.VideoMultipleWrapper;
import com.lishuaihua.album.api.VideoSingleWrapper;


public final class VideoChoice implements Choice<VideoMultipleWrapper, VideoSingleWrapper> {

    private Context mContext;

    public VideoChoice(Context context) {
        mContext = context;
    }

    @Override
    public VideoMultipleWrapper multipleChoice() {
        return new VideoMultipleWrapper(mContext);
    }

    @Override
    public VideoSingleWrapper singleChoice() {
        return new VideoSingleWrapper(mContext);
    }

}
