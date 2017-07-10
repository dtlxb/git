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

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.MyBaseAdapter;
import cn.gogoal.im.common.AppDevice;

/**
 * Created by dave.
 * Date: 2017/6/14.
 * Desc: description
 */
public class FinancialAnalysisRightAdapter extends MyBaseAdapter {

    private ArrayList<JSONArray> contList;
    private Context context;

    private int screenWidth;

    public FinancialAnalysisRightAdapter(Context context, ArrayList list) {
        super(list);
        this.context = context;
        this.contList = list;

        screenWidth = AppDevice.getWidth(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_financial_right, parent, false);
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
            holder.textReport1.setLines(1);
            holder.textReport2.setLines(1);
            holder.textReport3.setLines(1);
            holder.textReport4.setLines(1);
            holder.textReport5.setLines(1);
        } else {
            holder.textReport5.setLines(2);
            holder.textReport1.setLines(2);
            holder.textReport2.setLines(2);
            holder.textReport3.setLines(2);
            holder.textReport4.setLines(2);
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                screenWidth - AppDevice.dp2px(context, 155), LinearLayout.LayoutParams.MATCH_PARENT);
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

        if (contList.get(position) == null || contList.get(position).size() == 0) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.textReport1.setText("--");
        } else if (contList.get(position).size() == 1) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
        } else if (contList.get(position).size() == 2) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.relaReport2.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.imgReport1.setVisibility(View.VISIBLE);
            } else {
                holder.imgReport1.setVisibility(View.INVISIBLE);
            }
            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
        } else if (contList.get(position).size() == 3) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.relaReport2.setVisibility(View.VISIBLE);
            holder.relaReport3.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.imgReport1.setVisibility(View.VISIBLE);
                holder.imgReport2.setVisibility(View.VISIBLE);
            } else {
                holder.imgReport1.setVisibility(View.INVISIBLE);
                holder.imgReport2.setVisibility(View.INVISIBLE);
            }
            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
        } else if (contList.get(position).size() == 4) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.relaReport2.setVisibility(View.VISIBLE);
            holder.relaReport3.setVisibility(View.VISIBLE);
            holder.relaReport4.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.imgReport1.setVisibility(View.VISIBLE);
                holder.imgReport2.setVisibility(View.VISIBLE);
                holder.imgReport3.setVisibility(View.VISIBLE);
            } else {
                holder.imgReport1.setVisibility(View.INVISIBLE);
                holder.imgReport2.setVisibility(View.INVISIBLE);
                holder.imgReport3.setVisibility(View.INVISIBLE);
            }
            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
            holder.textReport4.setText(contList.get(position).getString(3) != null ?
                    contList.get(position).getString(3) : "--");
        } else if (contList.get(position).size() == 5) {
            holder.relaReport1.setVisibility(View.VISIBLE);
            holder.relaReport2.setVisibility(View.VISIBLE);
            holder.relaReport3.setVisibility(View.VISIBLE);
            holder.relaReport4.setVisibility(View.VISIBLE);
            holder.relaReport5.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.imgReport1.setVisibility(View.VISIBLE);
                holder.imgReport2.setVisibility(View.VISIBLE);
                holder.imgReport3.setVisibility(View.VISIBLE);
                holder.imgReport4.setVisibility(View.VISIBLE);
            } else {
                holder.imgReport1.setVisibility(View.INVISIBLE);
                holder.imgReport2.setVisibility(View.INVISIBLE);
                holder.imgReport3.setVisibility(View.INVISIBLE);
                holder.imgReport4.setVisibility(View.INVISIBLE);
            }
            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
            holder.textReport4.setText(contList.get(position).getString(3) != null ?
                    contList.get(position).getString(3) : "--");
            holder.textReport5.setText(contList.get(position).getString(4) != null ?
                    contList.get(position).getString(4) : "--");
        }

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearAnalysis;
        private RelativeLayout relaReport1, relaReport2, relaReport3, relaReport4, relaReport5;
        private TextView textReport1, textReport2, textReport3, textReport4, textReport5;
        private ImageView imgReport1, imgReport2, imgReport3, imgReport4;
    }
}
