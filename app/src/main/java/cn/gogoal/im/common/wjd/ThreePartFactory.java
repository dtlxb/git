package cn.gogoal.im.common.wjd;

import android.content.Context;

/**
 * author wangjd on 2017/3/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 各种三方分享三方登录工厂方法
 */
public class ThreePartFactory {

    Context context;

    //=========================微信============================
    public static ThreePartFactory with(Context context) {
        ThreePartFactory factory=new ThreePartFactory();
        factory.context=context;
        return factory;
    }

    public WechatOperator wechat() {
        return WechatOperator.getInstance(context);
    }


    //===========================支付宝===================================

    //============================微博====================================

}
