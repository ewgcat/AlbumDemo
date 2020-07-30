
package com.lishuaihua.album.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.album.Action;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.Filter;
import com.lishuaihua.album.R;
import com.lishuaihua.album.impl.AlbumCallback;
import com.lishuaihua.album.task.ThumbnailBuildTask;
import com.lishuaihua.album.util.AlbumUtils;
import com.lishuaihua.album.util.DisplayUtils;
import com.lishuaihua.album.util.PermissionUtils;
import com.lishuaihua.fragment.CompatActivity;
import com.lishuaihua.statusview.StatusUtils;

import java.util.ArrayList;
import java.util.Locale;


public class AlbumActivity extends CompatActivity implements AlbumCallback {

    private static final int PERMISSION_STORAGE_ALBUM = 1;
    private static final int PERMISSION_STORAGE_IMAGE = 2;
    private static final int PERMISSION_STORAGE_VIDEO = 3;

    public static Filter<Long> mSizeFilter;
    public static Filter<String> mMimeFilter;
    public static Filter<Long> mDurationFilter;

    public static Action<ArrayList<AlbumFile>> sResult;
    public static Action<String> sCancel;

    private Bundle mArgument;
    private int mRequestCode;

    private boolean isSucceedLightStatus = false;

    @Override
    protected int fragmentLayoutId() {
        return R.id.root_frame_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setFullToStatusBar(this);
        StatusUtils.setFullToNavigationBar(this);
        DisplayUtils.initScreen(this);
        // Language.
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);

        setContentView(R.layout.activity_album);

        Intent intent = getIntent();
        initializeWidget();

        mArgument = intent.getExtras();
        mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);

        final int function = intent.getIntExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_ALBUM);
        switch (function) {
            case Album.FUNCTION_CHOICE_ALBUM:
            case Album.FUNCTION_CHOICE_IMAGE:
            case Album.FUNCTION_CHOICE_VIDEO: {
                requestPermission(PERMISSION_STORAGE_VIDEO);
                break;
            }
            default: {
                onAlbumCancel();
            }
        }
    }

    /**
     * Initialize widget.
     */
    private void initializeWidget() {
        Widget widget = getIntent().getParcelableExtra(Album.KEY_INPUT_WIDGET);
        int navigationColor = widget.getNavigationBarColor();

        if (widget.getStyle() == Widget.STYLE_LIGHT) {
            if (StatusUtils.setStatusBarDarkFont(this, true)) {
                isSucceedLightStatus = true;
            }
        }

        StatusUtils.setNavigationBarColor(this, navigationColor);
    }

    /**
     * Successfully set the status bar to light background.
     */
    public boolean isSucceedLightStatus() {
        return isSucceedLightStatus;
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
            case PERMISSION_STORAGE_ALBUM:
            case PERMISSION_STORAGE_IMAGE:
            case PERMISSION_STORAGE_VIDEO: {
                AlbumFragment albumFragment = fragment(AlbumFragment.class, mArgument);
                albumFragment.setSizeFilter(mSizeFilter);
                albumFragment.setMimeFilter(mMimeFilter);
                albumFragment.setDurationFilter(mDurationFilter);
                startFragment(albumFragment);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_ALBUM:
            case PERMISSION_STORAGE_IMAGE:
            case PERMISSION_STORAGE_VIDEO: {
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
                        onAlbumCancel();
                    }
                })
                .show();
    }

    public void onBackPressed() {
        if (!this.onBackStackFragment()) {
            onAlbumCancel();
        }
    }

    @Override
    public void onAlbumResult(ArrayList<AlbumFile> albumFiles) {
        new ThumbnailBuildTask(this, albumFiles, new ThumbnailBuildTask.Callback() {
            @Override
            public void onThumbnailCallback(ArrayList<AlbumFile> albumFiles) {
                if (sResult != null)
                    sResult.onAction(mRequestCode, albumFiles);
                setResult(RESULT_OK);
                finish();
            }
        }).execute();
    }

    @Override
    public void onAlbumCancel() {
        if (sCancel != null)
            sCancel.onAction(mRequestCode, "User canceled.");
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        mSizeFilter = null;
        mMimeFilter = null;
        mDurationFilter = null;
        sResult = null;
        sCancel = null;
        super.onDestroy();
    }
}