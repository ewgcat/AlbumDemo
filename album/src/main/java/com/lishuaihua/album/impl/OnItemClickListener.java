package com.lishuaihua.album.impl;

import android.view.View;

public interface OnItemClickListener {

    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    void onItemClick(View view, int position);
}
