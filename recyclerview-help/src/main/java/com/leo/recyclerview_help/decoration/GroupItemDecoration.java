package com.leo.recyclerview_help.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 分组ItemDecoration
 * Created by LEO
 * on 2018/2/7.
 */

public class GroupItemDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int paddingStartDp;
    private boolean shouldOver;

    public GroupItemDecoration(Context context, int topGapDp, int paddingStartDp, int textSizeDp,
                               @ColorRes int textColor, @ColorRes int bgColor, boolean shouldOver, @NonNull DecorationCallback decorationCallback) {
        Resources res = context.getResources();
        this.callback = decorationCallback;
        //设置悬浮栏的画笔---paint
        paint = new Paint();
        paint.setColor(res.getColor(bgColor));

        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSizeDp);
        textPaint.setColor(res.getColor(textColor));
        textPaint.setTextAlign(Paint.Align.LEFT);
        this.paddingStartDp = paddingStartDp;
        //决定悬浮栏的高度等
        topGap = topGapDp;
        this.shouldOver = shouldOver;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = callback.getGroupId(pos);
        if (TextUtils.isEmpty(groupId)) return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String groupId = callback.getGroupId(position);
            if (TextUtils.isEmpty(groupId)) return;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.equals(textLine, "")) {
                float top = view.getTop();
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);
                return;
            } else {
                if (position == 0 || isFirstInGroup(position)) {
                    float top = view.getTop() - topGap;
                    float bottom = view.getTop();
                    //绘制悬浮栏
                    c.drawRect(left, top - topGap, right, bottom, paint);
                    //绘制文本
                    Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
                    int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
                    c.drawText(textLine, left + paddingStartDp, baseline, textPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!shouldOver)
            return;
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        String preGroupId = "";
        String groupId = "-1";
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (TextUtils.isEmpty(groupId) || groupId.equals(preGroupId)) continue;

            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                //组内最后一个view进入了header
                if (!TextUtils.equals(nextGroupId, groupId) && viewBottom < textY) {
                    textY = viewBottom;
                }
            }
            //textY - topGap决定了悬浮栏绘制的高度和位置
            float top = textY - topGap;
            float bottom = textY;
            c.drawRect(left, textY - topGap, right, textY, paint);
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
            c.drawText(textLine, left + paddingStartDp, baseline, textPaint);
        }
    }


    /**
     * 判断是不是组中的第一个位置
     */
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同意组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String prevGroupId = callback.getGroupId(pos - 1);
            String groupId = callback.getGroupId(pos);
            //判断前一个字符串 与 当前字符串 是否相同
            return !prevGroupId.equals(groupId);
        }
    }

    //定义一个接口方便外界的调用
    public interface DecorationCallback {
        String getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}
