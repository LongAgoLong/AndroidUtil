package com.leo.commonutil.media

import com.leo.commonutil.media.mp3.Mp3PlayState

interface IPlayer {
    fun initMediaPlayer()
    fun start(url: String)
    fun toggle(@Mp3PlayState state: Int)
    fun reset()
    fun destroy()
}