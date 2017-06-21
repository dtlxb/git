package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AdvisersAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/5/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :专属顾问
 */
public class MyAdvisersActivity extends BaseActivity {
    @BindView(R.id.rv_advisers)
    RecyclerView rvAdvisers;

    @Override
    public int bindLayout() {
        return R.layout.activity_my_advisers;
    }

    @Override
    public void doBusiness(final Context mContext) {
        setMyTitle("我的专属顾问",true);

        BaseActivity.initRecycleView(rvAdvisers, 0);

        UserUtils.getAdvisers(new Impl<String>() {
            @Override
            public void response(int code, String data) {
                if (code==0){
                    AdvisersAdapter advisersAdapter = new AdvisersAdapter(
                            MyAdvisersActivity.this,
                            JSONObject.parseArray(data, Advisers.class),
                            11* AppDevice.getWidth(mContext)/100);
                    rvAdvisers.setAdapter(advisersAdapter);
                }
            }
        });
    }

}
