package cn.gogoal.im.adapter.market;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.StickyLayoutHelper;

import cn.gogoal.im.R;


/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :悬浮标题.
 */
public class TitleAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private String titleText;

    public TitleAdapter(String titleText) {
        this.titleText = titleText;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new StickyLayoutHelper(true);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view_market,parent,false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.tv_stock_ranklist_title);
        title.setText(titleText);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
