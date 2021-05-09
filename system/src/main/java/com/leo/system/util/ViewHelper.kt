package com.leo.system.util

import android.graphics.Paint
import android.graphics.RectF
import android.view.View.MeasureSpec
import kotlin.math.min

/**
 * 自定义View测量辅助工具类
 */
object ViewHelper {
    /**
     * 获取竖直居中基准线
     *
     * @param targetRect
     * @param paint
     * @return
     */
    fun getCenterYBaseLine(targetRect: RectF, paint: Paint): Int {
        val fontMetrics = paint.fontMetricsInt
        return ((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2).toInt()
    }

    /**
     * 解析自定义view尺寸
     *
     * @param size
     * @param measureSpec
     * @return
     */
    fun getMeasureExpectSize(size: Int, measureSpec: Int): Int {
        var result = size
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.EXACTLY -> result = specSize
            MeasureSpec.UNSPECIFIED -> result = size
            MeasureSpec.AT_MOST -> result = min(size, specSize)
        }
        return result
    }
}