package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.GroupCollectionData;
import cn.gogoal.im.bean.ImageTextBean;
import cn.gogoal.im.bean.SearchBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
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

    private List<SearchBean> allBeans;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();

        rvFlagSearch.setAdapter(new CommonAdapter<ImageTextBean<Integer>, BaseViewHolder>(R.layout.item_search_flag, getFlagData()) {
            @Override
            protected void convert(BaseViewHolder holder, ImageTextBean<Integer> data, int position) {
                holder.setImageResource(R.id.img_search_item, data.getIamge());
                holder.setText(R.id.tv_search_item, data.getText());
            }
        });

        getDatas();
    }

    private void getDatas() {
        allBeans = new ArrayList<>();
        //联系人列表
        String friendResponseInfo = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", "");
        List<ContactBean> list = new ArrayList<>();
        BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                friendResponseInfo,
                new TypeReference<BaseBeanList<ContactBean<String>>>() {
                });
        list.clear();
        list.addAll(beanList.getData());

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
    private List<ImageTextBean<Integer>> getFlagData() {
        /*int[] flagIcons = {R.mipmap.img_search_flag_contact, R.mipmap.img_search_flag_group, R.mipmap.home_bottom_tab_icon_message_normal,
                R.mipmap.img_search_flag_live, R.mipmap.img_search_flag_yanwang, R.mipmap.img_search_flag_pinpai};*/
        int[] flagIcons = {R.mipmap.img_search_flag_contact, R.mipmap.img_search_flag_group};
        List<ImageTextBean<Integer>> list = new ArrayList<>();

        for (int i = 0; i < flagIcons.length; i++) {
            list.add(new ImageTextBean<>(flagIcons[i], searchFlagArray[i]));
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

                    List<GroupCollectionData.DataBean> data =
                            JSONObject.parseObject(responseInfo, GroupCollectionData.class).getData();
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
