package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;
import cn.gogoal.im.bean.f10.TenHolderData;
import cn.gogoal.im.common.AppDevice;

/**
 * Created by dave.
 * Date: 2017/6/15.
 * Desc: description
 */
public class TenHolderAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private ArrayList<TenHolderData> contList;
    private Context context;
    private int tenWidth;

    private double ratio = 0;

    public TenHolderAdapter(Context context, ArrayList<TenHolderData> list, int screenWidth, double ratio) {
        this.context = context;
        this.contList = list;
        this.ratio = ratio;
        tenWidth = (screenWidth - AppDevice.dp2px(context, 30)) / 2;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_ten_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {

        final TextView textPropor = holder.findView(R.id.textPropor);

        if (position == 0) {
            holder.findView(R.id.linearCont).setVisibility(View.VISIBLE);
        } else {
            holder.findView(R.id.linearCont).setVisibility(View.GONE);
        }

        if (position == 0) {
            holder.setTextResColor(R.id.textSerialNum, R.color.hotsearch_list_number1);
            textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.hotsearch_list_number1));
        } else if (position == 1) {
            textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.hotsearch_list_number2));
            holder.setTextResColor(R.id.textSerialNum, R.color.hotsearch_list_number2);
        } else if (position == 2) {
            textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.hotsearch_list_number3));
            holder.setTextResColor(R.id.textSerialNum, R.color.hotsearch_list_number3);
        } else {
            textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.textColor_666666));
            holder.setTextResColor(R.id.textSerialNum, R.color.textColor_666666);
        }

        holder.setText(R.id.textSerialNum, position + 1 + "");

        holder.setText(R.id.textName, contList.get(position).getStock_holder_name() != null
                ? contList.get(position).getStock_holder_name() : "--");
        holder.setText(R.id.textRatio, contList.get(position).getStock_holder_ratio() != null
                ? contList.get(position).getStock_holder_ratio() + "%" : "--");
        holder.setText(R.id.textQuantity, contList.get(position).getStock_holding_quantity() != null
                ? contList.get(position).getStock_holding_quantity() : "--");

        int valWidth = (int) (tenWidth * Double.parseDouble(
                contList.get(position).getStock_holder_ratio()) / ratio);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(valWidth < 1 ? 1 : valWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textPropor.setLayoutParams(param);
    }

    @Override
    public int getItemCount() {
        return contList.size();
    }
}
