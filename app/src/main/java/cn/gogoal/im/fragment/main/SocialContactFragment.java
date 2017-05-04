package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.gogoal.im.adapter.SocialRecordAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BoxScreenData;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.bean.SocialRecordBean;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.widget.PopupWindowHelper;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

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

    private PopupWindowHelper screenHelper;

    @Override
    public int bindLayout() {
        return R.layout.fragment_social_contact;
    }

    @Override
    public void doBusiness(Context mContext) {
        /*setFragmentTitle(R.string.title_live).addAction(new XTitle.TextAction("筛选") {
            @Override
            public void actionClick(View view) {

            }
        });*/

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
                getLiveData(1);
                getLiveData(2);
                getRecordData(1);
            }
        });

        getLiveData(1);
        getLiveData(2);
        getRecordData(1);

        getScreenData();
    }

    @OnClick({R.id.imgFloatAction, R.id.boxScreen})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgFloatAction: //发起直播
                getUserValid();
                break;
            case R.id.boxScreen: //筛选
                if (boxScreen.isChecked()) {
                    screenHelper.showScreenFromRight(relaterTittle);
                    boxScreen.setTextColor(getResColor(R.color.stock_red));
                } else {
                    screenHelper.dismiss();
                    boxScreen.setTextColor(getResColor(R.color.textColor_333333));
                }
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
    private void getRecordData(final int page) {
        final Map<String, String> param = new HashMap<>();
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

    /**
     * 获取筛选列表数据
     */
    private void getScreenData() {

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    List<BoxScreenData> screenData = JSONObject.parseArray(String.valueOf(object.getJSONArray("data")), BoxScreenData.class);
                    if (screenData != null) {
                        showBoxScreen(screenData);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(null, GGOKHTTP.GET_PROGRAMME_GUIDE, ggHttpInterface).startGet();
    }

    /**
     * 设置筛选弹窗
     */
    private void showBoxScreen(List<BoxScreenData> screenData) {
        View dialogBoxScreen = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_box_screen, null);
        screenHelper = new PopupWindowHelper(dialogBoxScreen);

        screenHelper.setMissCallBack(new PopupWindowHelper.PopDismissCallBack() {
            @Override
            public void setBoxScreen() {
                boxScreen.setChecked(false);
                boxScreen.setTextColor(getResColor(R.color.textColor_333333));
            }
        });

        RecyclerView recyScreen = (RecyclerView) dialogBoxScreen.findViewById(R.id.recyScreen);
        initRecycleView(recyScreen, null);
        recyScreen.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyScreen.setAdapter(new BoxScreenAdapter(getActivity(), screenData));
    }

    class BoxScreenAdapter extends CommonAdapter<BoxScreenData, BaseViewHolder> {

        public BoxScreenAdapter(Context context, List<BoxScreenData> list) {
            super(R.layout.item_box_screen, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, final BoxScreenData data, int position) {
            TextView textProName = holder.getView(R.id.textProName);

            textProName.setText(data.getProgramme_name());
        }
    }
}
