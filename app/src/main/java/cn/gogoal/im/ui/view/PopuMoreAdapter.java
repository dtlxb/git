package cn.gogoal.im.ui.view;

import android.view.View;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.BaseIconText;

/**
 * author wangjd on 2017/6/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class PopuMoreAdapter extends CommonAdapter<BaseIconText<Integer, String>, BaseViewHolder> {
    private List<BaseIconText<Integer, String>> menuItemList;
    private PopuMoreMenu popuMoreMenu;
    private PopuMoreMenu.OnMenuItemClickListener onMenuItemClickListener;

    public void setOnMenuItemClickListener(PopuMoreMenu.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public PopuMoreAdapter(PopuMoreMenu popuMoreMenu, List<BaseIconText<Integer, String>> data) {
        super(R.layout.trm_item_popup_menu_list, data);
        menuItemList = data;
        this.popuMoreMenu = popuMoreMenu;
    }

    @Override
    protected void convert(final BaseViewHolder holder, BaseIconText<Integer, String> data, int position) {
        holder.setText(R.id.trm_menu_item_text, data.getText());
        holder.setImageResource(R.id.trm_menu_item_icon, data.getIamge());

        holder.setVisible(R.id.view_popu_divider,position==menuItemList.size()-1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuItemClickListener != null) {
                    popuMoreMenu.dismiss();
                    onMenuItemClickListener.onMenuItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

}
