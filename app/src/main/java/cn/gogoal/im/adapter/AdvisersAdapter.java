package cn.gogoal.im.adapter;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.Advisers;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class AdvisersAdapter extends CommonAdapter<Advisers, BaseViewHolder> {

    public AdvisersAdapter(List<Advisers> datas) {
        super(R.layout.item_dialog_advisers, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Advisers data, int position) {
        holder.setText(R.id.tv_advisers_phone, data.getSaler_mobile());
        holder.setText(R.id.tv_advisers_name, data.getSaler_name());
    }
}