package com.leo.commonutil.media.mp3

import com.leo.commonutil.media.util.MediaUtils
import com.leo.commonutil.media.util.TimeMode

class Mp3PlayerParams {
    var position = 0 // millisecond

    @Mp3PlayState
    var playState = Mp3PlayState.NONE
    var duration = 0 // millisecond
    var bufferPercent = 0
    val durationFormat: String
        get() = MediaUtils.timeFormat(duration, TimeMode.MODE_FORMAT)
}