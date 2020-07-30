
package com.lishuaihua.album.api;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.lishuaihua.album.Filter;


public abstract class BasicChoiceWrapper<T extends BasicChoiceWrapper, Result, Cancel, Checked> extends BasicAlbumWrapper<T, Result, Cancel, Checked> {

    boolean mHasCamera = true;
    @IntRange(from = 1, to = 4)
    int mColumnCount = 2;

    Filter<Long> mSizeFilter;
    Filter<String> mMimeTypeFilter;

    boolean mFilterVisibility = true;

    BasicChoiceWrapper(@NonNull Context context) {
        super(context);
    }

    /**
     * Turn on the camera function.
     */
    public T camera(boolean hasCamera) {
        this.mHasCamera = hasCamera;
        return (T) this;
    }

    /**
     * Sets the number of columns for the page.
     */
    public T columnCount(@IntRange(from = 1, to = 4) int count) {
        this.mColumnCount = count;
        return (T) this;
    }

    /**
     * Filter the file size.
     */
    public T filterSize(Filter<Long> filter) {
        this.mSizeFilter = filter;
        return (T) this;
    }

    /**
     * Filter the file extension.
     */
    public T filterMimeType(Filter<String> filter) {
        this.mMimeTypeFilter = filter;
        return (T) this;
    }

    /**
     * The visibility of the filtered file.
     */
    public T afterFilterVisibility(boolean visibility) {
        this.mFilterVisibility = visibility;
        return (T) this;
    }

}
