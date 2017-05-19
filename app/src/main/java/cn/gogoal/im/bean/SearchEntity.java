package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/5/19.
 */

public abstract class SearchEntity<T> {
    public boolean isHeader;
    public T t;
    public String header;

    public SearchEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SearchEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
