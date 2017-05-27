package cn.gogoal.im.common.recording;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaManager {
    private static boolean isPause;
    private static GGMediaPlayer ggMediaPlayer;


    /**
     * 播放音乐
     */
   /* public static void playSound(final AudioViewInfo audioViewInfo, MediaPlayer.OnCompletionListener onCompletionListener) {
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
    }*/
    public static void playStop() {

        if (null != ggMediaPlayer) {

            ggMediaPlayer.getGGMediaInterface().audioWillStop();
            ggMediaPlayer.stop();
        }
    }

    public static void playAudio(GGMediaPlayInterface playInterface, String path) {

        if (null != ggMediaPlayer) {
            ggMediaPlayer.getGGMediaInterface().audioWillStop();
            ggMediaPlayer.stop();
        }

        ggMediaPlayer = new GGMediaPlayer();
        ggMediaPlayer.setGGMediaInterface(playInterface);
        ggMediaPlayer.getGGMediaInterface().audioWillPlay();

        try {
            ggMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ggMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    GGMediaPlayer mediaPlayer = (GGMediaPlayer) mp;
                    mediaPlayer.getGGMediaInterface().audioWillEnd();
                }
            });
            ggMediaPlayer.setDataSource(path);
            ggMediaPlayer.prepare();
            ggMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (ggMediaPlayer != null && ggMediaPlayer.isPlaying()) { //正在播放的时候
            ggMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 当前是isPause状态
     */
    public static void resume() {
        if (ggMediaPlayer != null && isPause) {
            ggMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (ggMediaPlayer != null) {
            ggMediaPlayer.release();
            ggMediaPlayer = null;
        }
    }

    private static class GGMediaPlayer extends MediaPlayer {

        GGMediaPlayInterface GGMediaInterface;

        void setGGMediaInterface(GGMediaPlayInterface GGMediaInterface) {
            this.GGMediaInterface = GGMediaInterface;
        }

        GGMediaPlayInterface getGGMediaInterface() {
            return GGMediaInterface;
        }
    }

    public interface GGMediaPlayInterface {
        void audioWillPlay();

        void audioWillEnd();

        void audioWillStop();
    }
}
