package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :直播.
 */
public class SocialContactFragment extends BaseFragment {

    @BindView(R.id.swiperefresh_social)
    SwipeRefreshLayout refreshSocial;
    //个人发起直播
    @BindView(R.id.perLiveLinear)
    LinearLayout perLiveLinear;
    @BindView(R.id.perLiveRecycler)
    RecyclerView perLiveRecycler;
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

    private SocialLiveAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_social_contact;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_live).addAction(new XTitle.TextAction("筛选") {
            @Override
            public void actionClick(View view) {

            }
        });

        BaseActivity.iniRefresh(refreshSocial);

        BaseActivity.initRecycleView(perLiveRecycler, 0);
        perLiveRecycler.setNestedScrollingEnabled(false);

        BaseActivity.initRecycleView(orgLiveRecycler, 0);
        orgLiveRecycler.setNestedScrollingEnabled(false);

        BaseActivity.initRecycleView(recordRecycler, 0);
        recordRecycler.setNestedScrollingEnabled(false);

        refreshSocial.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLiveData(1);
                getLiveData(2);
            }
        });

        getLiveData(1);
        getLiveData(2);
    }

    @OnClick({R.id.imgFloatAction})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgFloatAction: //发起直播
                getUserValid();
                break;
        }
    }

    /*
    * 能否发起直播
    * */
    private void getUserValid() {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 1) {
                        if (data.getString("live_id") != null) {
                            Intent intent = new Intent(getContext(), LiveActivity.class);
                            intent.putExtra("live_id", data.getString("live_id"));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(getActivity(), CreateLiveActivity.class));
                        }
                    } else {
                        DialogHelp.getMessageDialog(getContext(), "您暂时没有权限直播，请联系客服申请！").show();
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
        new GGOKHTTP(param, GGOKHTTP.VIDEO_MOBILE, ggHttpInterface).startGet();
    }

    /**
     * 获取直播列表数据
     */
    private void getLiveData(final int live_source) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_source", live_source + "");
        param.put("page", "1");
        param.put("rows", "100");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                SocialLiveBean object = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
                if (object.getCode() == 0) {
                    if (live_source == 1) {
                        perLiveLinear.setVisibility(View.VISIBLE);
                        List<SocialLiveData> listData = object.getData();
                        adapter = new SocialLiveAdapter(getActivity(), listData);
                        perLiveRecycler.setAdapter(adapter);
                    } else if (live_source == 2) {
                        orgLiveLinear.setVisibility(View.VISIBLE);
                        List<SocialLiveData> listData = object.getData();
                        adapter = new SocialLiveAdapter(getActivity(), listData);
                        orgLiveRecycler.setAdapter(adapter);
                    }
                } else if (object.getCode() == 1001) {
                    if (live_source == 1) {
                        perLiveLinear.setVisibility(View.GONE);
                    } else if (live_source == 2) {
                        orgLiveLinear.setVisibility(View.GONE);
                    }
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }

                refreshSocial.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                refreshSocial.setRefreshing(false);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
    }
}
