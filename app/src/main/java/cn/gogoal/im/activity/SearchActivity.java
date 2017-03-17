package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
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

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();
        final String conversationId = getIntent().getStringExtra("conversation_id");

        KLog.e(conversationId);

        AVIMConversationQuery query = AVImClientManager.getInstance().getClient().getQuery().whereEqualTo("objectId", conversationId);

        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                KLog.e(JSONObject.toJSONString(list));
                list.get(0).queryMessagesFromCache(15, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        KLog.e(JSONObject.toJSONString(list));
                    }
                });
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

}
