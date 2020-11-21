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

import org.jetbrains.annotations.NotNull;

/**
 * Created by NyatoLEO on 2017/4/10.
 */

public class LineItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int orientation;
    private int itemGapPx, otherDirectionGap;
    private boolean withStart;
    private boolean withEnd;

    private Paint mPaint;

    public LineItemDecoration(Context context, int orientation, int itemGapPx) {
        this(context, orientation, itemGapPx, false, false, 0);
    }

    public LineItemDecoration(Context context, int orientation, int itemGapPx, boolean withStart, boolean withEnd) {
        this(context, orientation, itemGapPx, withStart, withEnd, -1);
    }

    public LineItemDecoration(Context context, int orientation, int itemGapPx, boolean withStart, boolean withEnd,
                              int color) {
        this(context, orientation, itemGapPx, 0, withStart, withEnd, color);
    }

    public LineItemDecoration(Context context, int orientation, int itemGapPx, int otherDirectionGap,
                              boolean withStart, boolean withEnd) {
        this(context, orientation, itemGapPx, otherDirectionGap, withStart, withEnd, -1);
    }

    public LineItemDecoration(Context context, int orientation, int itemGapPx, int otherDirectionGap,
                              boolean withStart, boolean withEnd, int color) {
        super();
        this.context = context;
        this.orientation = orientation;
        this.itemGapPx = itemGapPx;
        this.otherDirectionGap = otherDirectionGap;
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
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent,
                               @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (null == parent.getAdapter()) {
            return;
        }
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (orientation == OrientationHelper.VERTICAL) {
            if (withStart && position == 0) {
                outRect.top = itemGapPx;
            }
            if (!withEnd && position == parent.getAdapter().getItemCount() - 1) {
                return;
            }
            outRect.bottom = itemGapPx;
            if (0 != otherDirectionGap) {
                outRect.left = otherDirectionGap;
                outRect.right = otherDirectionGap;
            }
        } else {
            if (withStart && position == 0) {
                outRect.left = itemGapPx;
            }
            if (!withEnd && position == parent.getAdapter().getItemCount() - 1) {
                return;
            }
            outRect.right = itemGapPx;
            if (0 != otherDirectionGap) {
                outRect.top = otherDirectionGap;
                outRect.bottom = otherDirectionGap;
            }
        }
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (null == mPaint || null == layoutManager || layoutManager.getChildCount() == 0) {
            return;
        }
        if (orientation == OrientationHelper.VERTICAL) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top1 =
                        child.getTop() - params.bottomMargin - Math.round(ViewCompat.getTranslationY(child)) - itemGapPx;
                final int bottom1 = top1 + itemGapPx;
                final int top2 =
                        child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                final int bottom2 = top2 + itemGapPx;
                if (0 != otherDirectionGap) {//其他方向间隔线
                    if (withStart && withEnd) {
                        if (i == layoutManager.getChildCount() - 1) {
                            c.drawRect(left, top1, left + otherDirectionGap, bottom2, mPaint);
                            c.drawRect(right - otherDirectionGap, top1, right, bottom2, mPaint);
                        } else {
                            c.drawRect(left, top1, left + otherDirectionGap, top2, mPaint);
                            c.drawRect(right - otherDirectionGap, top1, right, top2, mPaint);
                        }
                    } else if (withStart) {
                        c.drawRect(left, top1, left + otherDirectionGap, top2, mPaint);
                        c.drawRect(right - otherDirectionGap, top1, right, top2, mPaint);
                    } else if (withEnd) {
                        c.drawRect(left, bottom1, left + otherDirectionGap, bottom2, mPaint);
                        c.drawRect(right - otherDirectionGap, bottom1, right, bottom2, mPaint);
                    }
                }
                if (withStart && i == 0) {
                    c.drawRect(left, top1, right, bottom1, mPaint);
                }
                if (!withEnd && i == layoutManager.getChildCount() - 1) {
                    break;
                }
                c.drawRect(left, top2, right, bottom2, mPaint);
            }
        } else {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left1 =
                        child.getLeft() - params.rightMargin - Math.round(ViewCompat.getTranslationX(child)) - itemGapPx;
                final int right1 = left1 + itemGapPx;
                final int left2 = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                final int right2 = left2 + itemGapPx;
                if (0 != otherDirectionGap) {
                    // 其他方向间隔线
                    if (withStart && withEnd) {
                        if (i == layoutManager.getChildCount() - 1) {
                            c.drawRect(left1, top, right2, top + otherDirectionGap, mPaint);
                            c.drawRect(left1, bottom - otherDirectionGap, right2, bottom, mPaint);
                        } else {
                            c.drawRect(left1, top, left2, top + otherDirectionGap, mPaint);
                            c.drawRect(left1, bottom - otherDirectionGap, left2, bottom, mPaint);
                        }
                    } else if (withStart) {
                        c.drawRect(left1, top, left2, top + otherDirectionGap, mPaint);
                        c.drawRect(left1, bottom - otherDirectionGap, left2, bottom, mPaint);
                    } else if (withEnd) {
                        c.drawRect(right1, top, right2, top + otherDirectionGap, mPaint);
                        c.drawRect(right1, bottom - otherDirectionGap, right2, bottom, mPaint);
                    }
                }
                if (withStart && i == 0) {
                    c.drawRect(left1, top, right1, bottom, mPaint);
                }
                if (!withEnd && i == layoutManager.getChildCount() - 1) {
                    break;
                }
                c.drawRect(left2, top, right2, bottom, mPaint);
            }
        }
    }
}
