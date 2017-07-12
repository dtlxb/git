package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.RecommendAdapter;
import cn.gogoal.im.adapter.SearchPersionResultAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.group.GroupCollectionData;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

/**
 * Created by huangxx on 2017/3/28.
 */

public class SearchPersonSquareActivity extends BaseActivity {

    @BindArray(R.array.searchTitle)
    String[] searchTitle;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.layout_2search)
    AppCompatEditText layout2search;

    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;

    private ArrayList<ContactBean> searchResultList;
    private SearchPersionResultAdapter persionAdapter;

    private ArrayList<GroupData> groupDatas;
    private RecommendAdapter groupdAdapter;

    private int page = 1;

    private int searchIndex;//搜索类型

    @Override
    public int bindLayout() {
        return R.layout.activity_search_person_square;
    }

    @Override
    public void doBusiness(Context mContext) {
        searchIndex = getIntent().getIntExtra("search_index", 0);
        setMyTitle(searchIndex == 0 ? "搜索好友" : "搜索群", true);

        initData();
        //一些监听
        iniListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchIndex == 1) {
            getRecommendGroup(AppConst.REFRESH_TYPE_FIRST, null);
        }
    }

    //初始化数据
    private void initData() {
        rvSearchResult.addItemDecoration(new NormalItemDecoration(getActivity()));

        if (searchIndex == 0) {
            searchResultList = new ArrayList<>();
            persionAdapter = new SearchPersionResultAdapter(searchResultList);
            rvSearchResult.setAdapter(persionAdapter);
        } else {
            groupDatas = new ArrayList<>();
            groupdAdapter = new RecommendAdapter(getActivity(), groupDatas);
            rvSearchResult.setAdapter(groupdAdapter);
        }
    }

    //初始化监听
    private void iniListener() {
        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                switch (searchIndex) {
                    case 0:
                        searchPersion(layout2search.getText().toString());
                        break;
                    case 1:
                        getRecommendGroup(AppConst.REFRESH_TYPE_RELOAD,
                                layout2search.getText().toString());
                        break;
                }
            }
        });

        layout2search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(layout2search.getText())) {
                        switch (searchIndex) {
                            case 0:
                                searchPersion(layout2search.getText().toString());
                                break;
                            case 1:
                                getRecommendGroup(AppConst.REFRESH_TYPE_PARENT_BUTTON,
                                        layout2search.getText().toString());
                                break;
                        }
                        AppDevice.hideSoftKeyboard(layout2search);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void searchPersion(final String keyword) {

        xLayout.setEmptyText(String.format(getString(R.string.str_result), keyword) + "的用户");

        xLayout.setStatus(XLayout.Loading);

        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("page", String.valueOf(page));
        param.put("rows", "20");
        param.put("keyword", keyword);


        new GGOKHTTP(param, GGOKHTTP.SEARCH_FRIEND, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    searchResultList.clear();

                    List<ContactBean> persionDatas = JSONObject.parseArray(
                            JSONObject.parseObject(responseInfo).getString("data"), ContactBean.class);

                    searchResultList.addAll(persionDatas);

                    persionAdapter.setEnableLoadMore(true);
                    persionAdapter.loadMoreComplete();

                    persionAdapter.notifyDataSetChanged();

                    xLayout.setStatus(XLayout.Success);

                    persionAdapter.removeAllHeaderView();

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null)
                    xLayout.setStatus(XLayout.Error);
            }
        }).startGet();
    }

    /**
     * 搜群
     */
    private void getRecommendGroup(final int loadType, final String keyword) {
        xLayout.setEmptyText(String.format(getString(R.string.str_result), keyword) + "官方群组");

        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        if (!TextUtils.isEmpty(keyword)) {
            map.put("keyword", keyword);
        }
        map.put("is_recommend", String.valueOf(TextUtils.isEmpty(keyword)));

        new GGOKHTTP(map, GGOKHTTP.SEARCH_GROUP, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    groupDatas.clear();
                    GroupCollectionData groupCollectionData = JSONObject.parseObject(responseInfo, GroupCollectionData.class);
                    if (null != groupCollectionData.getData()) {
                        groupDatas.addAll(groupCollectionData.getData());
                        groupdAdapter.notifyDataSetChanged();
                        xLayout.setStatus(XLayout.Success);
                    }
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (loadType != AppConst.REFRESH_TYPE_FIRST) {
                    xLayout.setStatus(XLayout.Error);
                }
            }
        }).startGet();
    }

}
