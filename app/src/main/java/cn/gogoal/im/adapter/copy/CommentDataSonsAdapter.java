package cn.gogoal.im.adapter.copy;

import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import hply.com.niugu.bean.CommentData_Sons;

/**
 * Created by daiwei on 2015/10/20.
 */
public class CommentDataSonsAdapter extends MyBaseAdapter<CommentData_Sons> {

    private ArrayList<CommentData_Sons> list;
    public CommentDataSonsAdapter(ArrayList<CommentData_Sons> list) {
        super(list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HoldView holdView;
        if (convertView == null) {
            holdView = new HoldView();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentdatasons_list_item, null);
            holdView.comment_sons= (TextView) convertView.findViewById(R.id.comment_sons);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }

        if(list.get(position).getUsername()!=null) {
            holdView.comment_sons.setText(list.get(position).getUsername() + "：" + list.get(position).getContent());
            SpannableStringBuilder builder = new SpannableStringBuilder(holdView.comment_sons.getText().toString());
            ForegroundColorSpan Span1 = new ForegroundColorSpan(ContextCompat.getColor(parent.getContext(),R.color.footer_text_color_selected));
            ForegroundColorSpan Span2 = new ForegroundColorSpan(ContextCompat.getColor(parent.getContext(),R.color.gray));
            builder.setSpan(Span1, 0, list.get(position).getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(Span2, list.get(position).getUsername().length(), holdView.comment_sons.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holdView.comment_sons.setText(builder);
        }else{
            holdView.comment_sons.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.gray));
            holdView.comment_sons.setText("    " + "：" + list.get(position).getContent());
        }
        return convertView;
    }

    class HoldView {
        TextView comment_sons;
    }
}
