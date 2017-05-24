package cn.gogoal.im.common.recording;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.AudioViewInfo;

public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private static List<AudioViewInfo> infoList = new ArrayList<>();

    /**
     * 播放音乐
     *
     * @param audioViewInfo;
     * @param onCompletionListener;
     */
    public static void playSound(final AudioViewInfo audioViewInfo, MediaPlayer.OnCompletionListener onCompletionListener) {
        infoList.add(audioViewInfo);
        if (infoList.size() > 2) {
            infoList.remove(0);
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    clearAime(audioViewInfo);
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        if (null != infoList && infoList.size() > 1 &&
                !infoList.get(0).getPath().equals(infoList.get(1).getPath())) {
            clearAime(infoList.get(0));
        }

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(audioViewInfo.getPath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 当前是isPause状态
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 暂停动画
     */
    public static void clearAime(AudioViewInfo info) {
        if (info.getType() == 1) {
            info.getView().setBackgroundResource(R.mipmap.right_voice_anime3);
        } else if (info.getType() == 0) {
            info.getView().setBackgroundResource(R.mipmap.left_voice_anime3);
        } else {

        }
    }
}
