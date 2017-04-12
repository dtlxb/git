package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.IMNewFriendBean;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;


/**
 * Created by huangxx on 2017/3/13.
 */

public class IMNewFrienActivity extends BaseActivity {

    XTitle titleBar;
    private List<IMNewFriendBean> newFriendBeans = new ArrayList<>();
    private ListAdapter listAdapter;
    private JSONArray jsonArray;
    private JSONArray messageListJsonArray;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
    private int addType;
    private String conversationId;
    @BindView(R.id.recyclerView)
    RecyclerView newFriendList;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        addType = getIntent().getIntExtra("add_type", 0x01);
        conversationId = getIntent().getStringExtra("conversation_id");
        titleBar = setMyTitle(R.string.title_message, true);
        //未读数清零
        messageListJsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", new JSONArray());
        IMMessageBeans.addAll(JSON.parseArray(String.valueOf(messageListJsonArray), IMMessageBean.class));
        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversationId)) {
                IMMessageBeans.get(i).setUnReadCounts("0");
                MessageUtils.saveMessageInfo(messageListJsonArray, IMMessageBeans.get(i));
            }
        }

        if (addType == 0x01) {
            titleBar.setLeftText(R.string.title_new_friend);
            jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_newFriendList", new JSONArray());
        } else if (addType == 0x02) {
            titleBar.setLeftText(R.string.title_add_group);
            jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + conversationId + "_unadd_accountList_beans", new JSONArray());
        }

        initRecycleView(newFriendList, R.drawable.shape_divider_1px);

        KLog.e(jsonArray.toString());
        newFriendBeans.clear();
        if (null != jsonArray) {
            newFriendBeans = JSON.parseArray(String.valueOf(jsonArray), IMNewFriendBean.class);
        }

        if (null != newFriendBeans && newFriendBeans.size() > 0) {
            //按照时间排序
            Collections.sort(newFriendBeans, new Comparator<IMNewFriendBean>() {
                @Override
                public int compare(IMNewFriendBean object1, IMNewFriendBean object2) {
                    return Long.compare(object2.getMessage().getTimestamp(), object1.getMessage().getTimestamp());
                }
            });
        }

        listAdapter = new ListAdapter(IMNewFrienActivity.this, R.layout.item_new_friend, newFriendBeans);
        newFriendList.setAdapter(listAdapter);
    }


    class ListAdapter extends CommonAdapter<IMNewFriendBean, BaseViewHolder> {

        public ListAdapter(Context context, int layoutId, List<IMNewFriendBean> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final IMNewFriendBean mIMNewFriendBean, int position) {
            String dateStr = "";
            String message = "";
            String avatar = "";
            String nickName = "";
            final TextView addView = holder.getView(R.id.add_button);
            ImageView avatarIv = holder.getView(R.id.head_image);


            //未读数
            dateStr = CalendarUtils.parseDateIMMessageFormat(mIMNewFriendBean.getMessage().getTimestamp());

            //消息类型
            JSONObject contentObject = JSON.parseObject(mIMNewFriendBean.getMessage().getContent());
            JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
            if (addType == 0x01) {
                message = contentObject.getString("_lctext");
            } else if (addType == 0x02) {
                message = "请求加入" + lcattrsObject.getString("group_name");
            }
            avatar = lcattrsObject.getString("avatar");
            final String firend_id = lcattrsObject.getString("account_id");
            nickName = lcattrsObject.getString("nickname");


            ImageDisplay.loadNetImage(IMNewFrienActivity.this, avatar, avatarIv);
            holder.setText(R.id.nickname, nickName);
            holder.setText(R.id.last_message, message + "\t\t" + dateStr);
            KLog.e(mIMNewFriendBean);
            if (mIMNewFriendBean.getIsYourFriend()) {
                if (addType == 0x01) {
                    addView.setText("已添加");
                } else if (addType == 0x02) {
                    addView.setText("已通过");
                }
                addView.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.relater_play_count));
                addView.setBackgroundColor(Color.WHITE);
            } else {
                if (addType == 0x01) {
                    addView.setText("添加");
                } else if (addType == 0x02) {
                    addView.setText("通过");
                }
                addView.setBackgroundResource(R.drawable.shape_add_friend);
                addView.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.absoluteWhite));
            }

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addType == 0x01) {
                        addFirend(mIMNewFriendBean, firend_id, addView);
                    } else if (addType == 0x02) {
                        List<String> idList = new ArrayList<>();
                        idList.add(firend_id);
                        agreenToGroup(mIMNewFriendBean, idList, addView);
                    }
                    addView.setClickable(false);
                }
            });
        }
    }

    public void addFirend(final IMNewFriendBean mIMNewFriendBean, String friend_id, final TextView view) {

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("friend_id", friend_id);
        params.put("text", "你好啊！！！");
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    if (view.getText().toString().equals("添加")) {
                        view.setText("已添加");
                        view.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.relater_play_count));
                        view.setBackgroundResource(R.color.absoluteWhite);
                        view.setClickable(true);

                        //通讯录添加此人
                        JSONObject newObject = new JSONObject();
                        JSONObject contentObject = JSON.parseObject(mIMNewFriendBean.getMessage().getContent());
                        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                        newObject.put("conv_id", mIMNewFriendBean.getMessage().getConversationId());
                        newObject.put("avatar", lcattrsObject.get("avatar"));
                        newObject.put("nickname", lcattrsObject.get("nickname"));
                        newObject.put("friend_id", lcattrsObject.get("account_id"));

                        String string = SPTools.getString(UserUtils.getUserAccountId() + "_contact_beans", null);
                        JSONArray contactsJsonArray = null;
                        JSONObject contactjsonObject;
                        if (null != string && string.equals("")) {
                            contactjsonObject = JSON.parseObject(string);
                            if (null != contactjsonObject.get("data")) {
                                contactsJsonArray = contactjsonObject.getJSONArray("data");
                                contactsJsonArray.add(newObject);
                            }
                        } else {
                            contactsJsonArray = new JSONArray();
                            contactsJsonArray.add(newObject);
                            contactjsonObject = new JSONObject();
                        }
                        contactjsonObject.put("data", contactsJsonArray);
                        contactjsonObject.put("code", 0);
                        contactjsonObject.put("message", "成功");
                        SPTools.saveString(UserUtils.getUserAccountId() + "_contact_beans", JSON.toJSONString(contactjsonObject));

                        //列表缓存
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject messageJSONObject = (JSONObject) jsonObject.get("message");

                            if (messageJSONObject.getString("from").equals(mIMNewFriendBean.getMessage().getFrom())) {
                                ((JSONObject) jsonArray.get(i)).put("isYourFriend", true);
                            }
                        }

                        SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_newFriendList", jsonArray);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                view.setClickable(true);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_FRIEND, ggHttpInterface).startGet();
    }

    public void agreenToGroup(final IMNewFriendBean mIMNewFriendBean, List idList, final TextView view) {

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(idList));
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    if (view.getText().toString().equals("通过")) {
                        view.setText("已通过");
                        view.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.relater_play_count));
                        view.setBackgroundResource(R.color.absoluteWhite);
                        view.setClickable(true);

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject messageJSONObject = (JSONObject) jsonObject.get("message");

                            if (messageJSONObject.getString("from").equals(mIMNewFriendBean.getMessage().getFrom())) {
                                ((JSONObject) jsonArray.get(i)).put("isYourFriend", true);
                            }
                        }

                        SPTools.saveJsonArray(UserUtils.getUserAccountId() + conversationId + "_unadd_accountList_beans", jsonArray);

                        JSONObject jsonObject = new JSONObject();
                        JSONObject contentObject = JSON.parseObject(mIMNewFriendBean.getMessage().getContent());
                        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                        jsonObject.put("_id", lcattrsObject.get("_id"));
                        jsonObject.put("avatar", lcattrsObject.get("avatar"));
                        jsonObject.put("nickname", lcattrsObject.get("nickname"));
                        jsonObject.put("friend_id", lcattrsObject.get("account_id"));
                        JSONArray accountArray = new JSONArray();
                        accountArray.add(jsonObject);

                        MessageUtils.changeSquareInfo(lcattrsObject.getString("conv_id"), accountArray, "5");

                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                view.setClickable(true);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_MEMBER, ggHttpInterface).startGet();
    }
}
