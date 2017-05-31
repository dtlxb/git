package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BoxScreenData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

/**
 * author wangjd on 2017/5/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主Tab 直播列表
 */
public class LiveListFragment extends BaseFragment {
    @BindView(R.id.boxScreen)
    AppCompatCheckBox boxScreen;

    @BindView(R.id.tv_do_live)
    TextView tvDoLive;

    @BindView(R.id.rv_live_list)
    RecyclerView rvLiveList;

    @BindView(R.id.view_dialog_mask)
    View viewDialogMask;

    @BindView(R.id.rv_menu_list)
    RecyclerView rvMenuList;

    //========================menu========================
    private List<BoxScreenData> menuData;
    private BoxScreenAdapter boxScreenAdapter;
    //========================live list==============

    @Override
    public int bindLayout() {
        return R.layout.fragment_live_list;
    }

    @Override
    public void doBusiness(Context mContext) {
        //点击展开
        boxScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                new LiveMenuDialog().show(getChildFragmentManager());
            }
        });

        menuData = new ArrayList<>();
        boxScreenAdapter = new BoxScreenAdapter(menuData);
        rvMenuList.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        rvMenuList.setAdapter(boxScreenAdapter);

        //获取侧滑菜单中的分类数据
        getMenuData();
    }

    private void getMenuData() {
        new GGOKHTTP(null, GGOKHTTP.GET_PROGRAMME_GUIDE, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                KLog.e(responseInfo);

                JSONObject object = JSONObject.parseObject(responseInfo);

                if (object.getIntValue("code") == 0) {
                    List<BoxScreenData> data = JSONObject.parseArray(object.getString("data"), BoxScreenData.class);
                    menuData.addAll(data);

                    BoxScreenData allData=new BoxScreenData();
                    allData.setProgramme_name("全部");
                    menuData.add(0,allData);
                    boxScreenAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e("获取直播分类失败");
            }
        }).startGet();
    }

    private class BoxScreenAdapter extends CommonAdapter<BoxScreenData, BaseViewHolder> {

        private BoxScreenAdapter(List<BoxScreenData> list) {
            super(R.layout.item_box_screen, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, final BoxScreenData data, final int position) {
            holder.setText(R.id.textProName,data.getProgramme_name());
        }
    }

}