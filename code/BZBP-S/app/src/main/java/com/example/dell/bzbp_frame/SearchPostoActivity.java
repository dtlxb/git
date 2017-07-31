package com.example.dell.bzbp_frame;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class SearchPostoActivity extends BaseActivity{

    public static String ip;

    private ListView list_searchposto;
    private TextView textView_searchposto_no_posto;

    private List<Map<String, Object>> mData;
    private ArrayList<Posto> resultlist = new ArrayList<Posto>();
    private Bundle bundle;
    private User user;
    private Route route;


    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
        //获取前一activity传递的信息
        bundle = this.getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        route = (Route)bundle.getSerializable("route");

        //从routedetail中查看包含的posto会传一个“route”,否则为直接按位置搜索
        if(null==route){
            //将用户位置&用户名写入posto中上传
            Posto temp = new Posto();
            temp.setUsername(user.getUsername());
            temp.setLatitude((Double) bundle.getDouble("latitude"));
            temp.setLongitude((Double) bundle.getDouble("longitude"));

            MyThread myThread1 = new MyThread();
            myThread1.setGetUrl("http://" + ip + "/rest/getPostosByLocation");
            myThread1.setPosto(temp);
            myThread1.setWhat(2);
            myThread1.start();
            try {
                myThread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获得posto列表
            resultlist = myThread1.getPostos();

        }else{

            Posto temp = new Posto();
            temp.setPid(route.getRid());
            temp.setUsername(user.getUsername());
            MyThread myThread1 = new MyThread();
            myThread1.setGetUrl("http://" + ip + "/rest/getPostosByRouteId");
            myThread1.setPosto(temp);
            myThread1.setWhat(2);
            myThread1.start();
            try {
                myThread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获得posto列表
            resultlist = myThread1.getPostos();
        }

        mData = getData();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_posto_search);
        textView_searchposto_no_posto = (TextView)findViewById(R.id.text_view_searchposto_noposto);
        list_searchposto = (ListView)findViewById(R.id.list_searchposto);
        MyAdapter adapter = new MyAdapter(this);
        list_searchposto.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        if(resultlist.size() == 0){
            textView_searchposto_no_posto.setText("没有找到附近的posto！QAQ");
        }
    }

    //将获得的posto拆分
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            Bitmap bit;
            String picture=resultlist.get(i).getImage();
            if(resultlist.get(i).getPid()>=0) {
                byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
                bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                map.put("postolist_image",bit);
                map.put("postolist_username", resultlist.get(i).getUsername());
                map.put("postolist_comment", resultlist.get(i).getComment());
                map.put("postolist_date", resultlist.get(i).getDate());
            }
            map.put("postolist_name", resultlist.get(i).getName());
            list.add(map);
        }


        return list;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView name;
        public TextView comment;
        public TextView username;
        public TextView date;
        public Button viewBtn;
    }

    public class MyAdapter extends BaseAdapter {

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
            final Posto temp = resultlist.get(position);
            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.postolist, null);

                holder.username = (TextView)convertView.findViewById(R.id.postolist_username);

                if(temp.getPid()!=-1) {
                    holder.viewBtn = (Button) convertView.findViewById(R.id.postolist_button);
                    holder.img = (ImageView)convertView.findViewById(R.id.postolist_image);
                    holder.name = (TextView)convertView.findViewById(R.id.postolist_name);
                    holder.comment = (TextView)convertView.findViewById(R.id.postolist_comment);
                    holder.date=(TextView)convertView.findViewById(R.id.postolist_date);
                }
                holder.name = (TextView)convertView.findViewById(R.id.postolist_name);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText("name:" + (String) mData.get(position).get("postolist_name"));
            if(temp.getPid()!=-1) {
                holder.img.setImageBitmap((Bitmap) mData.get(position).get("postolist_image"));

                //holder.comment.setText((String)mData.get(position).get("postolist_comment"));
                //holder.username.setText((String)mData.get(position).get("postolist_username"));

                DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                String time = format.format((Long) mData.get(position).get("postolist_date"));
                holder.date.setText("time:" + time);

                holder.viewBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SearchPostoActivity.this, PostoDetailActivity.class);
                        Bundle intent_bundle = new Bundle();

                        intent_bundle.putSerializable("user", bundle.getSerializable("user"));
                        intent_bundle.putSerializable("posto",temp);
                        i.putExtras(intent_bundle);
                        startActivity(i);
                    }
                });
            }

            return convertView;
        }

    }





}