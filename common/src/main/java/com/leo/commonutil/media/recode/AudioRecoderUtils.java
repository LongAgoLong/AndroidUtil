package com.leo.commonutil.media.recode;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.leo.commonutil.calendar.DateUtil;
import com.leo.commonutil.system.LogUtil;
import com.leo.commonutil.asyn.WeakHandler;
import com.leo.commonutil.enume.UnitTime;

import java.io.File;
import java.io.IOException;

/**
 * Created by LEO
 * on 2017/5/11.
 * 录音封装类
 */
public class AudioRecoderUtils {
    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;

    private MediaRecorder mMediaRecorder;
    private static final String TAG = "nyato";
    private static int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    private OnAudioStatuListener audioStatusUpdateListener;
    private long fileTime;

    /**
     * 文件存储默认sdcard/record
     */
    public AudioRecoderUtils() {
        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    public AudioRecoderUtils(@NonNull String filePath) {
        this(filePath, 600);
    }

    public AudioRecoderUtils(@NonNull String filePath, int timeSecond) {
        MAX_LENGTH = timeSecond * 1000;
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;


    /**
     * 开始录音 使用amr格式
     * 录音文件
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            String time = DateUtil.format(UnitTime.MILLIONSECOND, System.currentTimeMillis(), DateUtil.DATA_YYYY_MM_DD_HH_MM4);
            filePath = FolderPath + time + ".amr";
            mMediaRecorder.setOutputFile(filePath);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            /* ③准备 */
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            updateMicStatus();
            LogUtil.i(TAG, "startTime:" + startTime);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (null != mUpdateMicStatusTimer) {
            mHandler.removeCallbacks(mUpdateMicStatusTimer);
        }
        if (mMediaRecorder == null){
            return 0L;
        }
        endTime = System.currentTimeMillis();

        // 有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况
        // 捕获异常清理一下就行了
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            if (null != audioStatusUpdateListener) {
                audioStatusUpdateListener.onStop(filePath, fileTime);
            }
            filePath = "";
        } catch (RuntimeException e) {
            if (null != mMediaRecorder) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            filePath = "";
        }
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            if (null != audioStatusUpdateListener){
                audioStatusUpdateListener.onCancel();
            }
        } catch (RuntimeException e) {
            if (null != mMediaRecorder) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
        filePath = "";
        if (null != mUpdateMicStatusTimer) {
            mHandler.removeCallbacks(mUpdateMicStatusTimer);
        }
    }

    private static final WeakHandler mHandler = new WeakHandler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private static final int BASE = 1;
    private static final int SPACE = 500;// 间隔取样时间

    public void setOnAudioStatuListener(OnAudioStatuListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db;// 分贝
            fileTime = System.currentTimeMillis() - startTime;
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener)
                    audioStatusUpdateListener.onUpdate(db, fileTime);
            }
            if (fileTime >= MAX_LENGTH) {
                stopRecord();
            } else {
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
            }
        }
    }
}
