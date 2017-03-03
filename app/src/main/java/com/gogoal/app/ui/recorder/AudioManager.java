package com.gogoal.app.ui.recorder;

import android.media.MediaRecorder;

import com.gogoal.app.base.MyApp;
import com.gogoal.app.common.MD5Utils;

import java.io.File;
import java.io.IOException;

public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private boolean isPrepared;

    private static AudioManager mInstance;

    public static AudioManager getmInstance(String dir){
        if(mInstance == null) {
            synchronized (AudioManager.class){
                if(mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public AudioManager(String dir) {
        this.mDir = dir;
    }

    public AudioStateListener mListener;

    /**
     * 回调准备完毕
     */
    public interface AudioStateListener{
        void prepareDown();
    }

    public void setOnAudioStateListener(AudioStateListener listener) {
        mListener = listener;
    }

    /**
     * 准备
     */
    public void prepareAudio(){

        try {
            isPrepared = false;

            File dir = new File(mDir);
            String fileName = generateFileName();
            File file = new File(dir,fileName);

            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频的格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            //设置音频的编码为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //准备录音
            mMediaRecorder.prepare();
            //开始录音
            mMediaRecorder.start();
            //准备结束
            isPrepared = true;
            if(mListener != null) {
                mListener.prepareDown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件的名称
     * @return
     */
    private String generateFileName() {
        return MD5Utils.getMD5EncryptyString(MyApp.getContext().getPackageName()+"_"+System.currentTimeMillis())+".amr";
    }

    /**
     * 获得音量等级
     */
    public int getVoiceLevel(int maxLevel){
        if(isPrepared){
            try{
                //mMediaRecorder.getMaxAmplitude() 范围:1-2^15之间
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 1;
    }

    /**
     * 释放
     */
    public void release(){
//        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    /**
     * 取消
     */
    public void cancel(){
        release();
        //删除文件
        if(mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }

    /**
     * 获取当前文件存储路径
     * @return
     */
    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }
}
