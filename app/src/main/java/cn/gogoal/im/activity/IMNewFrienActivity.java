package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.base.MyApp.getContext;

/**
 * Created by huangxx on 2017/3/13.
 */

public class IMNewFrienActivity extends BaseActivity {

    XTitle titleBar;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
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

        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
        IMMessageBeans.clear();
        if (null != jsonArray) {
            IMMessageBeans = JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class);
        }

        if (null != IMMessageBeans && IMMessageBeans.size() > 0) {
            //按照时间排序
            Collections.sort(IMMessageBeans, new Comparator<IMMessageBean>() {
                @Override
                public int compare(IMMessageBean object1, IMMessageBean object2) {
                    return Long.compare(object2.getLastTime(), object1.getLastTime());
                }
            });
        }

        listAdapter = new ListAdapter(mContext, R.layout.item_new_friend, IMMessageBeans);
        newFriendList.setAdapter(listAdapter);
    }


    private class ListAdapter extends CommonAdapter<IMMessageBean> {

        private ListAdapter(Context context, int layoutId, List<IMMessageBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, IMMessageBean messageBean, int position) {
            String dateStr = "";
            String message = "";
            ImageView avatarIv = holder.getView(R.id.head_image);

            //未读数
            if (null != messageBean.getLastTime()) {
                dateStr = CalendarUtils.parseDateIMMessageFormat(messageBean.getLastTime());
            }

            //消息类型
            if (messageBean.getLastMessage() != null) {
                String content = messageBean.getLastMessage().getContent();
                JSONObject contentObject = JSON.parseObject(content);
                String _lctype = contentObject.getString("_lctype");
                switch (_lctype) {
                    case "-1":
                        message = contentObject.getString("_lctext");
                        break;
                    case "-2":
                        message = "[图片]";
                        break;
                    case "-3":
                        message = "[语音]";
                        break;
                    default:
                        break;
                }
            } else {
                message = "";
            }

            ImageDisplay.loadNetImage(IMNewFrienActivity.this, messageBean.getAvatar(), avatarIv);
            holder.setText(R.id.whose_message, messageBean.getNickname());
            holder.setText(R.id.last_message, message + "\t\t" + dateStr);
            holder.setText(R.id.last_time, "添加");
        }
    }
}
