package com.gogoal.app.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gogoal.app.R;
import com.gogoal.app.adapter.recycleviewAdapterHelper.CommonAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.base.ViewHolder;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.CalendarUtils;
import com.gogoal.app.common.SPTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;
    private List<JSONObject> jsonObjects = new ArrayList<>();
    private ListAdapter listAdapter;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_message);
    }

    @Override
    public void onResume() {
        super.onResume();

        JSONArray jsonArray = SPTools.getJsonArray("conversation_beans", null);

        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.clear();
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObjects.add(jsonArray.getJSONObject(i));
            }
        }

        Log.e("+++jsonObjects", jsonObjects.size() + "");

        initRecycleView(message_recycler, 0);

        listAdapter = new ListAdapter(getContext(), R.layout.item_fragment_message, jsonObjects);

        message_recycler.setAdapter(listAdapter);

        //长按删除
        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    class ListAdapter extends CommonAdapter<JSONObject> {

        public ListAdapter(Context context, int layoutId, List<JSONObject> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, JSONObject jsonObject, int position) {
            String dateStr = "";
            String message = "";
            if (null != jsonObject.get("lastTime")) {
                dateStr = CalendarUtils.parseDateFormat((Long) jsonObject.get("lastTime"));
            }

            /*if (jsonObject.get("lastMessage") instanceof AVIMTextMessage) {
                message = ((AVIMTextMessage) jsonObject.get("lastMessage")).getText();
            } else if (jsonObject.get("lastMessage") instanceof AVIMAudioMessage) {
                message = "语音信息";
            }else {
                message = "图片信息";
            }*/

            Log.e("+++message", jsonObject.get("lastMessage") + "");
            if (jsonObject.get("lastMessage") == null) {
                message = "";
            } else {
                JSONObject object = JSON.parseObject(jsonObject.getString("lastMessage"));
                JSONObject contentObject=JSON.parseObject(object.getString("content"));
                message = contentObject.getString("_lctext");
            }

            holder.setText(R.id.whose_message, (String) jsonObject.get("speakerTo"));
            holder.setText(R.id.last_message, message);
            holder.setText(R.id.last_time, dateStr);
        }
    }

    /**
     * 消息接收
     */
    /*@Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                AVIMTextMessage msg = (AVIMTextMessage) message;
                message_show.setText(msg.getText());
            }
        }
    }*/


}
