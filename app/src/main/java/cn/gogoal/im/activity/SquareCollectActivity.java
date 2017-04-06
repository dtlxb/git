package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/29.
 */

public class SquareCollectActivity extends BaseActivity {

    @BindView(R.id.square_room_recycler)
    RecyclerView squareRoomRecycler;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private ListAdapter listAdapter;

    private List<JSONObject> groupList;

    @Override
    public int bindLayout() {
        return R.layout.activity_square_collcet;
    }

    @Override
    public void doBusiness(final Context mContext) {

        setMyTitle(R.string.title_square_collcet, true).setLeftText(R.string.tv_cancle).addAction(new XTitle.TextAction(getString(R.string.square_collcet_add)) {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(mContext, SearchPersonSquareActivity.class);
                intent.putExtra("search_index", 1);
                startActivity(intent);
            }
        });

        xLayout.setEmptyText("你还没有群组\n\r赶快找到属于你的组织吧");
        initRecycleView(squareRoomRecycler, R.drawable.shape_divider_recyclerview_1px);

        JSONArray groupsArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_groups_saved", new JSONArray());
        Boolean needRefresh = SPTools.getBoolean("needRefresh", false);
        KLog.e(groupsArray.toString());

        groupList = new ArrayList<>();
        if (needRefresh) {
            getGroupList();
        } else {
            if (groupsArray.size() > 0) {
                for (int i = 0; i < groupsArray.size(); i++) {
                    groupList.add((JSONObject) groupsArray.get(i));
                }
                xLayout.setStatus(XLayout.Success);
            } else {
                xLayout.setStatus(XLayout.Empty);
            }
        }
        KLog.e(groupList);
        listAdapter = new ListAdapter(SquareCollectActivity.this, R.layout.item_square_collect, groupList);
        squareRoomRecycler.setAdapter(listAdapter);

    }

    //收藏群
    public void getGroupList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        xLayout.setStatus(XLayout.Loading);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                Log.e("====responseInfo", responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONArray newGroupArray = (JSONArray) result.get("data");
                    for (int i = 0; i < newGroupArray.size(); i++) {
                        groupList.add((JSONObject) newGroupArray.get(i));
                    }
                    xLayout.setStatus(XLayout.Success);
                    listAdapter.notifyDataSetChanged();
                    SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_groups_saved", newGroupArray);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_LIST, ggHttpInterface).startGet();
    }

    private class ListAdapter extends CommonAdapter<JSONObject> {

        private ListAdapter(Context context, int layoutId, List<JSONObject> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, JSONObject groupObject, int position) {
            ImageView avatarIv = holder.getView(R.id.head_image);
            TextView timeView = holder.getView(R.id.last_time);
            TextView nameView = holder.getView(R.id.whose_message);

            KLog.e(groupObject.toString());

            ImageDisplay.loadFileImage(getmContext(), new File(ImageUtils.getBitmapFilePaht(groupObject.getString("conv_id"), "imagecache")), avatarIv);
            holder.setText(R.id.last_message, groupObject.getJSONObject("attr").getString("intro") != null ? groupObject.getJSONObject("attr").getString("intro") : "");
            nameView.setMaxWidth(AppDevice.getWidth(getActivity()) - 130);
            nameView.setText(groupObject.getString("name"));
            timeView.setText("(" + groupObject.getString("m_size") + ")");
        }
    }

}
