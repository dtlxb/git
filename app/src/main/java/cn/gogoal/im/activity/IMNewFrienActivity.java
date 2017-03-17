package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMNewFriendBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XTitle;


/**
 * Created by huangxx on 2017/3/13.
 */

public class IMNewFrienActivity extends BaseActivity {

    XTitle titleBar;
    private List<IMNewFriendBean> newFriendBeans = new ArrayList<>();
    private ListAdapter listAdapter;
    private JSONArray jsonArray;
    @BindView(R.id.new_friend_list)
    RecyclerView newFriendList;

    @Override
    public int bindLayout() {
        return R.layout.activity_im_newfriend;
    }

    @Override
    public void doBusiness(Context mContext) {
        titleBar = setMyTitle(R.string.title_newfirend, true);
        titleBar.setLeftText(R.string.title_message);
        initRecycleView(newFriendList, R.drawable.shape_divider_recyclerview_1px);

        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_newFriendList", new JSONArray());

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


    class ListAdapter extends CommonAdapter<IMNewFriendBean> {

        public ListAdapter(Context context, int layoutId, List<IMNewFriendBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final IMNewFriendBean mIMNewFriendBean, int position) {
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
            message = contentObject.getString("_lctext");

            JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
            avatar = lcattrsObject.getString("avatar");
            final String firend_id = lcattrsObject.getString("account_id");
            nickName = lcattrsObject.getString("nickname");


            ImageDisplay.loadNetImage(IMNewFrienActivity.this, avatar, avatarIv);
            holder.setText(R.id.nickname, nickName);
            holder.setText(R.id.last_message, message + "\t\t" + dateStr);
            KLog.e(mIMNewFriendBean);
            if (mIMNewFriendBean.getIsYourFriend()) {
                addView.setText("已添加");
                addView.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.relater_play_count));
                addView.setBackgroundResource(R.color.absoluteWhite);
            } else {
                addView.setText("添加");
                addView.setBackgroundResource(R.drawable.shape_add_friend);
                addView.setTextColor(ContextCompat.getColor(IMNewFrienActivity.this, R.color.absoluteWhite));
            }

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddFirend(mIMNewFriendBean, firend_id, addView);
                    addView.setClickable(false);
                }
            });
        }
    }

    public void AddFirend(final IMNewFriendBean mIMNewFriendBean, String friend_id, final TextView view) {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
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
                    UIHelper.toast(IMNewFrienActivity.this, "添加成功!");
                    if (view.getText().toString().equals("添加")) {
                        view.setText("已添加");
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

                        SPTools.saveJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_newFriendList", jsonArray);
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
}
