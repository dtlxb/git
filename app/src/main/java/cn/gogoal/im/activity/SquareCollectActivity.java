package cn.gogoal.im.activity;

import android.content.Context;
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
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
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
    private List<JSONObject> objectList;

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
        objectList = new ArrayList<>();
        if (null == groupsArray || groupsArray.size() == 0) {
            getGroupList();
        } else {
            for (int i = 0; i < groupsArray.size(); i++) {
                objectList.add((JSONObject) groupsArray.get(i));
            }
        }
        listAdapter = new ListAdapter(SquareCollectActivity.this, R.layout.item_fragment_message, objectList);
        squareRoomRecycler.setAdapter(listAdapter);

    }

    //收藏群
    public void getGroupList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                Log.e("====responseInfo", responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONArray newGroupArray = (JSONArray) result.get("data");
                    for (int i = 0; i < newGroupArray.size(); i++) {
                        objectList.add((JSONObject) newGroupArray.get(i));
                    }
                    listAdapter.notifyDataSetChanged();
                    SPTools.saveJsonArray(UserUtils.getToken() + "_groups_saved", newGroupArray);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
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
            timeView.setVisibility(View.GONE);

            KLog.e(groupObject.getString("m_size"));
            File filePath = SquareCollectActivity.this.getExternalFilesDir("imagecache");

            ImageDisplay.loadFileImage(getmContext(), new File(ImageUtils.getBitmapFile(groupObject.getString("conv_id"), filePath)), avatarIv);
            holder.setText(R.id.last_message, groupObject.getJSONObject("attr").getString("intro") == null ? groupObject.getJSONObject("attr").getString("intro") : "");
            holder.setText(R.id.whose_message, groupObject.getString("name") + "(" + groupObject.getString("m_size") + ")");
        }
    }

}
