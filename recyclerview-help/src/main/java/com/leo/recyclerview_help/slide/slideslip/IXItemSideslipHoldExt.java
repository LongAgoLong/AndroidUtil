package com.leo.recyclerview_help.slide.slideslip;

import android.view.View;

public interface IXItemSideslipHoldExt {

    float getActionWidth();

    View getSlideView();

    boolean canSlide();

    void setCanSlide(boolean canSlide);

    void close();
}
