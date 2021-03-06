package com.leo.commonutil.media

import com.leo.commonutil.media.mp3.Mp3PlayState

interface IPlayer {
    fun initMediaPlayer()
    fun start(url: String)
    fun toggle(state: Mp3PlayState)
    fun reset()
    fun destroy()
}