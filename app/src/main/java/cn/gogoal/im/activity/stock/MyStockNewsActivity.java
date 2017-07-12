package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import butterknife.BindArray;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.stock.news_report.MyStockNewsFragment;
import cn.gogoal.im.fragment.stock.news_report.MyStockNoticesFragment;
import cn.gogoal.im.fragment.stock.news_report.MyStockReportFragment;


/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:自选股中的[新闻，公告,研报]
 */

public class MyStockNewsActivity extends BaseActivity {
    @BindArray(R.array.mystock_news_title)
    String[] newsTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_mystock_news;
    }

    @Override
    public void doBusiness(Context mContext) {
        //展示tab的index====0=新闻；1=公告；2=研报

        final int index = getIntent().getIntExtra("showTabIndex", 0);

        setMyTitle("自选股" + newsTitle[index], true);

        getNewsGroupId(new Impl<String>() {
            @Override
            public void response(int code, String data) {
                if (code == 0) {
                    switch (index) {
                        case 0:
                            shownewsFragment(
                                    MyStockNewsFragment.newInstance(
                                            Integer.parseInt(data)));
                            break;
                        case 1:
                            shownewsFragment(
                                    MyStockNoticesFragment.newInstance(
                                            Integer.parseInt(data)));
                            break;
                        case 2:
                            shownewsFragment(
                                    MyStockReportFragment.newInstance(
                                            Integer.parseInt(data)));
                            break;
                    }
                } else {
                }
            }
        });
    }

    private void shownewsFragment(Fragment showFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!showFragment.isAdded()) {
            transaction.add(R.id.layout_news_content, showFragment);
        }
        transaction.show(showFragment);
        transaction.commit();
    }

    /**
     * 获取自选股股票
     */
    private void getNewsGroupId(final Impl<String> callback) {

        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("get_sum", String.valueOf(1));
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_ID, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject responseJson = JSONObject.parseObject(responseInfo);
                int code = responseJson.getIntValue("code");
                if (code == 0) {
                    if (responseJson.containsKey("data")) {
                        JSONObject data = (JSONObject) responseJson.getJSONArray("data").get(0);
                        if (callback != null)
                            callback.response(Impl.RESPON_DATA_SUCCESS, data.getString("group_id"));
                    } else {//data 字段丢失
                        if (callback != null)
                            callback.response(Impl.RESPON_DATA_ERROR, "data non null");
                    }
                } else {
                    if (callback != null)
                        callback.response(Impl.RESPON_DATA_ERROR,
                                responseJson.getString("message"));
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callback != null)
                    callback.response(Impl.RESPON_DATA_ERROR, msg);
            }
        }).startGet();
    }
}
