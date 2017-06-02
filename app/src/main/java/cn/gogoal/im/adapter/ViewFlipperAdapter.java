package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.FlipperData;

/**
 * author wangjd on 2017/6/1 0001.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ViewFlipperAdapter extends BaseAdapter {

    private FlipperClickListener listener;
    private Context context;

    private List<FlipperData> flipperDataList;

    public ViewFlipperAdapter(Context context, List<FlipperData> flipperDataList) {
        this.context = context;
        this.flipperDataList = flipperDataList;
    }

    public void setOnFlipperClickListener(FlipperClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return flipperDataList == null ? 0 : flipperDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return flipperDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_flipper, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_item_view_flipper);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.textView.setSingleLine(false);

        holder.textView.setText(flipperDataList.get(position).getType_name()+" :"+
                flipperDataList.get(position).getContent());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.click(v, position);
                }
            }
        });

        holder.textView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.down_to_center));

        return convertView;
    }


    public interface FlipperClickListener {
        void click(View view, int position);
    }

    public static class ViewHolder {
        private TextView textView;
    }
}
