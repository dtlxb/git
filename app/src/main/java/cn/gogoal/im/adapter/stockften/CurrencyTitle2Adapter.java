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

    public static final int TYPE_COMPANY_SUMMARY = 1; //公司概况
    public static final int TYPE_EQUITY = 2; //股本
    public static final int TYPE_SENIOR_EXECUTIVE = 3; //高管

    private String title;//标题内容
    private Context context;
    private int type;
    private String stockCode;
    private String stockName;

    public CurrencyTitle2Adapter(Context context, String title, int type, String stockCode,
                                 String stockName) {
        this.context = context;
        this.title = title;
        this.type = type;
        this.stockCode = stockCode;
        this.stockName = stockName;
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
        holder.setText(R.id.textTitle, title);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
