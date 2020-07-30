package com.lishuaihua.album;


import androidx.annotation.NonNull;


public interface Action<T> {

    /**
     * When the action responds.
     *
     * @param requestCode requestCode.
     * @param result      The result of the action.
     */
    void onAction(int requestCode, @NonNull T result);

}
