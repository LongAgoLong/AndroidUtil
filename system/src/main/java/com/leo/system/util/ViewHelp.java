package com.leo.system.util;

import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View.MeasureSpec;

/**
 * 自定义View测量辅助工具类
 */
public class ViewHelp {
    private ViewHelp() {
    }

    /**
     * 获取竖直居中基准线
     *
     * @param targetRect
     * @param paint
     * @return
     */
    public static int getCenterYBaseLine(RectF targetRect, Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (int) ((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseline;
    }

    /**
     * 解析自定义view尺寸
     *
     * @param size
     * @param measureSpec
     * @return
     */
    public static int getMeasureExpectSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
        }
        return result;
    }
}
