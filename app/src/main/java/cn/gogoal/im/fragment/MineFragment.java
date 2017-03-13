package cn.gogoal.im.fragment;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ImageTextBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.UIHelper;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.rv_mine)
    RecyclerView rvMine;

    @BindView(R.id.item_mine)
    LinearLayout itemSetting;

    public MineFragment() {
    }

    @BindArray(R.array.mine_arr)
    String[] mineTitle;

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(getString(R.string.title_mine));
        itemSetting.setPadding(AppDevice.dp2px(mContext,15),0,AppDevice.dp2px(mContext,15),0);
        rvMine.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(
                mContext, LinearLayoutManager.VERTICAL);
        decoration.setDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.shape_normal_list_divider_1dp));
        rvMine.addItemDecoration(decoration);
        rvMine.setAdapter(new MineAdapter(mContext,iniData()));
    }

    private List<ImageTextBean<Integer>> iniData() {
        List<ImageTextBean<Integer>> list = new ArrayList<>();
        for (String con : mineTitle) {
            list.add(new ImageTextBean<>(R.mipmap.ic_launcher, con));
        }
        return list;
    }

    private class MineAdapter extends CommonAdapter<ImageTextBean<Integer>> {

        private MineAdapter(Context context,List<ImageTextBean<Integer>> datas) {
            super(context, R.layout.item_mine, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ImageTextBean<Integer> data, int position) {
            holder.setImageResource(R.id.iv_mine_item_icon, data.getIamge());
            holder.setText(R.id.tv_mine_item_text, data.getText());
            UIHelper.setRippBg(holder.itemView);
        }
    }
}
