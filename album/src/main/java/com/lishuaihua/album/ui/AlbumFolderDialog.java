package com.lishuaihua.album.ui;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.album.ui.adapter.AlbumFolderAdapter;
import com.lishuaihua.album.R;
import com.lishuaihua.album.AlbumFolder;
import com.lishuaihua.album.impl.OnItemClickListener;

import java.util.List;


public class AlbumFolderDialog extends BottomSheetDialog {

    private int mCurrentPosition = 0;
    private BottomSheetBehavior bottomSheetBehavior;
    private OnItemClickListener mItemClickListener;

    public AlbumFolderDialog(@NonNull Context context,
                             Widget widget,
                             @Nullable List<AlbumFolder> albumFolders,
                             @Nullable OnItemClickListener itemClickListener) {
        super(context, R.style.DialogStyle_Folder);
        setContentView(R.layout.dialog_floder);

        setWindowBarColor(widget.getStatusBarColor(), widget.getNavigationBarColor());
        fixRestart();

        RecyclerView recyclerView = (RecyclerView) getDelegate().findViewById(R.id.rv_content_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AlbumFolderAdapter folderAdapter = new AlbumFolderAdapter(context, albumFolders, widget.getBucketItemCheckSelector());
        folderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                behaviorHide();
                if (mItemClickListener != null && mCurrentPosition != position) {
                    mCurrentPosition = position;
                    mItemClickListener.onItemClick(view, position);
                }
            }
        });
        recyclerView.setAdapter(folderAdapter);

        mItemClickListener = itemClickListener;
    }

    /**
     * Set window bar color.
     *
     * @param statusColor     status bar color.
     * @param navigationColor navigation bar color.
     */
    private void setWindowBarColor(@ColorInt int statusColor, @ColorInt int navigationColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusColor);
                window.setNavigationBarColor(navigationColor);
            }
        }
    }

    /**
     * Fix reshow.
     */
    private void fixRestart() {
        View view = findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (view == null) return;
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    /**
     * Dismiss dialog.
     */
    public void behaviorHide() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
