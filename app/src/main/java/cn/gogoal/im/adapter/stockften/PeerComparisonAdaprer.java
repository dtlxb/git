package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.f10.PeerCompariData;
import cn.gogoal.im.common.AppDevice;

/**
 * Created by dave.
 * Date: 2017/6/29.
 * Desc: description
 */
public class PeerComparisonAdaprer extends CommonAdapter<PeerCompariData, BaseViewHolder> {

    public static final int TYPE_MARKET_CAP = 1; //总市值
    public static final int TYPE_GROSS_INCOME = 2; //总收入
    public static final int TYPE_NET_PROFIT = 3; //净利润

    private Context context;
    private String value;
    private int peerType;
    private int proWidth;

    public PeerComparisonAdaprer(Context context, ArrayList<PeerCompariData> data, String value,
                                 int screenWidth, int peerType) {
        super(R.layout.item_peer_comparison, data);
        this.context = context;
        this.value = value;
        this.peerType = peerType;

        proWidth = (screenWidth - AppDevice.dp2px(context, 30)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder holder, PeerCompariData data, int position) {

        final TextView textPropor = holder.getView(R.id.textPropor);

        if (position == 1 || position == 2) {
            holder.setText(R.id.textRank, "");
            textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.hotsearch_list_number2));
        } else {
            holder.setText(R.id.textRank, data.getRanking());
            if (Double.parseDouble(data.getStock_proportion()) < 0) {
                textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.stock_green));
            } else {
                textPropor.setBackgroundColor(ContextCompat.getColor(context, R.color.hotsearch_list_number1));
            }
        }

        holder.setText(R.id.textStockName, data.getStock_title());

        int valWidth = (int) (proWidth * Math.abs(Double.parseDouble(data.getStock_proportion()))
                / Math.abs(Double.parseDouble(value)));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(valWidth < 1 ? 1 : valWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textPropor.setLayoutParams(param);

        holder.setText(R.id.textValue, data.getStock_proportion());
    }
}
