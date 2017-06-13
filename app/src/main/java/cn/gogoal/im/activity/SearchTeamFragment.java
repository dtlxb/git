//package cn.gogoal.im.activity;
//
//
//import android.content.Context;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//
//import org.simple.eventbus.Subscriber;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import cn.gogoal.im.R;
//import cn.gogoal.im.adapter.RecommendAdapter;
//import cn.gogoal.im.base.BaseFragment;
//import cn.gogoal.im.bean.group.GroupCollectionData;
//import cn.gogoal.im.bean.group.GroupData;
//import cn.gogoal.im.common.AppConst;
//import cn.gogoal.im.common.AppDevice;
//import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
//import cn.gogoal.im.common.UIHelper;
//import cn.gogoal.im.common.UserUtils;
//import cn.gogoal.im.ui.NormalItemDecoration;
//import cn.gogoal.im.ui.view.XLayout;
//
//public class SearchTeamFragment extends BaseFragment {
//
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//
//    @BindView(R.id.xLayout)
//    XLayout xLayout;
//
//    private RecommendAdapter adapter;
//
//    private int loadType = AppConst.REFRESH_TYPE_FIRST;
//
//    private ArrayList<GroupData> dataBeanList;
//    private List<String> groupMembers;
//
//    @Override
//    public int bindLayout() {
//        return R.layout.layout_normal_list_without_refresh;
//    }
//
//    @Override
//    public void doBusiness(Context mContext) {
//
//        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//
//        dataBeanList = new ArrayList<>();
//        adapter = new RecommendAdapter(mContext,dataBeanList);
//        groupMembers = new ArrayList<>();
//
//        TextView headView = new TextView(mContext);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        headView.setGravity(Gravity.CENTER_VERTICAL);
//        headView.setPadding(AppDevice.dp2px(mContext, 16), AppDevice.dp2px(mContext, 10), 0,
//                AppDevice.dp2px(mContext, 9));
//        headView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        headView.setTextColor(getResColor(R.color.textColor_333333));
//        headView.setText("推荐");
//        headView.setLayoutParams(params);
//        adapter.addHeaderView(headView);
//
//        recyclerView.setAdapter(adapter);
//
//        getRecommendGroup(AppConst.REFRESH_TYPE_FIRST, "");
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getRecommendGroup(AppConst.REFRESH_TYPE_SWIPEREFRESH, "");
//    }
//
//    @Subscriber(tag = "SEARCH_TEAM_TAG")
//    void searchTeam(String keyWord) {
//        getRecommendGroup(AppConst.REFRESH_TYPE_PARENT_BUTTON, keyWord);
//    }
//
//    private void getRecommendGroup(final int loadType, final String keyword) {
//        if (loadType == AppConst.REFRESH_TYPE_FIRST) {
//            xLayout.setStatus(XLayout.Loading);
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("token", UserUtils.getToken());
//        if (!TextUtils.isEmpty(keyword)) {
//            map.put("keyword", keyword);
//        }
//        map.put("is_recommend", String.valueOf(TextUtils.isEmpty(keyword)));
//
//        new GGOKHTTP(map, GGOKHTTP.SEARCH_GROUP, new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
//                    dataBeanList.clear();
//                    GroupCollectionData groupCollectionData = JSONObject.parseObject(responseInfo, GroupCollectionData.class);
//                    if (null != groupCollectionData.getData()) {
//                        dataBeanList.addAll(groupCollectionData.getData());
//                        adapter.notifyDataSetChanged();
//                        xLayout.setStatus(XLayout.Success);
//                    }
//                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
//                    xLayout.setStatus(XLayout.Empty);
//                    xLayout.setEmptyText(String.format(getString(R.string.str_result), keyword) + "群组");
//                    UIHelper.toastResponseError(getActivity(), responseInfo);
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
//                    @Override
//                    public void onReload(View v) {
//                        getRecommendGroup(AppConst.REFRESH_TYPE_SWIPEREFRESH,
//                                loadType == AppConst.REFRESH_TYPE_PARENT_BUTTON ? keyword : "");
//                    }
//                });
//
//                UIHelper.toastError(getActivity(), msg, xLayout);
//            }
//        }).startGet();
//    }
//
//}
