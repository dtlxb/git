package cn.gogoal.im.adapter.market;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.BigDataAcyivity;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :大数据选股
 */
public class BigDataAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_stock_makret_big_data, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        //主题选股
        holder.setOnClickListener(R.id.btn_item_stock_market_subject, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), BigDataAcyivity.class);
                intent.putExtra("big_index",1);//第一个tab
                v.getContext().startActivity(intent);
            }
        });

        //事件选股
        holder.setOnClickListener(R.id.btn_item_stock_market_event, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), BigDataAcyivity.class);
                intent.putExtra("big_index",0);//第一个tab
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
