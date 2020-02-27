package com.leo.recyclerview_help.slide.slideslip;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RvItemSideslipViewHold extends RecyclerView.ViewHolder implements IRvItemSideslipHoldExt {
    private boolean canSlide = true;

    public RvItemSideslipViewHold(View itemView) {
        super(itemView);
    }

    @Override
    public void close() {
        getSlideView().setTranslationX(getActionWidth());
    }

    @Override
    public boolean canSlide() {
        return canSlide;
    }

    @Override
    public void setCanSlide(boolean canSlide) {
        this.canSlide = canSlide;
    }
}
