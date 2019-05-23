package com.leo.commonutil.system;

import android.view.View.MeasureSpec;

/**
 * 自定义View测量辅助工具类
 */
public class ViewHelp {
    private ViewHelp() {
    }

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
