package com.lishuaihua.album.widget.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Api20ItemDivider extends Divider {

    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;

    /**
     * @param color divider color.
     */
    public Api20ItemDivider(@ColorInt int color) {
        this(color, 4, 4);
    }

    /**
     * @param color         line color.
     * @param dividerWidth  line width.
     * @param dividerHeight line height.
     */
    public Api20ItemDivider(@ColorInt int color, int dividerWidth, int dividerHeight) {
        mDivider = new ColorDrawable(color);
        mDividerWidth = dividerWidth;
        mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int columnCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean firstRaw = isFirstRaw(position, columnCount);
        boolean lastRaw = isLastRaw(position, columnCount, childCount);
        boolean firstColumn = isFirstColumn(position, columnCount);
        boolean lastColumn = isLastColumn(position, columnCount);

        if (columnCount == 1) {
            if (firstRaw) {
                outRect.set(0, 0, 0, mDividerHeight / 2);
            } else if (lastRaw) {
                outRect.set(0, mDividerHeight / 2, 0, 0);
            } else {
                outRect.set(0, mDividerHeight / 2, 0, mDividerHeight / 2);
            }
        } else {
            if (firstRaw && firstColumn) {
                outRect.set(0, 0, mDividerWidth / 2, mDividerHeight / 2);
            } else if (firstRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, 0, 0, mDividerHeight / 2);
            } else if (firstRaw) {
                outRect.set(mDividerWidth / 2, 0, mDividerWidth / 2, mDividerHeight / 2);
            } else if (lastRaw && firstColumn) {
                outRect.set(0, mDividerHeight / 2, mDividerWidth / 2, 0);
            } else if (lastRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, 0, 0);
            } else if (lastRaw) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, 0);
            } else if (firstColumn) {
                outRect.set(0, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2);
            } else if (lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, 0, mDividerHeight / 2);
            } else {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2);
            }
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    private boolean isFirstRaw(int position, int columnCount) {
        return position < columnCount;
    }

    private boolean isLastRaw(int position, int columnCount, int childCount) {
        if (columnCount == 1)
            return position + 1 == childCount;
        else {
            int lastRawItemCount = childCount % columnCount;
            int rawCount = (childCount - lastRawItemCount) / columnCount + (lastRawItemCount > 0 ? 1 : 0);

            int rawPositionJudge = (position + 1) % columnCount;
            if (rawPositionJudge == 0) {
                int rawPosition = (position + 1) / columnCount;
                return rawCount == rawPosition;
            } else {
                int rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1;
                return rawCount == rawPosition;
            }
        }
    }

    private boolean isFirstColumn(int position, int columnCount) {
        if (columnCount == 1)
            return true;
        return position % columnCount == 0;
    }

    private boolean isLastColumn(int position, int columnCount) {
        if (columnCount == 1)
            return true;
        return (position + 1) % columnCount == 0;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        c.save();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getLeft();
            final int top = child.getBottom();
            final int right = child.getRight();
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        c.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getRight();
            final int top = child.getTop();
            final int right = left + mDividerWidth;
            final int bottom = child.getBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    @Override
    public int getHeight() {
        return mDividerHeight;
    }

    @Override
    public int getWidth() {
        return mDividerWidth;
    }
}
