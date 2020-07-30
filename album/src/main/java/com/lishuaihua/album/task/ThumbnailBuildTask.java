
package com.lishuaihua.album.task;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.loading.dialog.LoadingDialog;

import java.util.ArrayList;

public class ThumbnailBuildTask extends AsyncTask<Void, Void, ArrayList<AlbumFile>> {

    private ArrayList<AlbumFile> mAlbumFiles;
    private Callback mCallback;

    private Dialog mDialog;
    private ThumbnailBuilder mThumbnailBuilder;

    public ThumbnailBuildTask(Context context, ArrayList<AlbumFile> albumFiles, Callback callback) {
        this.mAlbumFiles = albumFiles;
        this.mCallback = callback;

        this.mDialog = new LoadingDialog(context);
        this.mThumbnailBuilder = new ThumbnailBuilder(context);
    }

    @Override
    protected void onPreExecute() {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    @Override
    protected ArrayList<AlbumFile> doInBackground(Void... params) {
        for (AlbumFile albumFile : mAlbumFiles) {
            @AlbumFile.MediaType int mediaType = albumFile.getMediaType();
            String thumbnail = null;
            if (mediaType == AlbumFile.TYPE_IMAGE) {
                thumbnail = mThumbnailBuilder.createThumbnailForImage(albumFile.getPath());
            } else if (mediaType == AlbumFile.TYPE_VIDEO) {
                thumbnail = mThumbnailBuilder.createThumbnailForVideo(albumFile.getPath());
            }
            albumFile.setThumbPath(thumbnail);
        }
        return mAlbumFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<AlbumFile> albumFiles) {
        if(mDialog.isShowing())
            mDialog.dismiss();
        mCallback.onThumbnailCallback(albumFiles);
    }

    public interface Callback {
        void onThumbnailCallback(ArrayList<AlbumFile> albumFiles);
    }
}
