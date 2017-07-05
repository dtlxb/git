package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.MyBaseAdapter;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringFormatUtil;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by dave.
 * Date: 2017/6/14.
 * Desc: description
 */
public class FinancialStatementsRightAdapter extends MyBaseAdapter {

    private ArrayList<JSONObject> contList;
    private Context context;
    private String stype;

    private int screenWidth;

    public FinancialStatementsRightAdapter(Context context, ArrayList<JSONObject> list, String stype) {
        super(list);
        this.context = context;
        this.contList = list;
        this.stype = stype;

        screenWidth = AppDevice.getWidth(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_financial_statements_right, parent, false);
            holder = new ViewHolder();
            holder.linearAnalysis = (LinearLayout) convertView.findViewById(R.id.linearAnalysis);
            holder.relaReport1 = (RelativeLayout) convertView.findViewById(R.id.relaReport1);
            holder.relaReport2 = (RelativeLayout) convertView.findViewById(R.id.relaReport2);
            holder.relaReport3 = (RelativeLayout) convertView.findViewById(R.id.relaReport3);
            holder.relaReport4 = (RelativeLayout) convertView.findViewById(R.id.relaReport4);
            holder.relaReport5 = (RelativeLayout) convertView.findViewById(R.id.relaReport5);
            holder.textReport1 = (TextView) convertView.findViewById(R.id.textReport1);
            holder.textReport2 = (TextView) convertView.findViewById(R.id.textReport2);
            holder.textReport3 = (TextView) convertView.findViewById(R.id.textReport3);
            holder.textReport4 = (TextView) convertView.findViewById(R.id.textReport4);
            holder.textReport5 = (TextView) convertView.findViewById(R.id.textReport5);
            holder.imgReport1 = (ImageView) convertView.findViewById(R.id.imgReport1);
            holder.imgReport2 = (ImageView) convertView.findViewById(R.id.imgReport2);
            holder.imgReport3 = (ImageView) convertView.findViewById(R.id.imgReport3);
            holder.imgReport4 = (ImageView) convertView.findViewById(R.id.imgReport4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.imgReport1.setVisibility(View.VISIBLE);
            holder.imgReport2.setVisibility(View.VISIBLE);
            holder.imgReport3.setVisibility(View.VISIBLE);
            holder.imgReport4.setVisibility(View.VISIBLE);

            holder.textReport1.setLines(1);
            holder.textReport2.setLines(1);
            holder.textReport3.setLines(1);
            holder.textReport4.setLines(1);
            holder.textReport5.setLines(1);
        } else {
            holder.imgReport1.setVisibility(View.INVISIBLE);
            holder.imgReport2.setVisibility(View.INVISIBLE);
            holder.imgReport3.setVisibility(View.INVISIBLE);
            holder.imgReport4.setVisibility(View.INVISIBLE);

            holder.textReport1.setLines(2);
            holder.textReport2.setLines(2);
            holder.textReport3.setLines(2);
            holder.textReport4.setLines(2);
            holder.textReport5.setLines(2);
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                screenWidth - AppDevice.dp2px(context, 150), LinearLayout.LayoutParams.MATCH_PARENT);
        holder.relaReport1.setLayoutParams(param);
        holder.relaReport2.setLayoutParams(param);
        holder.relaReport3.setLayoutParams(param);
        holder.relaReport4.setLayoutParams(param);
        holder.relaReport5.setLayoutParams(param);

        if (position % 2 == 0) {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        if (position == 0) {
            JSONArray title = contList.get(position).getJSONArray("title");
            if (title.size() == 0) {
                holder.textReport1.setText("--");
                holder.textReport2.setText("--");
                holder.textReport3.setText("--");
                holder.textReport4.setText("--");
                holder.textReport5.setText("--");
            } else {
                holder.textReport1.setText(title.getString(0) != null ?
                        title.getString(0) : "--");
                holder.textReport2.setText(title.getString(1) != null ?
                        title.getString(1) : "--");
                holder.textReport3.setText(title.getString(2) != null ?
                        title.getString(2) : "--");
                holder.textReport4.setText(title.getString(3) != null ?
                        title.getString(3) : "--");
                holder.textReport5.setText(title.getString(4) != null ?
                        title.getString(4) : "--");
            }
        } else {
            JSONArray original_data = contList.get(position).getJSONArray("original_data");
            if (stype.equals("1")) {
                if (original_data.size() == 0) {
                    holder.textReport1.setText("--");
                    holder.textReport2.setText("--");
                    holder.textReport3.setText("--");
                    holder.textReport4.setText("--");
                    holder.textReport5.setText("--");
                } else {
                    holder.textReport1.setText(original_data.getString(0) != null ?
                            original_data.getString(0) : "--");
                    holder.textReport2.setText(original_data.getString(1) != null ?
                            original_data.getString(1) : "--");
                    holder.textReport3.setText(original_data.getString(2) != null ?
                            original_data.getString(2) : "--");
                    holder.textReport4.setText(original_data.getString(3) != null ?
                            original_data.getString(3) : "--");
                    holder.textReport5.setText(original_data.getString(4) != null ?
                            original_data.getString(4) : "--");
                }
            } else {
                JSONArray rate_data = contList.get(position).getJSONArray("rate_data");
                if (original_data.size() == 0) {
                    if (rate_data.size() == 0) {
                        holder.textReport1.setText("--\n--");
                        holder.textReport2.setText("--\n--");
                        holder.textReport3.setText("--\n--");
                        holder.textReport4.setText("--\n--");
                        holder.textReport5.setText("--\n--");
                    } else {
                        if (rate_data.getDoubleValue(0) > 0) {
                            holder.textReport1.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(0)), setRateData(rate_data.getString(0)),
                                    R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(0) < 0) {
                            holder.textReport1.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(0)), setRateData(rate_data.getString(0)),
                                    R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport1.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(0)), setRateData(rate_data.getString(0)),
                                    R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(1) > 0) {
                            holder.textReport2.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(1)), setRateData(rate_data.getString(1)),
                                    R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(1) < 0) {
                            holder.textReport2.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(1)), setRateData(rate_data.getString(1)),
                                    R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport2.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(1)), setRateData(rate_data.getString(1)),
                                    R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(2) > 0) {
                            holder.textReport3.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(2)), setRateData(rate_data.getString(2)),
                                    R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(2) < 0) {
                            holder.textReport3.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(2)), setRateData(rate_data.getString(2)),
                                    R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport3.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(2)), setRateData(rate_data.getString(2)),
                                    R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(3) > 0) {
                            holder.textReport4.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(3)), setRateData(rate_data.getString(3)),
                                    R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(3) < 0) {
                            holder.textReport4.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(3)), setRateData(rate_data.getString(3)),
                                    R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport4.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(3)), setRateData(rate_data.getString(3)),
                                    R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(4) > 0) {
                            holder.textReport5.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(4)), setRateData(rate_data.getString(4)),
                                    R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(4) < 0) {
                            holder.textReport5.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(4)), setRateData(rate_data.getString(4)),
                                    R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport5.setText(new StringFormatUtil(context, "--\n"
                                    + setRateData(rate_data.getString(4)), setRateData(rate_data.getString(4)),
                                    R.color.textColor_333333).fillColor().getResult());
                        }
                    }

                } else {
                    if (rate_data.size() == 0) {
                        holder.textReport1.setText(original_data.getString(0) != null ?
                                original_data.getString(0) : "--" + "\n--");
                        holder.textReport2.setText(original_data.getString(1) != null ?
                                original_data.getString(1) : "--" + "\n--");
                        holder.textReport3.setText(original_data.getString(2) != null ?
                                original_data.getString(2) : "--" + "\n--");
                        holder.textReport4.setText(original_data.getString(3) != null ?
                                original_data.getString(3) : "--" + "\n--");
                        holder.textReport5.setText(original_data.getString(4) != null ?
                                original_data.getString(4) : "--" + "\n--");
                    } else {
                        if (rate_data.getDoubleValue(0) > 0) {
                            holder.textReport1.setText(new StringFormatUtil(context,
                                    (original_data.getString(0) != null ? original_data.getString(0) : "--")
                                            + "\n" + setRateData(rate_data.getString(0)),
                                    setRateData(rate_data.getString(0)), R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(0) < 0) {
                            holder.textReport1.setText(new StringFormatUtil(context,
                                    (original_data.getString(0) != null ? original_data.getString(0) : "--")
                                            + "\n" + setRateData(rate_data.getString(0)),
                                    setRateData(rate_data.getString(0)), R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport1.setText(new StringFormatUtil(context,
                                    (original_data.getString(0) != null ? original_data.getString(0) : "--")
                                            + "\n" + setRateData(rate_data.getString(0)),
                                    setRateData(rate_data.getString(0)), R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(1) > 0) {
                            holder.textReport2.setText(new StringFormatUtil(context,
                                    (original_data.getString(1) != null ? original_data.getString(1) : "--")
                                            + "\n" + setRateData(rate_data.getString(1)),
                                    setRateData(rate_data.getString(1)), R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(1) < 0) {
                            holder.textReport2.setText(new StringFormatUtil(context,
                                    (original_data.getString(1) != null ? original_data.getString(1) : "--")
                                            + "\n" + setRateData(rate_data.getString(1)),
                                    setRateData(rate_data.getString(1)), R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport2.setText(new StringFormatUtil(context,
                                    (original_data.getString(1) != null ? original_data.getString(1) : "--")
                                            + "\n" + setRateData(rate_data.getString(1)),
                                    setRateData(rate_data.getString(1)), R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(2) > 0) {
                            holder.textReport3.setText(new StringFormatUtil(context,
                                    (original_data.getString(2) != null ? original_data.getString(2) : "--")
                                            + "\n" + setRateData(rate_data.getString(2)),
                                    setRateData(rate_data.getString(2)), R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(2) < 0) {
                            holder.textReport3.setText(new StringFormatUtil(context,
                                    (original_data.getString(2) != null ? original_data.getString(2) : "--")
                                            + "\n" + setRateData(rate_data.getString(2)),
                                    setRateData(rate_data.getString(2)), R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport3.setText(new StringFormatUtil(context,
                                    (original_data.getString(2) != null ? original_data.getString(2) : "--")
                                            + "\n" + setRateData(rate_data.getString(2)),
                                    setRateData(rate_data.getString(2)), R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(3) > 0) {
                            holder.textReport4.setText(new StringFormatUtil(context,
                                    (original_data.getString(3) != null ? original_data.getString(3) : "--")
                                            + "\n" + setRateData(rate_data.getString(3)),
                                    setRateData(rate_data.getString(3)), R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(3) < 0) {
                            holder.textReport4.setText(new StringFormatUtil(context,
                                    (original_data.getString(3) != null ? original_data.getString(3) : "--")
                                            + "\n" + setRateData(rate_data.getString(3)),
                                    setRateData(rate_data.getString(3)), R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport4.setText(new StringFormatUtil(context,
                                    (original_data.getString(3) != null ? original_data.getString(3) : "--")
                                            + "\n" + setRateData(rate_data.getString(3)),
                                    setRateData(rate_data.getString(3)), R.color.textColor_333333).fillColor().getResult());
                        }

                        if (rate_data.getDoubleValue(4) > 0) {
                            holder.textReport5.setText(new StringFormatUtil(context,
                                    (original_data.getString(4) != null ? original_data.getString(4) : "--")
                                            + "\n" + setRateData(rate_data.getString(4)),
                                    setRateData(rate_data.getString(4)), R.color.colorPrimary).fillColor().getResult());
                        } else if (rate_data.getDoubleValue(4) < 0) {
                            holder.textReport5.setText(new StringFormatUtil(context,
                                    (original_data.getString(4) != null ? original_data.getString(4) : "--")
                                            + "\n" + setRateData(rate_data.getString(4)),
                                    setRateData(rate_data.getString(4)), R.color.stock_green).fillColor().getResult());
                        } else {
                            holder.textReport5.setText(new StringFormatUtil(context,
                                    (original_data.getString(4) != null ? original_data.getString(4) : "--")
                                            + "\n" + setRateData(rate_data.getString(4)),
                                    setRateData(rate_data.getString(4)), R.color.textColor_333333).fillColor().getResult());
                        }
                    }
                }
            }
        }

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearAnalysis;
        private RelativeLayout relaReport1, relaReport2, relaReport3, relaReport4, relaReport5;
        private TextView textReport1, textReport2, textReport3, textReport4, textReport5;
        private ImageView imgReport1, imgReport2, imgReport3, imgReport4;
    }

    private String setRateData(String rate) {
        String str = "--";
        if (rate == null) {
            str = "--";
        } else {
            str = StringUtils.save2Significand(Double.parseDouble(rate) * 100) + "%";
        }
        return str;
    }
}
