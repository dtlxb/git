package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.LiveListAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.LiveListItemBean;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.bean.SocialRecordBean;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.AppConst;
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

    private List<LiveListItemBean> liveDatas;//全部

    private List<LiveListItemBean> persionalDatas;  //个人

    private List<LiveListItemBean> pcDatas;         //后台发起

    private List<LiveListItemBean> recorderDatas;         //录播发起

    private LiveListAdapter liveListAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_live_list;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.iniRefresh(refreshLayout);

        rvLiveList.setLayoutManager(new LinearLayoutManager(mContext));

        liveDatas = new ArrayList<>();
        persionalDatas = new ArrayList<>();
        pcDatas = new ArrayList<>();
        recorderDatas = new ArrayList<>();

        liveListAdapter = new LiveListAdapter(mContext, liveDatas);

        rvLiveList.setAdapter(liveListAdapter);

        request(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                liveDatas.clear();
                request(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void request(int refreshType) {
        getLiveData(refreshType, 1, null);

        getLiveData(refreshType, 2, null);

        getRecordData(refreshType, null);
    }

    /**
     * 获取直播列表数据
     */
    private void getLiveData(final int refreshType, final int live_source, String programme_id) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_source", live_source + "");

        if (programme_id != null) {
            param.put("programme_id", programme_id);
        }

        param.put("page", "1");
        param.put("rows", "200");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    SocialLiveBean bean = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
                    List<SocialLiveData> datas = bean.getData();

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        if (live_source == 1) {
                            persionalDatas.clear();
                        } else {
                            pcDatas.clear();
                        }
                    }

                    for (SocialLiveData liveData : datas) {
                        LiveListItemBean listItemBean =
                                new LiveListItemBean(
                                        liveData.getLive_time_start(),
                                        liveData.getLive_large_img(),
                                        liveData.getVideo_name(),
                                        liveData.getAnchor().getFace_url(),
                                        liveData.getAnchor().getAnchor_name(),
                                        liveData.getProgramme_name(),
                                        liveData.getLive_id(),
                                        liveData.getLive_status());

                        if (live_source == 1) {
                            persionalDatas.add(listItemBean);
                        } else {
                            pcDatas.add(listItemBean);
                        }
                    }

                    liveDatas.addAll(0, persionalDatas);

                    liveDatas.addAll(pcDatas);

                    liveListAdapter.notifyDataSetChanged();

                } else if (code == 1001) {
                    //没有请求到直播数据
                } else {
                    //请求出错
                }
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
    private void getRecordData(final int refreshType, String programme_id) {
        final Map<String, String> param = new HashMap<>();
        if (programme_id != null) {
            param.put("programme_id", programme_id);
        }
        param.put("page", String.valueOf(1));
        param.put("rows", "20");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        recorderDatas.clear();
                    }

                    ArrayList<SocialRecordData> recordDatas =
                            JSONObject.parseObject(responseInfo, SocialRecordBean.class).getData();
                    for (SocialRecordData data : recordDatas) {
                        recorderDatas.add(new LiveListItemBean(
                                data.getUpdate_time(),
                                data.getVideo_img_url(),
                                data.getVideo_name(),
                                data.getAnchor().getFace_url(),
                                data.getAnchor_name(),
                                data.getProgramme_name(),
                                data.getVideo_id(),-1));
                    }

                    liveDatas.addAll(recorderDatas);

                    liveListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
    }

}