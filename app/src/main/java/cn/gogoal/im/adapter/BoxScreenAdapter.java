package cn.gogoal.im.adapter;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.BoxScreenData;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :直播列表分类的Grid列表
 */
public class BoxScreenAdapter extends CommonAdapter<BoxScreenData, BaseViewHolder> {

    private List<BoxScreenData> list;

    private ClassifyItemClickListener listener;

    public void setOnClassifyChangeListener(ClassifyItemClickListener listener) {
        this.listener = listener;
    }

    public BoxScreenAdapter(List<BoxScreenData> list) {
        super(R.layout.item_box_screen, list);

        this.list=list;

    }

    @Override
    protected void convert(BaseViewHolder holder, final BoxScreenData data, final int position) {
        TextView holderView = holder.getView(R.id.textProName);
        holderView.setText(data.getProgramme_name());

        if (data.isSelected()) {
            holderView.setBackgroundResource(R.drawable.screen_bg_checked);
            holderView.setTextColor(getResColor(R.color.colorPrimary));
        } else {
            holderView.setBackgroundResource(R.drawable.screen_bg_unchecked);
            holderView.setTextColor(getResColor(R.color.textColor_333333));
        }

        holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOther(data);

                if (listener != null) {
                    listener.itemClick(data, position);
                }
            }
        });
    }

    private void toggleOther(BoxScreenData data) {
        data.setSelected(true);

        for (BoxScreenData d:list){
            if (d.getProgramme_id()!=data.getProgramme_id()){
                d.setSelected(false);
            }
        }

        notifyDataSetChanged();
    }

    public interface ClassifyItemClickListener {
        void itemClick(BoxScreenData data, int pos);
    }

}
