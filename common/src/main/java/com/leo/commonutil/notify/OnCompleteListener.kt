package com.leo.commonutil.notify

import android.media.MediaPlayer

/**
 * Create by LEO
 * on 2018/8/20
 * at 14:20
 * MediaPlayer播放完毕监听
 */
interface OnCompleteListener {
    fun onCompletion(mp: MediaPlayer)
}
