package com.lishuaihua.album.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.album.ui.adapter.AlbumFilePreviewAdapter;
import com.lishuaihua.album.ui.adapter.BasicPreviewAdapter;
import com.lishuaihua.album.Action;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.R;
import com.lishuaihua.album.util.AlbumUtils;
import com.lishuaihua.album.util.PermissionUtils;
import com.lishuaihua.statusview.StatusUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class GalleryAlbumActivity extends AppCompatActivity {

    private static final String TAG = "AlbumGallery";
    private static final int PERMISSION_STORAGE = 1;

    public static Action<ArrayList<AlbumFile>> sResult;
    public static Action<String> sCancel;

    private Toolbar mToolbar;
    private MenuItem mFinishMenuItem;

    private ViewPager mViewPager;
    private TextView mTvDuration;
    private AppCompatCheckBox mCheckBox;

    private int mRequestCode;
    @NonNull
    private Widget mWidget;
    private ArrayList<AlbumFile> mAlbumFiles;
    private Map<AlbumFile, Boolean> mCheckedMap;
    private int mCurrentItemPosition;
    private boolean mCheckable;
    private int mNavigationAlpha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setFullToStatusBar(this);
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);
        setContentView(R.layout.activity_preview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_check);

        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);
        mWidget = intent.getParcelableExtra(Album.KEY_INPUT_WIDGET);
        mAlbumFiles = intent.getParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST);
        mCurrentItemPosition = intent.getIntExtra(Album.KEY_INPUT_CURRENT_POSITION, 0);
        mCheckable = intent.getBooleanExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, true);
        mNavigationAlpha = intent.getIntExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, 80);

        if (mAlbumFiles == null) {
            Log.e(TAG, "Parameter error.",
                    new IllegalArgumentException("The checkedList can be null."));
            onGalleryCancel();
        } else if (mAlbumFiles.size() == 0 || mCurrentItemPosition == mAlbumFiles.size()) {
            Log.e(TAG, "Parameter error.",
                    new IllegalArgumentException("The currentPosition is " + mCurrentItemPosition + ","
                            + " the checkedList.size() is " + mAlbumFiles.size()));
            onGalleryCancel();
        } else {
            mCheckedMap = new HashMap<>();
            for (AlbumFile albumFile : mAlbumFiles) {
                mCheckedMap.put(albumFile, true);
            }
            initializeWidget();
            initializePager();

            requestPermission(PERMISSION_STORAGE);
        }
    }

    /**
     * Initialize widget.
     */
    @SuppressLint("RestrictedApi")
    private void initializeWidget() {
        int navigationColor = mWidget.getNavigationBarColor();
        navigationColor = AlbumUtils.getAlphaColor(navigationColor, mNavigationAlpha);
        StatusUtils.setFullToNavigationBar(this);
        StatusUtils.setNavigationBarColor(this, navigationColor);

        setTitle(mWidget.getTitle());

        if (!mCheckable) {
            findViewById(R.id.bottom_root).setVisibility(View.GONE);
        } else {
            ColorStateList itemSelector = mWidget.getMediaItemCheckSelector();
            mCheckBox.setSupportButtonTintList(itemSelector);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = mCheckBox.isChecked();
                    mCheckedMap.put(mAlbumFiles.get(mCurrentItemPosition), isChecked);
                    setCheckedCountUI(getCheckCount());
                }
            });
        }
    }

    /**
     * Initialize ViewPager.
     */
    private void initializePager() {
        if (mAlbumFiles != null) {
            if (mAlbumFiles.size() > 3)
                mViewPager.setOffscreenPageLimit(3);
            else if (mAlbumFiles.size() > 2)
                mViewPager.setOffscreenPageLimit(2);
        }
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    /**
     * Listener of ViewPager changed.
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mCurrentItemPosition = position;
            AlbumFile albumFile = mAlbumFiles.get(mCurrentItemPosition);
            mCheckBox.setChecked(mCheckedMap.get(albumFile));
            mToolbar.setSubtitle(mCurrentItemPosition + 1 + " / " + mAlbumFiles.size());

            if (albumFile.getMediaType() == AlbumFile.TYPE_VIDEO) {
                mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
                mTvDuration.setVisibility(View.VISIBLE);
            } else {
                mTvDuration.setVisibility(View.GONE);
            }
        }
    };

    /**
     * Get check item count.
     */
    private int getCheckCount() {
        int checkedCount = 0;
        for (Map.Entry<AlbumFile, Boolean> entry : mCheckedMap.entrySet()) {
            if (entry.getValue()) checkedCount += 1;
        }
        return checkedCount;
    }

    /**
     * Set the number of selected pictures.
     */
    private void setCheckedCountUI(int count) {
        String finishStr = getString(R.string.menu_finish);
        finishStr += "(" + count + " / " + mAlbumFiles.size() + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        mFinishMenuItem = menu.findItem(R.id.menu_finish);
        if (!mCheckable)
            mFinishMenuItem.setVisible(false);
        else
            setCheckedCountUI(getCheckCount());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_finish) {
            onGalleryResult();
        } else if (id == android.R.id.home) {
            onGalleryCancel();
        }
        return true;
    }

    /**
     * Scan, but unknown permissions.
     *
     * @param requestCode request code.
     */
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            String[] deniedPermissions = PermissionUtils.getDeniedPermissions(this, permission);

            if (deniedPermissions.length == 0) {
                dispatchGrantedPermission(requestCode);
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        deniedPermissions,
                        requestCode);
            }
        } else {
            dispatchGrantedPermission(requestCode);
        }
    }

    /**
     * Dispatch granted permission.
     */
    private void dispatchGrantedPermission(int requestCode) {
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                BasicPreviewAdapter previewAdapter = new AlbumFilePreviewAdapter(this, mAlbumFiles);
                mViewPager.setAdapter(previewAdapter);
                mViewPager.setCurrentItem(mCurrentItemPosition);
                mPageChangeListener.onPageSelected(mCurrentItemPosition);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                if (PermissionUtils.isGrantedResult(grantResults)) dispatchGrantedPermission(requestCode);
                else albumPermissionDenied();
                break;
            }
        }
    }

    /**
     * The permission for Album is denied.
     */
    private void albumPermissionDenied() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.title_permission_failed)
                .setMessage(R.string.permission_storage_failed_hint)
                .setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onGalleryCancel();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        onGalleryCancel();
    }

    private void onGalleryResult() {
        if (sResult != null) {
            ArrayList<AlbumFile> checkedList = new ArrayList<>();
            for (Map.Entry<AlbumFile, Boolean> entry : mCheckedMap.entrySet()) {
                if (entry.getValue()) checkedList.add(entry.getKey());
            }
            sResult.onAction(mRequestCode, checkedList);
        }
        setResult(RESULT_OK);
        finish();
    }

    private void onGalleryCancel() {
        if (sCancel != null)
            sCancel.onAction(mRequestCode, "User canceled.");
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        sResult = null;
        sCancel = null;
        super.onDestroy();
    }
}