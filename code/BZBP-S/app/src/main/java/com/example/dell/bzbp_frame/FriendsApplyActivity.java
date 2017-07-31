package com.example.dell.bzbp_frame;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsApplyActivity extends ListActivity {
    public static String ip = "192.168.1.97:8080/BookStore";

    private ListView listview;
    private List<Map<String, Object>> mData;
    private ArrayList<User> resultlist = new ArrayList<User>();
    private Bundle bundle;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取bundle
        bundle = this.getIntent().getExtras();
        //获取user
        user=(User)bundle.getSerializable("user");

        //获取好友列表
        Posto getfriendapply = new Posto();
        getfriendapply.setPid(user.getId());
        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getAppliesById");
        myThread1.setPosto(getfriendapply);
        myThread1.setWhat(9);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultlist = myThread1.getUsers();

        //没有好友的情况
        if(resultlist.size() == 0){
            User emptyUser = new User();
            emptyUser.setUsername("暂无申请QAQ");
            emptyUser.setId(-1);
            resultlist.add(emptyUser);
        }

        mData = getData();
        MyAdapter3 adapter = new MyAdapter3(this);
        setListAdapter(adapter);

    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("friendapplylist_username", resultlist.get(i).getUsername());
            list.add(map);
        }
        return list;
    }

    // ListView 中某项被选中后的逻辑
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

    public final class ViewHolder{
        public TextView username;
        public Button viewBtn;
    }

    public class MyAdapter3 extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter3(Context context){
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
            final User temp = resultlist.get(position);
            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.friendapplylist, null);
                holder.username = (TextView)convertView.findViewById(R.id.text_view_friendapplylist_username);
                holder.viewBtn = (Button)convertView.findViewById(R.id.button_friendapplylist);
                if(temp.getId()==-1) holder.viewBtn.setText("返回");
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.username.setText((String)mData.get(position).get("friendapplylist_username"));
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(temp.getId()==-1) FriendsApplyActivity.this.finish();
                    Posto agreeapply = new Posto();
                    agreeapply.setPid(temp.getId());
                    agreeapply.setBelong_rid(user.getId());
                    MyThread myThread1 = new MyThread();
                    myThread1.setGetUrl("http://" + ip + "/rest/addFriend");
                    myThread1.setPosto(agreeapply);
                    myThread1.setWhat(0);
                    myThread1.start();

                    try {
                        myThread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    onCreate(bundle);
                }
            });

            return convertView;
        }

    }






}
