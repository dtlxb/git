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

import org.litepal.crud.DataSupport;

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
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.IMNewFriendBean;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;


/**
 * Created by huangxx on 2017/3/13.
 */

public class IMNewFriendActivity extends BaseActivity {

    XTitle titleBar;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.recyclerView)
    RecyclerView newFriendList;

    private List<IMNewFriendBean> newFriendBeans = new ArrayList<>();
    private ListAdapter listAdapter;
    private JSONArray jsonArray;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
    private int addType;
    private String conversationId;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        addType = getIntent().getIntExtra("add_type", 0x01);
        conversationId = getIntent().getStringExtra("conversation_id");
        titleBar = setMyTitle(R.string.title_message, true);
        boolean icClear = false;
        //未读数清零
        IMMessageBeans.addAll(DataSupport.findAll(IMMessageBean.class));
        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversationId)) {
                IMMessageBeans.get(i).setUnReadCounts("0");
                MessageListUtils.saveMessageInfo(IMMessageBeans.get(i));
                icClear = true;
            }
        }

        xLayout.setEmptyText("暂时还没有好友申请");
        if (icClear) {
            //通知服务器重新获取
            AppManager.getInstance().sendMessage("Cache_change");
        }

        if (addType == 0x01) {
            titleBar.setLeftText(R.string.title_new_friend);
            jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_newFriendList", new JSONArray());
        } else if (addType == 0x02) {
            titleBar.setLeftText(R.string.title_add_group);
            jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationId + "_unadd_accountList_beans", new JSONArray());
        }

        initRecycleView(newFriendList, R.drawable.shape_divider_1px);

        newFriendBeans.clear();
        if (null != jsonArray && jsonArray.size() > 0) {
            xLayout.setStatus(XLayout.Success);
            newFriendBeans = JSON.parseArray(String.valueOf(jsonArray), IMNewFriendBean.class);
        } else {
            xLayout.setStatus(XLayout.Empty);
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

        listAdapter = new ListAdapter(R.layout.item_new_friend, newFriendBeans);
        newFriendList.setAdapter(listAdapter);
    }


    class ListAdapter extends CommonAdapter<IMNewFriendBean, BaseViewHolder> {

        public ListAdapter(int layoutId, List<IMNewFriendBean> datas) {
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
            final String friend_id = lcattrsObject.getString("account_id");
            nickName = lcattrsObject.getString("nickname");

            ImageDisplay.loadImage(IMNewFriendActivity.this, avatar, avatarIv);
            holder.setText(R.id.nickname, nickName);
            holder.setText(R.id.last_message, message + "\t\t" + dateStr);
            if (mIMNewFriendBean.getIsYourFriend()) {
                if (addType == 0x01) {
                    addView.setText("已添加");
                } else if (addType == 0x02) {
                    addView.setText("已通过");
                }
                addView.setTextColor(ContextCompat.getColor(IMNewFriendActivity.this, R.color.relater_play_count));
                addView.setBackgroundColor(Color.WHITE);
                addView.setClickable(false);
            } else {
                if (addType == 0x01) {
                    addView.setText("添加");
                } else if (addType == 0x02) {
                    addView.setText("通过");
                }
                addView.setBackgroundResource(R.drawable.shape_add_friend);
                addView.setTextColor(ContextCompat.getColor(IMNewFriendActivity.this, android.R.color.white));
                addView.setClickable(true);
            }

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addType == 0x01) {
                        addFriend(mIMNewFriendBean, friend_id, addView);
                    } else if (addType == 0x02) {
                        List<String> idList = new ArrayList<>();
                        idList.add(friend_id);
                        agreeToGroup(mIMNewFriendBean, idList, addView);
                    }
                    addView.setClickable(false);
                }
            });
        }
    }

    public void addFriend(final IMNewFriendBean mIMNewFriendBean, String friend_id, final TextView view) {

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
                if ((int) result.get("code") == 0) {
                    if (view.getText().toString().equals("添加")) {
                        view.setText("已添加");
                        view.setTextColor(ContextCompat.getColor(IMNewFriendActivity.this, R.color.relater_play_count));
                        view.setBackgroundResource(android.R.color.white);
                        view.setClickable(false);

                        //列表缓存
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject messageJSONObject = (JSONObject) jsonObject.get("message");

                            if (messageJSONObject.getString("from").equals(mIMNewFriendBean.getMessage().getFrom())) {
                                ((JSONObject) jsonArray.get(i)).put("isYourFriend", true);
                            }
                        }

                        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_newFriendList", jsonArray);

                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                view.setClickable(true);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_FRIEND, ggHttpInterface).startGet();
    }

    public void agreeToGroup(final IMNewFriendBean mIMNewFriendBean, List idList, final TextView view) {

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
                        view.setTextColor(ContextCompat.getColor(IMNewFriendActivity.this, R.color.relater_play_count));
                        view.setBackgroundResource(android.R.color.white);
                        view.setClickable(false);

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject messageJSONObject = (JSONObject) jsonObject.get("message");

                            if (messageJSONObject.getString("from").equals(mIMNewFriendBean.getMessage().getFrom())) {
                                ((JSONObject) jsonArray.get(i)).put("isYourFriend", true);
                            }
                        }

                        SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversationId + "_unadd_accountList_beans", jsonArray);

                        JSONObject jsonObject = new JSONObject();
                        JSONObject contentObject = JSON.parseObject(mIMNewFriendBean.getMessage().getContent());
                        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                        jsonObject.put("_id", lcattrsObject.get("_id"));
                        jsonObject.put("avatar", lcattrsObject.get("avatar"));
                        jsonObject.put("nickname", lcattrsObject.get("nickname"));
                        jsonObject.put("friend_id", lcattrsObject.get("account_id"));
                        JSONArray accountArray = new JSONArray();
                        accountArray.add(jsonObject);

                        UserInfoUtils.saveGroupUserInfo(lcattrsObject.getString("conv_id"), accountArray);

                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (view!=null)
                view.setClickable(true);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_MEMBER, ggHttpInterface).startGet();
    }
}
