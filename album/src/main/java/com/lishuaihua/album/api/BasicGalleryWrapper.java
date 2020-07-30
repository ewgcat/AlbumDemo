
package com.lishuaihua.album.api;

import android.content.Context;

import androidx.annotation.IntRange;

import java.util.ArrayList;

public abstract class BasicGalleryWrapper<T extends BasicGalleryWrapper, Result, Cancel, Checked> extends BasicAlbumWrapper<T, ArrayList<Result>, Cancel, ArrayList<Checked>> {

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    int mCurrentPosition = 0;
    boolean mCheckable = true;
    @IntRange(from = 0, to = 255)
    int mNavigationAlpha = 80;

    public BasicGalleryWrapper(Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     */
    public final T checkedList(ArrayList<Checked> checked) {
        this.mChecked = checked;
        return (T) this;
    }

    /**
     * Set the show position of List.
     */
    public T currentPosition(@IntRange(from = 1, to = Integer.MAX_VALUE) int currentPosition) {
        this.mCurrentPosition = currentPosition;
        return (T) this;
    }

    /**
     * The ability to select pictures.
     */
    public T checkable(boolean checkable) {
        this.mCheckable = checkable;
        return (T) this;
    }

    /**
     * Set alpha of NavigationBar.
     */
    public T navigationAlpha(@IntRange(from = 0, to = 255) int alpha) {
        this.mNavigationAlpha = alpha;
        return (T) this;
    }
}
