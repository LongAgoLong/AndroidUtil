package com.leo.commonutil.media.mp3;

import com.leo.commonutil.media.util.MediaUtils;
import com.leo.commonutil.media.util.TimeMode;

public class Mp3PlayerParams {
    private int position = 0;
    @Mp3PlayState
    private int playState = Mp3PlayState.NONE;

    private int duration; // millisecond
    private int bufferPercent;

    public int getPosition() {
        return position;
    }

    public Mp3PlayerParams setPosition(int position) {
        this.position = position;
        return this;
    }

    public int getPlayState() {
        return playState;
    }

    public Mp3PlayerParams setPlayState(@Mp3PlayState int playState) {
        this.playState = playState;
        return this;
    }

    public String getDurationFormat() {
        return MediaUtils.timeFormat(getDuration(), TimeMode.MODE_FORMAT);
    }

    public int getDuration() {
        return duration;
    }

    public Mp3PlayerParams setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getBufferPercent() {
        return bufferPercent;
    }

    public Mp3PlayerParams setBufferPercent(int bufferPercent) {
        this.bufferPercent = bufferPercent;
        return this;
    }
}
