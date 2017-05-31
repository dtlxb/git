package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/5/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主Tab 直播列表
 */
public class LiveListFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.rv_live_list)
    RecyclerView rvLiveList;

    @Override
    public int bindLayout() {
        return R.layout.fragment_live_list;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.iniRefresh(refreshLayout);

        getLiveData(1, null);
        getLiveData(2, null);
        getRecordData(null);
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
                SocialLiveBean bean = JSONObject.parseObject(responseInfo, SocialLiveBean.class);

//                if (object.getCode() == 0) {
//                    if (live_source == 1) {
//                        perLiveLinear.setVisibility(View.VISIBLE);
//                        List<SocialLiveData> liveData = object.getData();
//                        liveAdapter = new SocialLiveAdapter(getActivity(), liveData);
//                        perLiveRecycler.setAdapter(liveAdapter);
//                    } else if (live_source == 2) {
//                        orgLiveLinear.setVisibility(View.VISIBLE);
//                        List<SocialLiveData> listData = object.getData();
//                        liveAdapter = new SocialLiveAdapter(getActivity(), listData);
//                        orgLiveRecycler.setAdapter(liveAdapter);
//                    }
//                } else if (object.getCode() == 1001) {
//                    if (live_source == 1) {
//                        perLiveLinear.setVisibility(View.GONE);
//                    } else if (live_source == 2) {
//                        orgLiveLinear.setVisibility(View.GONE);
//                    }
//                } else {
//                    UIHelper.toast(getContext(), R.string.net_erro_hint);
//                    KLog.e(TAG);
//                }
//
//                refreshSocial.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
    }

    /**
     * 获取录播列表数据
     */
    private void getRecordData(String programme_id) {
//
//        recordAdapter.setEnableLoadMore(false);

        final Map<String, String> param = new HashMap<>();
        if (programme_id != null) {
            param.put("programme_id", programme_id);
        }
        param.put("page", String.valueOf(1));
        param.put("rows", "20");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                FileUtil.writeRequestResponse(responseInfo,"录播数据");
//                SocialRecordBean object = JSONObject.parseObject(responseInfo, SocialRecordBean.class);
//                if (object.getCode() == 0) {
//                    recordLinear.setVisibility(View.VISIBLE);
//                    ArrayList<SocialRecordData> data = object.getData();
//                    recordData.addAll(data);
//                    recordAdapter.notifyDataSetChanged();
//                    recordAdapter.setEnableLoadMore(true);
//                    recordAdapter.loadMoreComplete();
//
//                } else if (object.getCode() == 1001) {
//                    if (recordPage == 1) {
//                        recordLinear.setVisibility(View.GONE);
//                    }
//                } else {
//                    UIHelper.toast(getContext(), R.string.net_erro_hint);
//                    KLog.e(TAG);
//                }
//
//                refreshSocial.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
//                if (refreshSocial!=null)
//                    refreshSocial.setRefreshing(false);
//
//                UIHelper.toast(getContext(), R.string.net_erro_hint);
//                KLog.e(TAG);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
    }

}