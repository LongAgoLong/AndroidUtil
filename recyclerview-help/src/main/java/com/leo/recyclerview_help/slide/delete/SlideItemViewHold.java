package com.leo.recyclerview_help.slide.delete;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class SlideItemViewHold extends RecyclerView.ViewHolder implements Extension {
    private boolean canSlide = true;

    public SlideItemViewHold(View itemView) {
        super(itemView);
    }

    @Override
    public boolean canSlide() {
        return canSlide;
    }

    protected void setCanSlide(boolean canSlide) {
        this.canSlide = canSlide;
    }
}
