package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.wrapper.HeaderAndFooterWrapper;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ImageTextBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;


/**
 * author wangjd on 2017/3/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :搜索.
 */
public class SearchActivity extends BaseActivity {

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

    private SearchConversationAdapter searchReultAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();

        HashSet<String> testSet=new HashSet<>();
        testSet.add("qwer");
        testSet.add("yruru");
        testSet.add("vsvvdfv");
        testSet.add("6456");
        testSet.add("23fsdf");
//        for ()
//        ArrayUtils.addSearchKeyword();

        final String conversationId = getIntent().getStringExtra("conversation_id");

        KLog.e(conversationId);

        AVImClientManager.getInstance().findConversationById(conversationId, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                conversation.queryMessagesFromCache(1000, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        if (null != list && !list.isEmpty()) {
                            KLog.e(JSONObject.toJSONString(list));
                        }
                    }
                });
            }

            @Override
            public void joinFail(String error) {

            }
        });

        rvFlagSearch.setAdapter(new CommonAdapter<ImageTextBean<Integer>>(getActivity(),R.layout.item_search_flag,getFlagData()) {
            @Override
            protected void convert(ViewHolder holder, ImageTextBean<Integer> data, int position) {
                holder.setImageResource(R.id.img_search_item,data.getIamge());
                holder.setText(R.id.tv_search_item,data.getText());
            }
        });

        CommonAdapter<String> historyAdapter = new CommonAdapter<String>(mContext, R.layout.item_search_history, ArrayUtils.getSearchList()) {
            @Override
            protected void convert(ViewHolder holder, String data, int position) {

            }
        };

        HeaderAndFooterWrapper headerWrapper = new HeaderAndFooterWrapper(historyAdapter);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, null);
        headerView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        TextView textHead= (TextView) headerView.findViewById(R.id.tv_search_history);
        textHead.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textHead.getLayoutParams();
        params.setMargins(AppDevice.dp2px(mContext,20),AppDevice.dp2px(mContext,11),0,AppDevice.dp2px(mContext,11));
        headerWrapper.addHeaderView(headerView);

        rvHistory.setAdapter(headerWrapper);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //初始化标题
    private void iniTitle() {
        setMyTitle("查找聊天", true);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/search.ttf");
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        et.setTypeface(iconfont);
        et.setTextSize(14);
        et.setHint(getString(R.string.str_search));
    }

    private List<ImageTextBean<Integer>> getFlagData() {
        int[] flagIcons={R.mipmap.img_search_flag_contact,R.mipmap.img_search_flag_group,R.mipmap.home_bottom_tab_icon_message_normal,
                R.mipmap.img_search_flag_live,R.mipmap.img_search_flag_yanwang,R.mipmap.img_search_flag_pinpai};
        List<ImageTextBean<Integer>> list=new ArrayList<>();

        for (int i=0;i<flagIcons.length;i++){
            list.add(new ImageTextBean<>(flagIcons[i],searchFlagArray[i]));
        }
        return list;
    }

    /**
     * 筛选逻辑
     */
    private List<AVIMMessage> filter(List<AVIMMessage> messages, String searchQuery) {
        searchQuery = searchQuery.toLowerCase();

        final List<AVIMMessage> filteredModelList = new ArrayList<>();
        for (AVIMMessage msg : messages) {

            final String content = msg.getContent().toLowerCase();
            final String userName = msg.getFrom().toLowerCase();

            if (content.contains(searchQuery) || userName.contains(searchQuery)) {
                filteredModelList.add(msg);
            }
        }
        return filteredModelList;
    }

    private class SearchConversationAdapter extends CommonAdapter<IMMessageBean>{

        public SearchConversationAdapter(Context context, int layoutId, List<IMMessageBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, IMMessageBean data, int position) {

        }
    }
}
