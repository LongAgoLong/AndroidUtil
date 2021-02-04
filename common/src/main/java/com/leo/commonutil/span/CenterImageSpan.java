package com.leo.commonutil.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * Create by LEO
 * on 2018/6/7
 * at 10:11

 * TextView图文混排居中
 */

public class CenterImageSpan extends ImageSpan {
    public CenterImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
    }

    public CenterImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenterImageSpan(Drawable d) {
        super(d, ALIGN_BOTTOM);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}