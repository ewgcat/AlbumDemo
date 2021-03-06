package com.lishuaihua.album.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.lishuaihua.album.impl.OnItemCheckedListener;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.R;
import com.lishuaihua.album.impl.OnItemClickListener;
import com.lishuaihua.album.util.AlbumUtils;

import java.util.List;


public class AlbumFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_VIDEO = 3;

    private final LayoutInflater mInflater;
    private final int itemSize;
    private final boolean hasCamera;
    @Album.ChoiceMode
    private final int mChoiceMode;
    private final ColorStateList mSelector;

    private List<AlbumFile> mAlbumFiles;

    private OnItemClickListener mAddPhotoClickListener;
    private OnItemClickListener mItemClickListener;
    private OnItemCheckedListener mItemCheckedListener;

    public AlbumFileAdapter(Context context, int itemSize, boolean hasCamera, @Album.ChoiceMode int choiceMode, ColorStateList selector) {
        this.mInflater = LayoutInflater.from(context);
        this.hasCamera = hasCamera;
        this.itemSize = itemSize;
        this.mChoiceMode = choiceMode;
        this.mSelector = selector;
    }

    public void notifyDataSetChanged(List<AlbumFile> albumFiles) {
        this.mAlbumFiles = albumFiles;
        super.notifyDataSetChanged();
    }

    public void setAddClickListener(OnItemClickListener addPhotoClickListener) {
        this.mAddPhotoClickListener = addPhotoClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setItemCheckedListener(OnItemCheckedListener checkListener) {
        this.mItemCheckedListener = checkListener;
    }

    @Override
    public int getItemCount() {
        int camera = hasCamera ? 1 : 0;
        return mAlbumFiles == null ? camera : mAlbumFiles.size() + camera;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: {
                return hasCamera ? TYPE_BUTTON : TYPE_IMAGE;
            }
            default: {
                position = hasCamera ? position - 1 : position;
                AlbumFile albumFile = mAlbumFiles.get(position);
                return albumFile.getMediaType() == AlbumFile.TYPE_VIDEO ? TYPE_VIDEO : TYPE_IMAGE;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BUTTON: {
                return new ButtonViewHolder(mInflater.inflate(R.layout.item_content_button, parent, false),
                        itemSize,
                        hasCamera,
                        mAddPhotoClickListener);
            }
            case TYPE_IMAGE: {
                return new ImageHolder(mInflater.inflate(R.layout.item_content_image, parent, false),
                        itemSize,
                        hasCamera,
                        mChoiceMode,
                        mSelector,
                        mItemClickListener,
                        mItemCheckedListener);
            }
            case TYPE_VIDEO:
            default: { // TYPE_VIDEO.
                return new VideoHolder(mInflater.inflate(R.layout.item_content_video, parent, false),
                        itemSize,
                        hasCamera,
                        mChoiceMode,
                        mSelector,
                        mItemClickListener,
                        mItemCheckedListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BUTTON: {
                // Nothing.
                break;
            }
            case TYPE_IMAGE: {
                ImageHolder imageHolder = (ImageHolder) holder;
                int camera = hasCamera ? 1 : 0;
                position = holder.getAdapterPosition() - camera;
                AlbumFile albumFile = mAlbumFiles.get(position);
                imageHolder.setData(albumFile);
                break;
            }
            case TYPE_VIDEO:
            default: { // TYPE_VIDEO.
                VideoHolder videoHolder = (VideoHolder) holder;
                int camera = hasCamera ? 1 : 0;
                position = holder.getAdapterPosition() - camera;
                AlbumFile albumFile = mAlbumFiles.get(position);
                videoHolder.setData(albumFile);
            }
        }
    }

    private static class ButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final boolean hasCamera;
        private final OnItemClickListener mItemClickListener;

        ButtonViewHolder(View itemView, int itemSize, boolean hasCamera, OnItemClickListener itemClickListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.hasCamera = hasCamera;
            this.mItemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && v == itemView) {
                int camera = hasCamera ? 1 : 0;
                mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
            }
        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final int itemSize;
        private final boolean hasCamera;
        @Album.ChoiceMode
        private final int mChoiceMode;

        private final OnItemClickListener mItemClickListener;
        private final OnItemCheckedListener mItemCheckedListener;

        private ImageView mIvImage;
        private AppCompatCheckBox mCheckBox;

        private FrameLayout mLayoutLayer;

        ImageHolder(View itemView, int itemSize, boolean hasCamera, @Album.ChoiceMode int choiceMode, ColorStateList selector,
                    OnItemClickListener itemClickListener, OnItemCheckedListener itemCheckedListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.itemSize = itemSize;
            this.hasCamera = hasCamera;
            this.mChoiceMode = choiceMode;
            this.mItemClickListener = itemClickListener;
            this.mItemCheckedListener = itemCheckedListener;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_content_image);
            mCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.cb_check);
            mLayoutLayer = (FrameLayout) itemView.findViewById(R.id.layout_layer);

            itemView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
            mLayoutLayer.setOnClickListener(this);
            if (mChoiceMode == Album.MODE_MULTIPLE) {
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setSupportButtonTintList(selector);
            } else {
                mCheckBox.setVisibility(View.GONE);
            }
        }

        void setData(AlbumFile albumFile) {
            mCheckBox.setChecked(albumFile.isChecked());
            Album.getAlbumConfig()
                    .getAlbumLoader()
                    .loadAlbumFile(mIvImage, albumFile, itemSize, itemSize);

            mLayoutLayer.setVisibility(albumFile.isEnable() ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                switch (mChoiceMode) {
                    case Album.MODE_SINGLE: {
                        if (mItemCheckedListener != null) {
                            mCheckBox.toggle();
                            int camera = hasCamera ? 1 : 0;
                            mItemCheckedListener.onCheckedChanged(mCheckBox, getAdapterPosition() - camera, mCheckBox.isChecked());
                        }
                        break;
                    }
                    case Album.MODE_MULTIPLE: {
                        if (mItemClickListener != null) {
                            int camera = hasCamera ? 1 : 0;
                            mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                        }
                        break;
                    }
                }
            } else if (v == mCheckBox) {
                if (mItemCheckedListener != null) {
                    boolean isChecked = mCheckBox.isChecked();
                    int camera = hasCamera ? 1 : 0;
                    mItemCheckedListener.onCheckedChanged(mCheckBox, getAdapterPosition() - camera, isChecked);
                }
            } else if (v == mLayoutLayer) {
                if (mItemClickListener != null) {
                    int camera = hasCamera ? 1 : 0;
                    mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                }
            }
        }
    }

    private static class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final int itemSize;
        private final boolean hasCamera;
        @Album.ChoiceMode
        private final int mChoiceMode;

        private final OnItemClickListener mItemClickListener;
        private final OnItemCheckedListener mItemCheckedListener;

        private ImageView mIvImage;
        private AppCompatCheckBox mCheckBox;
        private TextView mTvDuration;

        private FrameLayout mLayoutLayer;

        VideoHolder(View itemView, int itemSize, boolean hasCamera, @Album.ChoiceMode int choiceMode, ColorStateList selector,
                    OnItemClickListener itemClickListener, OnItemCheckedListener itemCheckedListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.itemSize = itemSize;
            this.hasCamera = hasCamera;
            this.mChoiceMode = choiceMode;
            this.mItemClickListener = itemClickListener;
            this.mItemCheckedListener = itemCheckedListener;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_content_image);
            mCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.cb_check);
            mTvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            mLayoutLayer = (FrameLayout) itemView.findViewById(R.id.layout_layer);

            itemView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
            mLayoutLayer.setOnClickListener(this);
            if (mChoiceMode == Album.MODE_MULTIPLE) {
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setSupportButtonTintList(selector);
            } else {
                mCheckBox.setVisibility(View.GONE);
            }
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().getAlbumLoader().loadAlbumFile(mIvImage, albumFile, itemSize, itemSize);
            mCheckBox.setChecked(albumFile.isChecked());
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));

            mLayoutLayer.setVisibility(albumFile.isEnable() ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                switch (mChoiceMode) {
                    case Album.MODE_SINGLE: {
                        if (mItemCheckedListener != null) {
                            mCheckBox.toggle();
                            int camera = hasCamera ? 1 : 0;
                            mItemCheckedListener.onCheckedChanged(mCheckBox, getAdapterPosition() - camera, mCheckBox.isChecked());
                        }
                        break;
                    }
                    case Album.MODE_MULTIPLE: {
                        if (mItemClickListener != null) {
                            int camera = hasCamera ? 1 : 0;
                            mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                        }
                        break;
                    }
                }
            } else if (v == mCheckBox) {
                if (mItemCheckedListener != null) {
                    boolean isChecked = mCheckBox.isChecked();
                    int camera = hasCamera ? 1 : 0;
                    mItemCheckedListener.onCheckedChanged(mCheckBox, getAdapterPosition() - camera, isChecked);
                }
            } else if (v == mLayoutLayer) {
                if (mItemClickListener != null) {
                    int camera = hasCamera ? 1 : 0;
                    mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                }
            }
        }
    }
}
