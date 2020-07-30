package com.lishuaihua.album.task;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.Filter;

import java.io.File;

public class PathConversion {

    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;

    public PathConversion(Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
    }

    @WorkerThread
    @NonNull
    public AlbumFile convert(String filePath) {
        File file = new File(filePath);
        String name = file.getName();

        AlbumFile albumFile = new AlbumFile();
        albumFile.setPath(filePath);
        albumFile.setName(name);
        String title = name;
        if (name.contains("."))
            title = name.split("\\.")[0];
        albumFile.setTitle(title);

        File parentFile = file.getParentFile();
        albumFile.setBucketName(parentFile.getName());

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePath));
        albumFile.setMimeType(mimeType);
        long nowTime = System.currentTimeMillis();
        albumFile.setAddDate(nowTime);
        albumFile.setModifyDate(nowTime);
        albumFile.setSize(file.length());
        int mediaType = AlbumFile.TYPE_INVALID;
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.contains("video"))
                mediaType = AlbumFile.TYPE_VIDEO;
            if (mimeType.contains("image"))
                mediaType = AlbumFile.TYPE_IMAGE;
        }
        albumFile.setMediaType(mediaType);

        // Filter.
        if (mSizeFilter != null && mSizeFilter.filter(file.length())) {
            albumFile.setEnable(false);
        }
        if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
            albumFile.setEnable(false);
        }

        if (mediaType == AlbumFile.TYPE_VIDEO) {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(filePath);
                player.prepare();
                albumFile.setDuration(player.getDuration());
                albumFile.setWidth(player.getVideoWidth());
                albumFile.setHeight(player.getVideoHeight());
            } catch (Exception ignored) {
            } finally {
                player.release();
            }

            if (mDurationFilter != null && mDurationFilter.filter(albumFile.getDuration())) {
                albumFile.setEnable(false);
            }
        }
        return albumFile;
    }

}
