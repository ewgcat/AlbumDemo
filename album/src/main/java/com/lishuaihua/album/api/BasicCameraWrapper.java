
package com.lishuaihua.album.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lishuaihua.album.Action;


public abstract class BasicCameraWrapper<T extends BasicCameraWrapper> {

    @NonNull
    final Context mContext;
    Action<String> mResult;
    Action<String> mCancel;
    int mRequestCode;
    @Nullable
    String mFilePath;

    public BasicCameraWrapper(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Set the action when result.
     */
    public final T onResult(Action<String> result) {
        this.mResult = result;
        return (T) this;
    }

    /**
     * Set the action when canceling.
     */
    public final T onCancel(Action<String> cancel) {
        this.mCancel = cancel;
        return (T) this;
    }

    /**
     * Request tag.
     */
    public T requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return (T) this;
    }

    /**
     * Set the image storage path.
     */
    public T filePath(@Nullable String filePath) {
        this.mFilePath = filePath;
        return (T) this;
    }

    /**
     * Start up.
     */
    public abstract void start();

}
