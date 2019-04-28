package com.leo.commonutil.media.recode;

public interface OnAudioStatuListener {
    /**
     * 录音中...
     *
     * @param db   当前声音分贝
     * @param time 录音时长
     */
    void onUpdate(double db, long time);

    /**
     * 停止录音
     *
     * @param filePath 保存路径
     */
    void onStop(String filePath, long time);

    /**
     * 取消录音
     */
    void onCancel();
}
