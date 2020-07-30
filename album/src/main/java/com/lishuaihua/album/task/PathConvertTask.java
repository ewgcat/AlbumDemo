package com.lishuaihua.album.task;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.Filter;
import com.lishuaihua.loading.dialog.LoadingDialog;


public class PathConvertTask extends AsyncTask<String, Void, AlbumFile> {

    public interface Callback {
        void onConvertCallback(AlbumFile albumFile);
    }

    private Dialog mDialog;
    private Callback mCallback;

    private PathConversion mConversion;

    public PathConvertTask(Context context, Callback callback,
                           Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mDialog = new LoadingDialog(context);
        this.mCallback = callback;

        this.mConversion = new PathConversion(sizeFilter, mimeFilter, durationFilter);
    }

    @Override
    protected void onPreExecute() {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    @Override
    protected AlbumFile doInBackground(String... params) {
        return mConversion.convert(params[0]);

    }

    @Override
    protected void onPostExecute(AlbumFile file) {
        if (mDialog.isShowing())
            mDialog.dismiss();
        mCallback.onConvertCallback(file);
    }
}
