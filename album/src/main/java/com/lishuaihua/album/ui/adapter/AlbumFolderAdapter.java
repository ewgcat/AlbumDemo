package com.lishuaihua.album.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.AlbumFolder;
import com.lishuaihua.album.R;
import com.lishuaihua.album.impl.OnItemClickListener;
import com.lishuaihua.album.util.DisplayUtils;

import java.util.List;

public class AlbumFolderAdapter extends RecyclerView.Adapter<AlbumFolderAdapter.FolderViewHolder> {

    private LayoutInflater mInflater;
    private List<AlbumFolder> mAlbumFolders;
    private ColorStateList mSelector;

    private int mImageSize = DisplayUtils.dip2px(80);
    private OnItemClickListener mItemClickListener;

    public AlbumFolderAdapter(Context context, List<AlbumFolder> mAlbumFolders, ColorStateList buttonTint) {
        this.mInflater = LayoutInflater.from(context);
        this.mSelector = buttonTint;
        this.mAlbumFolders = mAlbumFolders;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderViewHolder(mInflater.inflate(R.layout.item_dialog_folder, parent, false),
                mImageSize,
                mSelector,
                new OnItemClickListener() {

                    private int oldPosition = 0;

                    @Override
                    public void onItemClick(View view, int position) {
                        if (mItemClickListener != null)
                            mItemClickListener.onItemClick(view, position);

                        AlbumFolder albumFolder = mAlbumFolders.get(position);
                        if (!albumFolder.isChecked()) {
                            albumFolder.setChecked(true);
                            mAlbumFolders.get(oldPosition).setChecked(false);
                            notifyItemChanged(oldPosition);
                            notifyItemChanged(position);
                            oldPosition = position;
                        }
                    }
                });
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        final int newPosition = holder.getAdapterPosition();
        holder.setData(mAlbumFolders.get(newPosition));
    }

    @Override
    public int getItemCount() {
        return mAlbumFolders == null ? 0 : mAlbumFolders.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int mImageSize;
        private OnItemClickListener mItemClickListener;

        private ImageView mIvImage;
        private TextView mTvTitle;
        private AppCompatRadioButton mCheckBox;

        private FolderViewHolder(View itemView, int imageSize, ColorStateList selector, OnItemClickListener itemClickListener) {
            super(itemView);

            this.mImageSize = imageSize;
            this.mItemClickListener = itemClickListener;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_gallery_preview_image);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_gallery_preview_title);
            mCheckBox = (AppCompatRadioButton) itemView.findViewById(R.id.rb_gallery_preview_check);

            itemView.setOnClickListener(this);

            mCheckBox.setSupportButtonTintList(selector);
        }

        public void setData(AlbumFolder albumFolder) {
            List<AlbumFile> albumFiles = albumFolder.getAlbumFiles();
            mTvTitle.setText("(" + albumFiles.size() + ") " + albumFolder.getName());
            mCheckBox.setChecked(albumFolder.isChecked());

            Album.getAlbumConfig().getAlbumLoader().loadAlbumFile(mIvImage, albumFiles.get(0), mImageSize, mImageSize);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
