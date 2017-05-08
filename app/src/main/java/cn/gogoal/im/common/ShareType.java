package cn.gogoal.im.common;

/**
 * author wangjd on 2017/5/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public enum ShareType {
    TEXT(1), IAMGE(12), WEB(23), LIVE(34);
    private int type;

    ShareType(int type) {
        this.type = type;
    }
}
