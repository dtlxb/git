package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :港股.
 */
public class HKFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
//        commingSoon(xLayout);
        xLayout.setStatus(XLayout.Success);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CommonAdapter<String>(mContext,android.R.layout.simple_list_item_1,getDatas()) {
            @Override
            protected void convert(ViewHolder holder, String data, int position) {
                UIHelper.setRippBg(holder.itemView);
                holder.setText(android.R.id.text1,data);
            }
        });
    }

    private void commingSoon(XLayout xLayout) {
        xLayout.setEmptyText(getString(R.string.str_coming_soon));
        xLayout.setEmptyImage(R.mipmap.img_dev_coming_soon);
        xLayout.setStatus(XLayout.Empty);

    }

    private List<String> getDatas() {
        List<String> list = new ArrayList<>();
        for (int i=0;i<500;i++){
            list.add("item"+i);
        }
        return list;
    }
}
