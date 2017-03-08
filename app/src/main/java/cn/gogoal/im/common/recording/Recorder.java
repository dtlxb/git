package cn.gogoal.im.common.recording;

/**
 * author wangjd on 2017/3/3 0003.
 * Staff_id 1375
 * phone 18930640263
 */
public class Recorder {

    private float duration;

    private String filePath;

    public Recorder(float duration, String filePath) {
        this.duration = duration;
        this.filePath = filePath;
    }

    public float getDuration() {
        return duration;
    }

    public Recorder setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public Recorder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
