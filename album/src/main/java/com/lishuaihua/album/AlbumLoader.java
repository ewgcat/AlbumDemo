package com.lishuaihua.album;

import android.widget.ImageView;


public interface AlbumLoader {

    /**
     * Load a preview of the album file.
     *
     * @param imageView  {@link ImageView}.
     * @param albumFile  the media object may be a picture or video.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadAlbumFile(ImageView imageView, AlbumFile albumFile, int viewWidth, int viewHeight);

    /**
     * Load a preview of the picture.
     *
     * @param imageView  {@link ImageView}.
     * @param imagePath  file path, which may be a local path or a network path.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadImage(ImageView imageView, String imagePath, int viewWidth, int viewHeight);

}
