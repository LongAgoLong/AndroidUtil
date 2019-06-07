package com.leo.recyclerview_help.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by NyatoLEO on 2017/4/10.
 */

public class LineItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int orientation;
    private int itemSpacePX, otherDirectionSpace;
    private boolean withStart;
    private boolean withEnd;

    private Paint mPaint;

    public LineItemDecoration(Context context, int orientation, int itemSpacePX) {
        this(context, orientation, itemSpacePX, false, false, 0);
    }

    public LineItemDecoration(Context context, int orientation, int itemSpacePX, boolean withStart, boolean withEnd) {
        this(context, orientation, itemSpacePX, withStart, withEnd, -1);
    }

    public LineItemDecoration(Context context, int orientation, int itemSpacePX, boolean withStart, boolean withEnd, int color) {
        this(context, orientation, itemSpacePX, 0, withStart, withEnd, color);
    }

    public LineItemDecoration(Context context, int orientation, int itemSpacePX, int otherDirectionSpace, boolean withStart, boolean withEnd) {
        this(context, orientation, itemSpacePX, otherDirectionSpace, withStart, withEnd, -1);
    }

    public LineItemDecoration(Context context, int orientation, int itemSpacePX, int otherDirectionSpace, boolean withStart, boolean withEnd, int color) {
        super();
        this.context = context;
        this.orientation = orientation;
        this.itemSpacePX = itemSpacePX;
        this.otherDirectionSpace = otherDirectionSpace;
        this.withStart = withStart;
        this.withEnd = withEnd;
        if (-1 != color) {
            mPaint = new Paint();
            mPaint.setColor(color);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (orientation == OrientationHelper.VERTICAL) {
            if (withStart && position == 0)
                outRect.top = itemSpacePX;
            if (!withEnd && position == parent.getAdapter().getItemCount() - 1)
                return;
            outRect.bottom = itemSpacePX;
            if (0 != otherDirectionSpace) {
                outRect.left = otherDirectionSpace;
                outRect.right = otherDirectionSpace;
            }
        } else {
            if (withStart && position == 0)
                outRect.left = itemSpacePX;
            if (!withEnd && position == parent.getAdapter().getItemCount() - 1)
                return;
            outRect.right = itemSpacePX;
            if (0 != otherDirectionSpace) {
                outRect.top = otherDirectionSpace;
                outRect.bottom = otherDirectionSpace;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (null == mPaint || layoutManager.getChildCount() == 0)
            return;
        if (orientation == OrientationHelper.VERTICAL) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top1 = child.getTop() - params.bottomMargin - Math.round(ViewCompat.getTranslationY(child)) - itemSpacePX;
                final int bottom1 = top1 + itemSpacePX;
                final int top2 = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                final int bottom2 = top2 + itemSpacePX;
                if (0 != otherDirectionSpace) {//其他方向间隔线
                    if (withStart && withEnd) {
                        if (i == layoutManager.getChildCount() - 1) {
                            c.drawRect(left, top1, left + otherDirectionSpace, bottom2, mPaint);
                            c.drawRect(right - otherDirectionSpace, top1, right, bottom2, mPaint);
                        } else {
                            c.drawRect(left, top1, left + otherDirectionSpace, top2, mPaint);
                            c.drawRect(right - otherDirectionSpace, top1, right, top2, mPaint);
                        }
                    } else if (withStart) {
                        c.drawRect(left, top1, left + otherDirectionSpace, top2, mPaint);
                        c.drawRect(right - otherDirectionSpace, top1, right, top2, mPaint);
                    } else if (withEnd) {
                        c.drawRect(left, bottom1, left + otherDirectionSpace, bottom2, mPaint);
                        c.drawRect(right - otherDirectionSpace, bottom1, right, bottom2, mPaint);
                    }
                }
                if (withStart && i == 0) {
                    c.drawRect(left, top1, right, bottom1, mPaint);
                }
                if (!withEnd && i == layoutManager.getChildCount() - 1)
                    break;
                c.drawRect(left, top2, right, bottom2, mPaint);
            }
        } else {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left1 = child.getLeft() - params.rightMargin - Math.round(ViewCompat.getTranslationX(child)) - itemSpacePX;
                final int right1 = left1 + itemSpacePX;
                final int left2 = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                final int right2 = left2 + itemSpacePX;
                if (0 != otherDirectionSpace) {//其他方向间隔线
                    if (withStart && withEnd) {
                        if (i == layoutManager.getChildCount() - 1) {
                            c.drawRect(left1, top, right2, top + otherDirectionSpace, mPaint);
                            c.drawRect(left1, bottom - otherDirectionSpace, right2, bottom, mPaint);
                        } else {
                            c.drawRect(left1, top, left2, top + otherDirectionSpace, mPaint);
                            c.drawRect(left1, bottom - otherDirectionSpace, left2, bottom, mPaint);
                        }
                    } else if (withStart) {
                        c.drawRect(left1, top, left2, top + otherDirectionSpace, mPaint);
                        c.drawRect(left1, bottom - otherDirectionSpace, left2, bottom, mPaint);
                    } else if (withEnd) {
                        c.drawRect(right1, top, right2, top + otherDirectionSpace, mPaint);
                        c.drawRect(right1, bottom - otherDirectionSpace, right2, bottom, mPaint);
                    }
                }
                if (withStart && i == 0) {
                    c.drawRect(left1, top, right1, bottom, mPaint);
                }
                if (!withEnd && i == layoutManager.getChildCount() - 1)
                    break;
                c.drawRect(left2, top, right2, bottom, mPaint);
            }
        }
    }
}
