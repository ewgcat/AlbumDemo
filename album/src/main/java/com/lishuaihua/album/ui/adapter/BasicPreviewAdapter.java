package com.lishuaihua.album.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.lishuaihua.album.widget.photoview.AttacherImageView;
import com.lishuaihua.album.widget.photoview.PhotoViewAttacher;

import java.util.List;


public abstract class BasicPreviewAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<T> mPreviewList;

    public BasicPreviewAdapter(Context context, List<T> previewList) {
        this.mContext = context;
        this.mPreviewList = previewList;
    }

    @Override
    public int getCount() {
        return mPreviewList == null ? 0 : mPreviewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    protected abstract boolean loadPreview(ImageView imageView, T t, int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AttacherImageView imageView = new AttacherImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        imageView.setAttacher(attacher);

        T t = mPreviewList.get(position);
        loadPreview(imageView, t, position);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }
}
