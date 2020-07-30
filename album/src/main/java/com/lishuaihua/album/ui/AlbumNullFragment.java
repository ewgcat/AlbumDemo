package com.lishuaihua.album.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.album.Action;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.R;
import com.lishuaihua.album.util.AlbumUtils;
import com.lishuaihua.fragment.NoFragment;
import com.lishuaihua.statusview.StatusView;


public class AlbumNullFragment extends NoFragment {

    private static final String KEY_OUTPUT_IMAGE_PATH = "KEY_OUTPUT_IMAGE_PATH";

    /**
     * Resolve the image path at the time of success.
     *
     * @param bundle {@link #onFragmentResult(int, int, Bundle)}.
     * @return image path.
     */
    public static String parseImagePath(Bundle bundle) {
        return bundle.getString(KEY_OUTPUT_IMAGE_PATH);
    }

    private StatusView mStatusView;
    private Toolbar mToolbar;

    private TextView mTvMessage;
    private AppCompatButton mBtnCameraImage;
    private AppCompatButton mBtnCameraVideo;

    private Widget mWidget;
    @Album.ChoiceFunction
    private int mFunction;
    private boolean mHasCamera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_null, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mStatusView = (StatusView) view.findViewById(R.id.status_view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setToolbar(mToolbar);

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mBtnCameraImage = (AppCompatButton) view.findViewById(R.id.btn_camera_image);
        mBtnCameraVideo = (AppCompatButton) view.findViewById(R.id.btn_camera_video);

        mBtnCameraImage.setOnClickListener(mCameraClickListener);
        mBtnCameraVideo.setOnClickListener(mCameraClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argument = getArguments();
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        //noinspection WrongConstant
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        mHasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA);

        initializeWidget();
    }

    private void initializeWidget() {
        int statusBarColor = mWidget.getStatusBarColor();
        Drawable navigationIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_back_white);
        if (mWidget.getStyle() == Widget.STYLE_LIGHT) {
            if (((AlbumActivity) getActivity()).isSucceedLightStatus()) {
                mStatusView.setBackgroundColor(statusBarColor);
            } else {
                mStatusView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ColorPrimaryBlack));
            }

            mToolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.FontDark));
            mToolbar.setSubtitleTextColor(ContextCompat.getColor(getContext(), R.color.FontDark));

            AlbumUtils.setDrawableTint(navigationIcon, ContextCompat.getColor(getContext(), R.color.IconDark));
            displayHomeAsUpEnabled(navigationIcon);
        } else {
            mStatusView.setBackgroundColor(statusBarColor);
            displayHomeAsUpEnabled(navigationIcon);
        }

        int toolbarColor = mWidget.getToolBarColor();
        mToolbar.setBackgroundColor(toolbarColor);
        mToolbar.setTitle(mWidget.getTitle());

        switch (mFunction) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                mTvMessage.setText(R.string.not_found_image);
                mBtnCameraVideo.setVisibility(View.GONE);
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                mTvMessage.setText(R.string.not_found_video);
                mBtnCameraImage.setVisibility(View.GONE);
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM:
            default: {
                mTvMessage.setText(R.string.not_found_album);
                break;
            }
        }

        if (mHasCamera) {
            Widget.ButtonStyle buttonStyle = mWidget.getButtonStyle();
            ColorStateList buttonSelector = buttonStyle.getButtonSelector();
            mBtnCameraImage.setSupportBackgroundTintList(buttonSelector);
            mBtnCameraVideo.setSupportBackgroundTintList(buttonSelector);
            if (buttonStyle.getButtonStyle() == Widget.STYLE_LIGHT) {
                Drawable drawable = mBtnCameraImage.getCompoundDrawables()[0];
                AlbumUtils.setDrawableTint(drawable, ContextCompat.getColor(getContext(), R.color.IconDark));
                mBtnCameraImage.setCompoundDrawables(drawable, null, null, null);

                drawable = mBtnCameraVideo.getCompoundDrawables()[0];
                AlbumUtils.setDrawableTint(drawable, ContextCompat.getColor(getContext(), R.color.IconDark));
                mBtnCameraVideo.setCompoundDrawables(drawable, null, null, null);

                mBtnCameraImage.setTextColor(ContextCompat.getColor(getContext(), R.color.FontDark));
                mBtnCameraVideo.setTextColor(ContextCompat.getColor(getContext(), R.color.FontDark));
            }
        } else {
            mBtnCameraImage.setVisibility(View.GONE);
            mBtnCameraVideo.setVisibility(View.GONE);
        }
    }

    /**
     * Camera click.
     */
    private View.OnClickListener mCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_camera_image) {
                Album.camera(getContext())
                        .image()
                        .onResult(mCameraAction)
                        .start();
            } else if (id == R.id.btn_camera_video) {
                Album.camera(getContext())
                        .video()
                        .onResult(mCameraAction)
                        .start();
            }
        }
    };

    private Action<String> mCameraAction = new Action<String>() {
        @Override
        public void onAction(int requestCode, @NonNull String result) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_OUTPUT_IMAGE_PATH, result);
            setResult(RESULT_OK, bundle);
            finish();
        }
    };
}