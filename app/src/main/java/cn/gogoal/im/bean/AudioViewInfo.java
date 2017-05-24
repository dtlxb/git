package cn.gogoal.im.bean;

import android.view.View;

/**
 * Created by huangxx on 2017/5/24.
 */

public class AudioViewInfo {
    private View view;
    private String path;
    private int Type;

    public AudioViewInfo(View view, String path, int type) {
        this.view = view;
        this.path = path;
        Type = type;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
