package com.lishuaihua.album.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.util.DisplayUtils;

import java.util.List;

public class PathPreviewAdapter extends BasicPreviewAdapter<String> {

    public PathPreviewAdapter(Context context, List<String> previewList) {
        super(context, previewList);
    }

    @Override
    protected boolean loadPreview(ImageView imageView, String s, int position) {
        Album.getAlbumConfig()
                .getAlbumLoader()
                .loadImage(imageView, s, DisplayUtils.sScreenWidth, DisplayUtils.sScreenHeight);
        return true;
    }
}
