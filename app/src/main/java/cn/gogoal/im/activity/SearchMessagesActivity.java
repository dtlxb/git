package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVHistoryMessage;
import com.avos.avoscloud.AVHistoryMessageQuery;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by huangxx on 2017/5/15.
 */

public class SearchMessagesActivity extends BaseActivity {

    @BindView(R.id.layout_2search)
    AppCompatEditText layout2search;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_not_find)
    TextView notFind;
    private List<AVIMTextMessage> messageList;
    private MessageListAdapter messageListAdapter;
    private List<AVIMTextMessage> textMessages;
    private int chatType;

    @Override
    public int bindLayout() {
        return R.layout.activity_message_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.im_str_look_message, true);
        String conversationID = getIntent().getStringExtra("conversation_id");

        messageList = new ArrayList<>();
        textMessages = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(R.layout.item_fragment_message, textMessages);
        initRecycleView(recyclerView, R.drawable.shape_divider_1px);
        recyclerView.setAdapter(messageListAdapter);

        layout2search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(layout2search.getText())) {
                        AppDevice.hideSoftKeyboard(layout2search);
                    }
                    return true;
                }
                return false;
            }
        });
        layout2search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                textMessages.clear();
                if (!text.isEmpty()) {
                    for (int i = 0; i < messageList.size(); i++) {
                        String messageText = messageList.get(i).getText();
                        if (messageText.contains(text)) {
                            textMessages.add(messageList.get(i));
                        }
                    }
                    if (textMessages != null && textMessages.size() > 0) {
                        notFind.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        messageListAdapter.notifyDataSetChanged();
                    } else {
                        notFind.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    notFind.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    messageListAdapter.notifyDataSetChanged();
                }

            }
        });
        AVImClientManager.getInstance().findConversationById(conversationID, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                chatType = (int) conversation.getAttribute("chat_type");

                conversation.queryMessagesFromCache(1000, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i) instanceof AVIMTextMessage) {
                                    messageList.add((AVIMTextMessage) list.get(i));
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

        //跳转查看消息
        messageListAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent = null;
                if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                    intent = new Intent(SearchMessagesActivity.this, SingleChatRoomActivity.class);
                    AppManager.getInstance().finishActivity(SingleChatRoomActivity.class);
                    AppManager.getInstance().finishActivity(IMPersonActivity.class);
                } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                    AppManager.getInstance().finishActivity(SquareChatRoomActivity.class);
                    AppManager.getInstance().finishActivity(IMSquareChatSetActivity.class);
                    intent = new Intent(SearchMessagesActivity.this, SquareChatRoomActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("message_chooesed", textMessages.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                SearchMessagesActivity.this.finish();
            }
        });

    }

    public class MessageListAdapter extends CommonAdapter<AVIMTextMessage, BaseViewHolder> {

        public MessageListAdapter(int layoutResId, List<AVIMTextMessage> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AVIMTextMessage message, int position) {
            ImageView avatarIv = holder.getView(R.id.head_image);
            holder.setText(R.id.last_message, message.getText());
            //时间
            String dateStr = CalendarUtils.parseDateIMMessageFormat(message.getTimestamp());
            holder.setText(R.id.last_time, dateStr);
            //头像
            JSONObject contentObject = JSON.parseObject(message.getContent());
            JSONObject lcattrsObject = contentObject.getJSONObject("_lcattrs");
            ImageDisplay.loadRoundedRectangleImage(getActivity(), lcattrsObject.getString("avatar"), avatarIv);
            //昵称
            holder.setText(R.id.whose_message, lcattrsObject.getString("username"));
        }
    }
}
