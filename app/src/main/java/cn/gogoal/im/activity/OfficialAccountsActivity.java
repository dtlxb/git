package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.LcattrsBean;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/7.
 */

public class OfficialAccountsActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.official_message_recycler)
    RecyclerView officialRecycler;
    private List<LcattrsBean> officialMessages = new ArrayList<>();
    private OfficialMessagesAdapter messagesAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_official_account;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setMyTitle("", true);
        initRecycleView(officialRecycler, null);
        String conversationID = getIntent().getStringExtra("conversation_id");
        getAllMessages(conversationID);
        messagesAdapter = new OfficialMessagesAdapter(getActivity(), R.layout.item_official_account, officialMessages);
        officialRecycler.setAdapter(messagesAdapter);

        //未读数清零
        JSONArray messageListJsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", new JSONArray());
        List<IMMessageBean> iMMessageBeans = new ArrayList<>();
        iMMessageBeans.addAll(JSON.parseArray(String.valueOf(messageListJsonArray), IMMessageBean.class));
        for (int i = 0; i < iMMessageBeans.size(); i++) {
            if (iMMessageBeans.get(i).getConversationID().equals(conversationID)) {
                iMMessageBeans.get(i).setUnReadCounts("0");
                MessageUtils.saveMessageInfo(messageListJsonArray, iMMessageBeans.get(i));
            }
        }
    }

    private void getAllMessages(String conversationID) {
        AVImClientManager.getInstance().findConversationById(conversationID, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {

                conversation.queryMessagesFromCache(15, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        KLog.e(list.size());
                        if (e == null) {
                            if (null != list) {
                                for (int i = 0; i < list.size(); i++) {
                                    JSONObject contentObject = JSON.parseObject(list.get(i).getContent());
                                    JSONObject lcattrsObject = contentObject.getJSONObject("_lcattrs");
                                    officialMessages.add(JSON.parseObject(lcattrsObject.toJSONString(), LcattrsBean.class));
                                    KLog.e(officialMessages);
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void joinFail(String error) {

            }
        });
    }

    private class OfficialMessagesAdapter extends CommonAdapter<LcattrsBean, BaseViewHolder> {

        private OfficialMessagesAdapter(Context context, int layoutId, List<LcattrsBean> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, LcattrsBean lcattrsBean, int position) {
            TextView official_name = holder.getView(R.id.official_name);
            TextView official_content = holder.getView(R.id.official_content);
            TextView official_brief = holder.getView(R.id.official_brief);
            TextView official_allinfo = holder.getView(R.id.official_allinfo);
            ImageView official_image = holder.getView(R.id.official_image);

            if (null != lcattrsBean.getTitle()) {
                official_name.setText(lcattrsBean.getTitle().getWord());
                official_name.setTextColor(Color.parseColor(lcattrsBean.getTitle().getColor()));
            }

            if (null != lcattrsBean.getContent_title()) {
                if (!TextUtils.isEmpty(lcattrsBean.getContent_title().getWord())) {
                    official_content.setText(lcattrsBean.getContent_title().getWord());
                }
                if (!TextUtils.isEmpty(lcattrsBean.getContent_title().getColor())) {
                    official_content.setTextColor(Color.parseColor(lcattrsBean.getContent_title().getColor()));
                }
            }
            if (null != lcattrsBean.getContent() && !TextUtils.isEmpty(lcattrsBean.getContent().getWord())) {
                official_allinfo.setText(lcattrsBean.getContent().getWord());
            }

            int stringLength = 0;
            int lastLength = 0;
            SpannableStringBuilder stringBuffer = new SpannableStringBuilder();

            if (null != lcattrsBean.getCoustom_list()) {
                for (int i = 0; i < lcattrsBean.getCoustom_list().size(); i++) {
                    if (i > 0) {
                        stringBuffer.append("\n");
                    }
                    for (int j = 0; j < lcattrsBean.getCoustom_list().get(i).size(); j++) {
                        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor(
                                lcattrsBean.getCoustom_list().get(i).get(j).getColor().trim().replace(" ", "")));
                        lastLength = stringBuffer.length();
                        stringBuffer.append(lcattrsBean.getCoustom_list().get(i).get(j).getWord());
                        stringLength = stringBuffer.length();
                        stringBuffer.setSpan(fcs, lastLength, stringLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
                official_brief.setText(stringBuffer);
            }

            if (!TextUtils.isEmpty(lcattrsBean.getImg_url())) {
                ImageDisplay.loadImage(getActivity(), lcattrsBean.getImg_url(), official_image);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) official_image.getLayoutParams();
                params.height = 0;
                params.width = 0;
                official_image.setLayoutParams(params);
            }
        }
    }
}
