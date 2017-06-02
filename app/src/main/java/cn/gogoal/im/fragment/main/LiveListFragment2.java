package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.adapter.SocialRecordAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.bean.SocialRecordBean;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

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
    private List<SocialRecordData> liveRecorderDatas;
    private SocialRecordAdapter liveAdapterRecorder;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private int page;

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

        initData();
    }

    private void initData() {
        livePersionalDatas = new ArrayList<>();
        liveAdapterPersional = new SocialLiveAdapter(getActivity(), livePersionalDatas);
        rvLiveListPersional.setAdapter(liveAdapterPersional);

        livePcDatas = new ArrayList<>();
        liveAdapterPc = new SocialLiveAdapter(getActivity(), livePcDatas);
        rvLiveListPc.setAdapter(liveAdapterPersional);

        liveRecorderDatas = new ArrayList<>();
        liveAdapterRecorder = new SocialRecordAdapter(getActivity(), liveRecorderDatas);
        rvLiveListRecorder.setAdapter(liveAdapterRecorder);
    }

    public void request(int refreshType, String keyword) {
        getLiveData(refreshType, 1, keyword);
        getLiveData(refreshType, 2, keyword);
        getRecordData(refreshType, keyword);
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

                KLog.e(responseInfo);

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    SocialLiveBean bean = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
                    List<SocialLiveData> datas = bean.getData();

                    if (live_source == 1) {
                        livePersionalDatas.addAll(datas);
                        liveAdapterPersional.notifyDataSetChanged();
                    } else {
                        livePcDatas.addAll(datas);
                        liveAdapterPc.notifyDataSetChanged();
                    }

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
        param.put("page", String.valueOf(page));
        param.put("rows", "20");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    ArrayList<SocialRecordData> recordDatas =
                            JSONObject.parseObject(responseInfo, SocialRecordBean.class).getData();
                    liveRecorderDatas.addAll(recordDatas);
                    liveAdapterRecorder.notifyDataSetChanged();

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
        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
    }

}