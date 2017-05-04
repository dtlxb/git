package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.adapter.SocialRecordAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.bean.SocialRecordBean;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

/**
 * 直播筛选结果
 */
public class ScreenActivity extends BaseActivity {

    //后台发起直播
    @BindView(R.id.orgLiveLinear)
    LinearLayout orgLiveLinear;
    @BindView(R.id.orgLiveRecycler)
    RecyclerView orgLiveRecycler;
    //录播
    @BindView(R.id.recordLinear)
    LinearLayout recordLinear;
    @BindView(R.id.recordRecycler)
    RecyclerView recordRecycler;

    private SocialLiveAdapter liveAdapter;
    private SocialRecordAdapter recordAdapter;

    private String programme_name;
    private String programme_id;

    private int page = 1;

    @Override
    public int bindLayout() {
        return R.layout.activity_screen;
    }

    @Override
    public void doBusiness(Context mContext) {
        programme_name = getIntent().getStringExtra("programme_name");
        programme_id = getIntent().getStringExtra("programme_id");

        setMyTitle(programme_name, true);

        BaseActivity.initRecycleView(orgLiveRecycler, null);
        orgLiveRecycler.setNestedScrollingEnabled(false);

        BaseActivity.initRecycleView(recordRecycler, null);
        recordRecycler.setNestedScrollingEnabled(false);

        getLiveData(programme_id);
        getRecordData(page, programme_id);
    }

    /**
     * 获取直播列表数据
     */
    private void getLiveData(String programme_id) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_source", "2");
        param.put("programme_id", programme_id);
        param.put("page", "1");
        param.put("rows", "100");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                SocialLiveBean object = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
                if (object.getCode() == 0) {
                    orgLiveLinear.setVisibility(View.VISIBLE);
                    List<SocialLiveData> listData = object.getData();
                    liveAdapter = new SocialLiveAdapter(getActivity(), listData);
                    orgLiveRecycler.setAdapter(liveAdapter);
                } else if (object.getCode() == 1001) {
                    orgLiveLinear.setVisibility(View.GONE);
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
    }

    /**
     * 获取录播列表数据
     */
    private void getRecordData(final int page, String programme_id) {
        final Map<String, String> param = new HashMap<>();
        param.put("programme_id", programme_id);
        param.put("page", page + "");
        param.put("rows", "10");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                SocialRecordBean object = JSONObject.parseObject(responseInfo, SocialRecordBean.class);
                if (object.getCode() == 0) {
                    recordLinear.setVisibility(View.VISIBLE);
                    List<SocialRecordData> recordData = object.getData();
                    recordAdapter = new SocialRecordAdapter(getActivity(), recordData);
                    recordRecycler.setAdapter(recordAdapter);
                } else if (object.getCode() == 1001) {
                    if (page == 1) {
                        recordLinear.setVisibility(View.GONE);
                    }
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
    }

    private ScreenActivity getContext() {
        return ScreenActivity.this;
    }
}
