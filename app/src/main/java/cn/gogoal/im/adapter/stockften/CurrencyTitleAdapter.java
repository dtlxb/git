package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.StickyLayoutHelper;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */
public class CurrencyTitleAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private String title;//标题内容
    private Context context;

    public CurrencyTitleAdapter(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new StickyLayoutHelper(true);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(
                R.layout.layout_sticky_header_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.setText(R.id.tv_sticky_header_view, title);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
