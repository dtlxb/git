package com.gogoal.app.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gogoal.app.R;
import com.gogoal.app.adapter.recycleviewAdapterHelper.CommonAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.base.ViewHolder;
import com.gogoal.app.base.BaseFragment;
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

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("消息");
    }

    @Override
    public void onResume() {
        super.onResume();

        JSONArray jsonArray = SPTools.getJsonArray("conversation_beans", null);
        Log.e("+++jsonArray", jsonArray + "");

        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.clear();
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObjects.add(jsonArray.getJSONObject(i));
            }
        }

        Log.e("+++jsonObjects", jsonObjects.size() + "");

        initRecycleView(message_recycler, 0);

        message_recycler.setAdapter(new listAdapter(getContext(), R.layout.item_fragment_message, jsonObjects));

    }

    class listAdapter extends CommonAdapter<JSONObject> {

        public listAdapter(Context context, int layoutId, List<JSONObject> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, JSONObject jsonObject, int position) {
            holder.setText(R.id.whose_message, (String) jsonObject.get("conversationID"));
            holder.setText(R.id.last_message, (String) jsonObject.get("lastMessage"));
            holder.setText(R.id.last_time, (String) jsonObject.get("lastTime"));
        }
    }


}
