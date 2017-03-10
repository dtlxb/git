package cn.gogoal.im.common.openServices.qq;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class QQoperator {
    private static final QQoperator ourInstance = new QQoperator();

    public static QQoperator getInstance() {
        return ourInstance;
    }

    private QQoperator() {
    }
}
