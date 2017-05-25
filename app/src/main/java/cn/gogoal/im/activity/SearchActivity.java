package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ImageTextBean;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.ui.NormalItemDecoration;


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

        final String conversationId = getIntent().getStringExtra("conversation_id");

        KLog.e(conversationId);

        AVIMClientManager.getInstance().findConversationById(conversationId, new AVIMClientManager.ChatJoinManager() {
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

        rvFlagSearch.setAdapter(new CommonAdapter<ImageTextBean<Integer>, BaseViewHolder>(R.layout.item_search_flag, getFlagData()) {
            @Override
            protected void convert(BaseViewHolder holder, ImageTextBean<Integer> data, int position) {
                holder.setImageResource(R.id.img_search_item, data.getIamge());
                holder.setText(R.id.tv_search_item, data.getText());
            }
        });

        rvHistory.addItemDecoration(new NormalItemDecoration(mContext, Color.parseColor("#eeeeee")));

//        setHistory();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                ArrayUtils.addSearchKeyword(searchView.getQuery().toString());
//                setHistory();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    //初始化标题
    private void iniTitle() {
        setMyTitle("查找聊天", true);
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        et.setTextSize(14);
        et.setHint("搜索");

//        et.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    KLog.e("搜索="+searchView.getQuery());
//                    ArrayUtils.addSearchKeyword(searchView.getQuery().toString());
//                    setHistory();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private List<ImageTextBean<Integer>> getFlagData() {
        int[] flagIcons = {R.mipmap.img_search_flag_contact, R.mipmap.img_search_flag_group, R.mipmap.home_bottom_tab_icon_message_normal,
                R.mipmap.img_search_flag_live, R.mipmap.img_search_flag_yanwang, R.mipmap.img_search_flag_pinpai};
        List<ImageTextBean<Integer>> list = new ArrayList<>();

        for (int i = 0; i < flagIcons.length; i++) {
            list.add(new ImageTextBean<>(flagIcons[i], searchFlagArray[i]));
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

    private class SearchConversationAdapter extends CommonAdapter<IMMessageBean, BaseViewHolder> {

        public SearchConversationAdapter(Context context, int layoutId, List<IMMessageBean> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, IMMessageBean data, int position) {

        }
    }
}
