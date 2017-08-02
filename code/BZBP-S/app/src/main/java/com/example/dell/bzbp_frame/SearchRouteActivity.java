package com.example.dell.bzbp_frame;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchRouteActivity extends BaseActivity {
    public static String ip;

    private ListView list_searchroute;
    private TextView textView_searchroute_no_route;

    private List<Map<String, Object>> mData;
    private ArrayList<Route> resultlist = new ArrayList<Route>();
    private Bundle bundle;

    @Override
    protected void initData() {
        bundle = this.getIntent().getExtras();//获取前一activity传递的信息
        ip = this.getString(R.string.ipv4);

        Posto temp = new Posto();
        temp.setLatitude((Double) bundle.getDouble("latitude"));
        temp.setLongitude((Double) bundle.getDouble("longitude"));

        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getRoutesByLocation");
        myThread1.setPosto(temp);
        myThread1.setWhat(4);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resultlist = myThread1.getRoutes();
        mData = getData();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_route_search);
        textView_searchroute_no_route = (TextView)findViewById(R.id.text_view_searchroute_noroute);
        list_searchroute = (ListView)findViewById(R.id.list_searchroute);
        MyAdapter adapter = new MyAdapter(this);
        list_searchroute.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        if(resultlist.size() == 0){
            textView_searchroute_no_route.setText("没有找到附近的route！QAQ");
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("routelist_name", resultlist.get(i).getName());
            map.put("routelist_date", resultlist.get(i).getStart_time());
            list.add(map);
        }
        return list;
    }

    public final class ViewHolder{
        public TextView name;
        public TextView date;
        public Button button;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Route temp = resultlist.get(position);
            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.routelist, null);
                holder.name = (TextView)convertView.findViewById(R.id.routelist_name);
                holder.date = (TextView)convertView.findViewById(R.id.routelist_date);
                holder.button = (Button)convertView.findViewById(R.id.routelist_button);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }



            holder.name.setText("name:"+(String)mData.get(position).get("routelist_name"));

            DateFormat format= new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            String time = format.format((Long)mData.get(position).get("routelist_date"));
            holder.date.setText("time:"+time);

            holder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SearchRouteActivity.this,RouteDetailActivity.class);
                    Bundle intent_bundle = new Bundle();

                    intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                    intent_bundle.putSerializable("route",temp);
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
            });
            return convertView;
        }

    }

}
