package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SearchListAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.bean.group.GroupCollectionData;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.bean.SearchBean;
import cn.gogoal.im.bean.SearchData;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/5/18.
 */

public class IMSearchLocalActivity extends BaseActivity {

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.recycler_result)
    RecyclerView recyclerResult;

    @BindView(R.id.layout_no_result)
    LinearLayout layoutUnSearch;

    @BindArray(R.array.search_flag)
    String[] searchFlagArray;

    @BindView(R.id.rv_history)
    RecyclerView rvHistory;

    @BindView(R.id.rv_flag_search)
    RecyclerView rvFlagSearch;

    @BindView(R.id.tips_layout)
    RelativeLayout tips_layout;

    private List<SearchData> dataList;
    private List<SearchData> searchDatas;
    private List<SearchData> personData;
    private List<SearchData> groupsData;
    private SearchListAdapter searchListAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();

        rvFlagSearch.setAdapter(new CommonAdapter<BaseIconText<Integer, String>, BaseViewHolder>(R.layout.item_search_flag, getFlagData()) {
            @Override
            protected void convert(BaseViewHolder holder, BaseIconText<Integer, String> data, int position) {
                holder.setImageResource(R.id.img_search_item, data.getIamge());
                holder.setText(R.id.tv_search_item, data.getText());
            }
        });
        //获取本地所有对话
        getDatas();

        //输入框监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDatas.clear();
                groupsData.clear();
                personData.clear();
                if (!TextUtils.isEmpty(newText)) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (!dataList.get(i).isHeader) {
                            String conversationName = dataList.get(i).t.getNickname();
                            int chatType = dataList.get(i).t.getChatType();
                            if (conversationName.contains(newText)) {
                                if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                                    personData.add(dataList.get(i));
                                } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE || chatType == AppConst.IM_CHAT_TYPE_STOCK_SQUARE) {
                                    groupsData.add(dataList.get(i));
                                }
                            }
                        }
                    }
                    if (personData.size() > 0 || groupsData.size() > 0) {
                        if (personData.size() > 0) {
                            SearchData searchData1 = new SearchData(true, "朋友");
                            searchDatas.add(searchData1);
                            searchDatas.addAll(personData);
                        }
                        if (groupsData.size() > 0) {
                            SearchData searchData2 = new SearchData(true, "群组");
                            searchDatas.add(searchData2);
                            searchDatas.addAll(groupsData);
                        }

                        tips_layout.setVisibility(View.GONE);
                        rvHistory.setVisibility(View.VISIBLE);
                        searchListAdapter.notifyDataSetChanged();
                    } else {
                        tips_layout.setVisibility(View.VISIBLE);
                        rvHistory.setVisibility(View.GONE);
                    }
                } else {
                    tips_layout.setVisibility(View.VISIBLE);
                    rvHistory.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void getDatas() {
        dataList = new ArrayList<>();
        searchDatas = new ArrayList<>();
        personData = new ArrayList<>();
        groupsData = new ArrayList<>();
        searchListAdapter = new SearchListAdapter(IMSearchLocalActivity.this, searchDatas);
        initRecycleView(rvHistory, R.drawable.shape_divider_1px);
        rvHistory.setAdapter(searchListAdapter);
        //联系人列表

        List<UserBean> userBeanList = new ArrayList<>();
        userBeanList.addAll(UserInfoUtils.getAllUserInfo());

        SearchData searchData = new SearchData(true, "朋友");
        searchData.setParentPosition(0);
        dataList.add(searchData);
        for (int i = 0; i < userBeanList.size(); i++) {
            SearchBean searchBean = new SearchBean(userBeanList.get(i).getAvatar(), userBeanList.get(i).getNickname(), "", userBeanList.get(i).getConv_id(), AppConst.IM_CHAT_TYPE_SINGLE);
            SearchData searchContactsData = new SearchData(searchBean, userBeanList.size());
            searchContactsData.setChildPosition(i);
            dataList.add(searchContactsData);
        }

        //群组列表
        getGroupList();
    }

    //初始化标题
    private void iniTitle() {
        setMyTitle("查找聊天", true);
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        et.setTextSize(14);
        et.setHint("搜索");
    }

    //默认查询的东西
    private List<BaseIconText<Integer, String>> getFlagData() {
        /*int[] flagIcons = {R.mipmap.img_search_flag_contact, R.mipmap.img_search_flag_group, R.mipmap.home_bottom_tab_icon_message_normal,
                R.mipmap.img_search_flag_live, R.mipmap.img_search_flag_yanwang, R.mipmap.img_search_flag_pinpai};*/
        int[] flagIcons = {R.mipmap.img_search_flag_contact, R.mipmap.img_search_flag_group};
        List<BaseIconText<Integer, String>> list = new ArrayList<>();

        for (int i = 0; i < flagIcons.length; i++) {
            list.add(new BaseIconText<>(flagIcons[i], searchFlagArray[i]));
        }
        return list;
    }

    //收藏群列表
    public void getGroupList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {

                    List<GroupData> data =
                            JSONObject.parseObject(responseInfo, GroupCollectionData.class).getData();

                    SearchData searchData = new SearchData(true, "群组");
                    searchData.setParentPosition(1);
                    dataList.add(searchData);
                    for (int i = 0; i < data.size(); i++) {
                        SearchBean searchBean = new SearchBean(data.get(i).getAttr().getAvatar(), data.get(i).getName(),
                                data.get(i).getAttr().getIntro(), data.get(i).getConv_id(), AppConst.IM_CHAT_TYPE_SQUARE);
                        SearchData searchGroupData = new SearchData(searchBean, data.size());
                        searchGroupData.setChildPosition(i);
                        dataList.add(searchGroupData);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_LIST, ggHttpInterface).startGet();
    }

}