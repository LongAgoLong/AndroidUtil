package com.leo.commonutil.media.recode

import android.media.MediaRecorder
import com.leo.commonutil.asyn.WeakHandler
import com.leo.commonutil.calendar.DatePresetFormat
import com.leo.commonutil.calendar.DateUtil
import com.leo.commonutil.enume.UnitTime
import com.leo.system.log.XLog.e
import com.leo.system.log.XLog.i
import java.io.File
import java.io.IOException

/**
 * Created by LEO
 * on 2017/5/11.
 * 录音封装类
 */
class MediaRecordHelper(filePath: String, timeSecond: Int) {
    // 文件路径
    private var filePath: String? = null

    // 文件夹路径
    private val FolderPath: String
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var mMediaRecorder: MediaRecorder? = null
    private var iMediaRecordListener: IMediaRecordListener? = null
    private var fileTime: Long = 0

    /**
     * 开始录音 使用amr格式
     * 录音文件
     */
    fun startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = MediaRecorder()
        }
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            val time = DateUtil.format(UnitTime.MILLIONSECOND,
                    System.currentTimeMillis(),
                    DatePresetFormat.DATA_YMDHM2)
            filePath = "$FolderPath$time.amr"
            mMediaRecorder!!.setOutputFile(filePath)
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            /* ③准备 */mMediaRecorder!!.setMaxDuration(MAX_LENGTH)
            /* 获取开始时间* */startTime = System.currentTimeMillis()
            mMediaRecorder!!.prepare()
            /* ④开始 */mMediaRecorder!!.start()
            if (null != iMediaRecordListener) {
                iMediaRecordListener!!.onStart()
            }
            updateMicStatus()
            i(TAG, "startTime:$startTime")
        } catch (e: IOException) {
            e.printStackTrace()
            e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.message)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.message)
        }
    }

    /**
     * 停止录音
     */
    fun stopRecord(): Long {
        if (null != mUpdateMicStatusTimer) {
            mHandler.removeCallbacks(mUpdateMicStatusTimer)
        }
        if (mMediaRecorder == null) {
            return 0L
        }
        endTime = System.currentTimeMillis()

        // 有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况
        // 捕获异常清理一下就行了
        try {
            mMediaRecorder!!.stop()
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            if (null != iMediaRecordListener) {
                iMediaRecordListener!!.onStop(filePath, fileTime)
            }
            filePath = ""
        } catch (e: RuntimeException) {
            if (null != mMediaRecorder) {
                mMediaRecorder!!.reset()
                mMediaRecorder!!.release()
                mMediaRecorder = null
            }
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
            filePath = ""
        }
        return endTime - startTime
    }

    /**
     * 取消录音
     */
    fun cancelRecord() {
        try {
            mMediaRecorder!!.stop()
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            if (null != iMediaRecordListener) {
                iMediaRecordListener!!.onCancel()
            }
        } catch (e: RuntimeException) {
            if (null != mMediaRecorder) {
                mMediaRecorder!!.reset()
                mMediaRecorder!!.release()
                mMediaRecorder = null
            }
        }
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        filePath = ""
        if (null != mUpdateMicStatusTimer) {
            mHandler.removeCallbacks(mUpdateMicStatusTimer)
        }
    }

    private val mUpdateMicStatusTimer: Runnable? = Runnable { updateMicStatus() }
    fun setMediaRecordListener(iMediaRecordListener: IMediaRecordListener?) {
        this.iMediaRecordListener = iMediaRecordListener
    }

    /**
     * 更新麦克状态
     */
    private fun updateMicStatus() {
        if (mMediaRecorder != null) {
            val ratio = mMediaRecorder!!.maxAmplitude.toDouble() / BASE
            val db: Double // 分贝
            fileTime = System.currentTimeMillis() - startTime
            if (ratio > 1) {
                db = 20 * Math.log10(ratio)
                if (null != iMediaRecordListener) iMediaRecordListener!!.onProgress(db, fileTime)
            }
            if (fileTime >= MAX_LENGTH) {
                stopRecord()
            } else {
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE.toLong())
            }
        }
    }

    companion object {
        private const val TAG = "Audio"
        private var MAX_LENGTH = 1000 * 60 * 10 // 最大录音时长1000*60*10;
        private const val BASE = 1
        private const val SPACE = 500 // 间隔取样时间
        private val mHandler = WeakHandler()
    }

    init {
        MAX_LENGTH = timeSecond * 1000
        val path = File(filePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        FolderPath = filePath
    }
}