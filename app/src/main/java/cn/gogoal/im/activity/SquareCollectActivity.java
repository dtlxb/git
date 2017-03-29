package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/29.
 */

public class SquareCollectActivity extends BaseActivity {
    @BindView(R.id.square_room_recycler)
    RecyclerView squareRoomRecycler;
    private ListAdapter listAdapter;
    private XTitle xTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_square_collcet;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setMyTitle(R.string.title_square_collcet, true);
        xTitle.setLeftText(R.string.tv_cancle);
        //添加action
        XTitle.TextAction addAction = new XTitle.TextAction("添加") {
            @Override
            public void actionClick(View view) {
            }
        };
        xTitle.addAction(addAction);
        initRecycleView(squareRoomRecycler, R.drawable.shape_divider_recyclerview_1px);

        JSONArray groupsArray = SPTools.getJsonArray(UserUtils.getToken() + "_groups_saved", new JSONArray());
        List<JSONObject> objectList = new ArrayList<>();
        for (int i = 0; i < groupsArray.size(); i++) {
            objectList.add((JSONObject) groupsArray.get(i));
        }
        listAdapter = new ListAdapter(SquareCollectActivity.this, R.layout.item_fragment_message, objectList);
        squareRoomRecycler.setAdapter(listAdapter);

    }

    private class ListAdapter extends CommonAdapter<JSONObject> {

        private ListAdapter(Context context, int layoutId, List<JSONObject> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, JSONObject groupObject, int position) {
            ImageView avatarIv = holder.getView(R.id.head_image);
            TextView timeView = holder.getView(R.id.last_time);
            timeView.setVisibility(View.GONE);

            KLog.e(groupObject.getString("groupMembers"));

            holder.setText(R.id.last_message, groupObject.getString("squareDetail"));
            holder.setText(R.id.whose_message, groupObject.getString("squareName") + "(" + groupObject.getString("groupMembers") + ")");
        }
    }

}
