package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */
public class CurrencyTitle2Adapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private String[] title;//标题内容
    private Context context;
    private boolean isClickable;

    public CurrencyTitle2Adapter(Context context, String[] title, boolean isClickable) {
        this.context = context;
        this.title = title;
        this.isClickable = isClickable;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper(1);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(
                R.layout.layout_linear_title_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.setText(R.id.textTitle, title[0]);
        holder.setText(R.id.textCont, title[1]);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
