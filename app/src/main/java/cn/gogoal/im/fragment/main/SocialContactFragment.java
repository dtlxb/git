package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.adapter.SocialRecordAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.bean.SocialRecordBean;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :直播.
 */
public class SocialContactFragment extends BaseFragment {

    @BindView(R.id.relaterTittle)
    RelativeLayout relaterTittle;

    @BindView(R.id.boxScreen)
    CheckBox boxScreen;

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

    private SocialLiveAdapter liveAdapter;
    private SocialRecordAdapter recordAdapter;

    private ArrayList<SocialRecordData> recordData = new ArrayList<SocialRecordData>();

    private String programme_name = null;

    private int recordPage = 1;

    @Override
    public int bindLayout() {
        return R.layout.fragment_social_contact;
    }

    @Override
    public void doBusiness(Context mContext) {

        BaseActivity.iniRefresh(refreshSocial);

        BaseActivity.initRecycleView(perLiveRecycler, null);
        perLiveRecycler.setNestedScrollingEnabled(false);

        BaseActivity.initRecycleView(orgLiveRecycler, null);
        orgLiveRecycler.setNestedScrollingEnabled(false);

        BaseActivity.initRecycleView(recordRecycler, null);
        recordRecycler.setNestedScrollingEnabled(false);

        refreshSocial.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recordPage = 1;

                getLiveData(1, null);
                getLiveData(2, null);
                getRecordData(null);
            }
        });

        ((MainActivity) getActivity()).setCloseCallBack(new MainActivity.drawerCloseCallBack() {
            @Override
            public void closeDrawer() {
                boxScreen.setChecked(false);
                boxScreen.setTextColor(getResColor(R.color.textColor_333333));
            }
        });

        recordAdapter = new SocialRecordAdapter(getActivity(), recordData);
        recordRecycler.setAdapter(recordAdapter);

        //上拉加载
        recordAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (recordPage <= 10) {
                    recordPage++;
                    recordAdapter.loadMoreEnd(false);
                    getRecordData(programme_name);
                } else {
                    recordAdapter.loadMoreEnd(true);
                    recordAdapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);
                }
                recordAdapter.loadMoreComplete();
            }
        }, recordRecycler);

        getLiveData(1, null);
        getLiveData(2, null);
        getRecordData(null);
    }

    @Subscriber(tag = "setScreen")
    private void setScreen(BaseMessage s) {
        programme_name = s.getMsg();

        //perLiveLinear.setVisibility(View.GONE);

        recordPage = 1;
        recordData.clear();

        getLiveData(1, programme_name);
        getLiveData(2, programme_name);
        getRecordData(programme_name);
    }

    @OnClick({R.id.imgFloatAction, R.id.boxScreen})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgFloatAction: //发起直播
                getUserValid();
                break;
            case R.id.boxScreen: //筛选
                if (boxScreen.isChecked()) {
                    ((MainActivity) getActivity()).openMenu();
                    boxScreen.setTextColor(getResColor(R.color.stock_red));
                } else {
                    ((MainActivity) getActivity()).closeMenu();
                    boxScreen.setTextColor(getResColor(R.color.textColor_333333));
                }
                break;
        }
    }

    /**
     * 能否发起直播
     */
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
    private void getLiveData(final int live_source, String programme_id) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_source", live_source + "");
        if (programme_id != null) {
            param.put("programme_id", programme_id);
        }
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
                        List<SocialLiveData> liveData = object.getData();
                        liveAdapter = new SocialLiveAdapter(getActivity(), liveData);
                        perLiveRecycler.setAdapter(liveAdapter);
                    } else if (live_source == 2) {
                        orgLiveLinear.setVisibility(View.VISIBLE);
                        List<SocialLiveData> listData = object.getData();
                        liveAdapter = new SocialLiveAdapter(getActivity(), listData);
                        orgLiveRecycler.setAdapter(liveAdapter);
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

    /**
     * 获取录播列表数据
     */
    private void getRecordData(String programme_id) {

        recordAdapter.setEnableLoadMore(false);

        final Map<String, String> param = new HashMap<>();
        if (programme_id != null) {
            param.put("programme_id", programme_id);
        }
        param.put("page", String.valueOf(recordPage));
        param.put("rows", "10");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                SocialRecordBean object = JSONObject.parseObject(responseInfo, SocialRecordBean.class);
                if (object.getCode() == 0) {
                    recordLinear.setVisibility(View.VISIBLE);
                    ArrayList<SocialRecordData> data = object.getData();
                    recordData.addAll(data);
                    recordAdapter.notifyDataSetChanged();
                    recordAdapter.setEnableLoadMore(true);
                    recordAdapter.loadMoreComplete();

                } else if (object.getCode() == 1001) {
                    if (recordPage == 1) {
                        recordLinear.setVisibility(View.GONE);
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
        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
    }

}
