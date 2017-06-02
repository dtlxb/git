package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.SocialLiveData;

/**
 * author wangjd on 2017/5/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主Tab 直播列表
 */
public class LiveListFragment2 extends BaseFragment {

    @BindView(R.id.rv_live_list_persional)
    RecyclerView rvLiveListPersional;
    private List<SocialLiveData> livePersionalDatas;
    private SocialLiveAdapter liveAdapterPersional;

    @BindView(R.id.rv_live_list_pc)
    RecyclerView rvLiveListPc;
    private List<SocialLiveData> livePcDatas;
    private SocialLiveAdapter liveAdapterPc;

    @BindView(R.id.rv_live_list_recorder)
    RecyclerView rvLiveListRecorder;
    private List<SocialLiveData> liveRecorderDatas;
    private SocialLiveAdapter liveAdapterRecorder;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

//    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout refreshLayout;
//
//    @BindView(R.id.rv_live_list)
//    RecyclerView rvLiveList;
//
//    private List<LiveListItemBean> liveDatas;//全部
//
//    private List<LiveListItemBean> persionalDatas;  //个人
//
//    private List<LiveListItemBean> pcDatas;         //后台发起
//
//    private List<LiveListItemBean> recorderDatas;         //录播发起
//
//    private LiveListAdapter liveListAdapter;
//
//    private String keyword;//分类关键词
//
//    private int page;

    @Override
    public int bindLayout() {
        return R.layout.fragment_live_list_2;
    }

    @Override
    public void doBusiness(Context mContext) {

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

//        BaseActivity.iniRefresh(refreshLayout);

//        rvLiveList.setLayoutManager(
//                new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//
//        liveDatas = new ArrayList<>();
//        persionalDatas = new ArrayList<>();
//        pcDatas = new ArrayList<>();
//        recorderDatas = new ArrayList<>();
//
//        liveListAdapter = new LiveListAdapter(getActivity(), liveDatas);
//
//        rvLiveList.setAdapter(liveListAdapter);
//
//        request(AppConst.REFRESH_TYPE_FIRST, keyword);
//
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                liveDatas.clear();
//                request(AppConst.REFRESH_TYPE_SWIPEREFRESH, keyword);
//                refreshLayout.setRefreshing(false);
//            }
//        });
//
//        liveListAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                if (page <= 50) {
//                    page++;
//                    request(AppConst.REFRESH_TYPE_LOAD_MORE,keyword);
//                } else {
//                    liveListAdapter.loadMoreEnd(true);
//                    liveListAdapter.setEnableLoadMore(false);
//                    UIHelper.toast(getActivity(),"没有更多数据");
//
//                }
//                liveListAdapter.loadMoreComplete();
//            }
//        },rvLiveList);
    }

//    public void request(int refreshType, String keyword) {
//        liveListAdapter.setEnableLoadMore(false);
//
//        if (refreshType == AppConst.REFRESH_TYPE_PARENT_BUTTON) {//做筛选，先清空
//            recorderDatas.clear();
//            pcDatas.clear();
//            persionalDatas.clear();
//
//            liveDatas.clear();
//
//            this.keyword = keyword;
//        }
//
//        getLiveData(refreshType, 1, keyword);
//
//        getLiveData(refreshType, 2, keyword);
//
//        getRecordData(refreshType, keyword);
//    }
//
//    /**
//     * 获取直播列表数据
//     */
//    private void getLiveData(final int refreshType, final int live_source, String programme_id) {
//        final Map<String, String> param = new HashMap<>();
//        param.put("token", UserUtils.getToken());
//        param.put("live_source", live_source + "");
//
//        if (programme_id != null) {
//            param.put("programme_id", programme_id);
//        }
//
//        param.put("page", "1");
//        param.put("rows", "200");
//
//        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//
//                KLog.e(responseInfo);
//
//                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
//
//                if (code == 0) {
//                    SocialLiveBean bean = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
//                    List<SocialLiveData> datas = bean.getData();
//
//                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
//                        if (live_source == 1) {
//                            persionalDatas.clear();
//                        } else {
//                            pcDatas.clear();
//                        }
//                    }
//
//                    for (SocialLiveData liveData : datas) {
//                        LiveListItemBean listItemBean =
//                                new LiveListItemBean(
//                                        live_source,
//                                        liveData.getLive_status(),
//                                        liveData.getLive_id(),
//                                        liveData.getLive_large_img(),
//                                        liveData.getVideo_name(),
//                                        liveData.getAnchor().getFace_url(),
//                                        liveData.getAnchor().getAnchor_name(),
//                                        liveData.getProgramme_name(),
//                                        liveData.getLive_time_start(),
//                                        liveData.getPlay_base(),
//                                        liveData.getAuth() != 1
//                                );
//
//                        listItemBean.setNeedOrder(liveData.getOrder_status() != 0);
//
//                        if (live_source == 1) {
//                            persionalDatas.add(listItemBean);
//                        } else {
//                            pcDatas.add(listItemBean);
//                        }
//                    }
//
//                    liveListAdapter.notifyItemRangeRemoved(0,liveListAdapter.getItemCount());
//
//                    liveDatas.addAll(0, persionalDatas);
//
//                    liveDatas.addAll(pcDatas);
//
//                    liveListAdapter.notifyDataSetChanged();
//
//                } else if (code == 1001) {
//                    //没有请求到直播数据
//                } else {
//                    //请求出错
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
//    }
//
//    /**
//     * 获取录播列表数据
//     */
//    private void getRecordData(final int refreshType, String programme_id) {
//        final Map<String, String> param = new HashMap<>();
//        if (programme_id != null) {
//            param.put("programme_id", programme_id);
//        }
//        param.put("page", String.valueOf(page));
//        param.put("rows", "20");
//
//        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//
//                KLog.e(responseInfo);
//
//                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
//                if (code == 0) {
//
//                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
//                        recorderDatas.clear();
//                    }
//
//                    ArrayList<SocialRecordData> recordDatas =
//                            JSONObject.parseObject(responseInfo, SocialRecordBean.class).getData();
//                    for (SocialRecordData data : recordDatas) {
//                        LiveListItemBean listItemBean = new LiveListItemBean(
//                                2, // 目前回放视频只能是后端发起
//                                -1,// 视频状态，回放视频是-1
//                                data.getVideo_id(),
//                                data.getVideo_img_url(),
//                                data.getVideo_name(),
//                                data.getAnchor().getFace_url(),
//                                data.getAnchor().getAnchor_name(),
//                                data.getProgramme_name(),
//                                data.getUpdate_time(),
//                                data.getPlay_base(),
//                                data.getAuth() == 0);
//                        recorderDatas.add(listItemBean);
//                    }
//
//                    liveListAdapter.notifyItemRangeRemoved(0,liveListAdapter.getItemCount());
//
//                    liveDatas.addAll(recorderDatas);
//
//                    liveListAdapter.setEnableLoadMore(true);
//                    liveListAdapter.loadMoreComplete();
//
//                    liveListAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
//    }

}