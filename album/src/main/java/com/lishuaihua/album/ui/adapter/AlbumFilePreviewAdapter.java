package com.lishuaihua.album.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.util.DisplayUtils;

import java.util.List;


public class AlbumFilePreviewAdapter extends BasicPreviewAdapter<AlbumFile> {

    public AlbumFilePreviewAdapter(Context context, List<AlbumFile> previewList) {
        super(context, previewList);
    }

    @Override
    protected boolean loadPreview(ImageView imageView, AlbumFile albumFile, int position) {
        Album.getAlbumConfig()
                .getAlbumLoader()
                .loadAlbumFile(imageView, albumFile, DisplayUtils.sScreenWidth, DisplayUtils.sScreenHeight);
        return true;
    }
}
