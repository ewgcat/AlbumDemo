package com.lishuaihua.album.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.album.ui.adapter.AlbumFilePreviewAdapter;
import com.lishuaihua.album.ui.adapter.BasicPreviewAdapter;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.R;
import com.lishuaihua.album.util.AlbumUtils;
import com.lishuaihua.fragment.NoFragment;

import java.util.ArrayList;
import java.util.List;


public class AlbumPreviewFragment extends NoFragment {

    private MenuItem mFinishMenuItem;

    private ViewPager mViewPager;
    private TextView mTvDuration;
    private AppCompatCheckBox mCheckBox;
    private FrameLayout mLayoutLayer;

    @Album.ChoiceFunction
    private int mFunction;
    private Widget mWidget;
    private int mAllowSelectCount;

    private List<AlbumFile> mAlbumFiles = new ArrayList<>(1);
    private List<AlbumFile> mCheckedFiles = new ArrayList<>(1);
    private int mCurrentItemPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTvDuration = (TextView) view.findViewById(R.id.tv_duration);
        mCheckBox = (AppCompatCheckBox) view.findViewById(R.id.cb_check);
        mLayoutLayer = (FrameLayout) view.findViewById(R.id.layout_layer);

        setToolbar(toolbar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayHomeAsUpEnabled(R.drawable.ic_back_white);

        Bundle argument = getArguments();
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        //noinspection WrongConstant
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        mAllowSelectCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);

        initializeWidget();
        initializeViewPager();

        setCheckedCountUI(mCheckedFiles.size());
    }

    private void initializeWidget() {
        ColorStateList itemSelector = mWidget.getMediaItemCheckSelector();
        mCheckBox.setSupportButtonTintList(itemSelector);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckBox.isChecked();
                AlbumFile albumFile = mAlbumFiles.get(mCurrentItemPosition);
                albumFile.setChecked(isChecked);

                if (isChecked) {
                    if (mCheckedFiles.size() >= mAllowSelectCount) {
                        int messageRes;
                        switch (mFunction) {
                            case Album.FUNCTION_CHOICE_IMAGE: {
                                messageRes = R.plurals.check_image_limit;
                                break;
                            }
                            case Album.FUNCTION_CHOICE_VIDEO: {
                                messageRes = R.plurals.check_video_limit;
                                break;
                            }
                            case Album.FUNCTION_CHOICE_ALBUM:
                            default: {
                                messageRes = R.plurals.check_limit;
                                break;
                            }
                        }
                        Toast.makeText(
                                getContext(),
                                getResources().getQuantityString(messageRes, mAllowSelectCount, mAllowSelectCount),
                                Toast.LENGTH_LONG).show();

                        mCheckBox.setChecked(false);
                        albumFile.setChecked(false);
                    } else {
                        mCheckedFiles.add(albumFile);
                    }
                } else {
                    mCheckedFiles.remove(albumFile);
                }
                setCheckedCountUI(mCheckedFiles.size());
            }
        });
    }

    private void initializeViewPager() {
        if (mAlbumFiles != null) {
            if (mAlbumFiles.size() > 3)
                mViewPager.setOffscreenPageLimit(3);
            else if (mAlbumFiles.size() > 2)
                mViewPager.setOffscreenPageLimit(2);
        }

        BasicPreviewAdapter previewAdapter = new AlbumFilePreviewAdapter(getContext(), mAlbumFiles);
        mViewPager.setAdapter(previewAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                AlbumFile albumFile = mAlbumFiles.get(mCurrentItemPosition);
                mCheckBox.setChecked(albumFile.isChecked());
                mCheckBox.setEnabled(albumFile.isEnable());
                setTitle(mCurrentItemPosition + 1 + " / " + mAlbumFiles.size());

                if (albumFile.getMediaType() == AlbumFile.TYPE_VIDEO) {
                    mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
                    mTvDuration.setVisibility(View.VISIBLE);
                } else {
                    mTvDuration.setVisibility(View.GONE);
                }

                mLayoutLayer.setVisibility(albumFile.isEnable() ? View.GONE : View.VISIBLE);
            }
        };
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(mCurrentItemPosition);
        // Forced call.
        pageChangeListener.onPageSelected(mCurrentItemPosition);
    }

    /**
     * Bind the click folder.
     *
     * @param albumFiles image list.
     */
    public void bindAlbumFiles(List<AlbumFile> albumFiles, List<AlbumFile> checkedFiles, int currentItemPosition) {
        this.mAlbumFiles.addAll(albumFiles);
        this.mCheckedFiles = checkedFiles;
        this.mCurrentItemPosition = currentItemPosition;
    }

    /**
     * Set the number of selected pictures.
     *
     * @param count number.
     */
    private void setCheckedCountUI(int count) {
        String finishStr = getString(R.string.menu_finish);
        finishStr += "(" + count + " / " + mAllowSelectCount + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_preview, menu);
        mFinishMenuItem = menu.findItem(R.id.menu_finish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_finish) {
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}
