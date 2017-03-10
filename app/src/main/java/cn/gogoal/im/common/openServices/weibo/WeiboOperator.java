package cn.gogoal.im.common.openServices.weibo;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class WeiboOperator {
    private static class WeiboOperatorHolder {
        private static final WeiboOperator INSTANCE = new WeiboOperator();
    }

    public static WeiboOperator getInstance() {
        return WeiboOperatorHolder.INSTANCE;
    }

    private WeiboOperator() {
    }
}
