package com.example.dell.bzbp_frame.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bzbp_frame.R;
import com.example.dell.bzbp_frame.SearchPostoActivity;
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends ListFragment {

    private Context mContext;
    private Bundle bundle;
    private User user;
    private ArrayList<User> resultlist = new ArrayList<User>();
    private List<Map<String, Object>> mData;
    public static String ip;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        Bundle bundle = getArguments();

        user = (User)bundle.getSerializable("user");
        ip = this.getString(R.string.ipv4);

        init();
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("friendlist_username", resultlist.get(i).getUsername());
            list.add(map);
        }
        return list;
    }


    public final class ViewHolder{
        public TextView username;
        public Button viewBtn;
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
            final User temp = resultlist.get(position);
            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.friendlist, null);
                holder.username = (TextView)convertView.findViewById(R.id.text_view_friendlist_username);
                holder.viewBtn = (Button)convertView.findViewById(R.id.button_friendlist);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.username.setText((String)mData.get(position).get("friendlist_username"));

            holder.username.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Posto getfriendsdetail = new Posto();
                    getfriendsdetail.setPid(user.getId());
                    getfriendsdetail.setUsername(temp.getUsername());
                    Intent i = new Intent(mContext, SearchPostoActivity.class);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",user);
                    intent_bundle.putSerializable("getfriendsdetail",getfriendsdetail);
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
            });

            holder.viewBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Posto deletefriends = new Posto();
                    deletefriends.setPid(user.getId());
                    deletefriends.setBelong_rid(temp.getId());
                    MyThread myThread1 = new MyThread();
                    myThread1.setGetUrl("http://" + ip + "/rest/deleteFriend");
                    myThread1.setPosto(deletefriends);
                    myThread1.setWhat(9);
                    myThread1.start();
                    try {
                        myThread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(mContext,"删除成功", Toast.LENGTH_LONG).show();
                    init();
                }
            });



            return convertView;
        }

    }

    private void init(){
        Posto getfriends = new Posto();
        getfriends.setPid(user.getId());
        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getFriendsById");
        myThread1.setPosto(getfriends);
        myThread1.setWhat(9);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultlist = myThread1.getUsers();
        if(resultlist.size() == 0){
            Toast.makeText(mContext,"没有好友QAQ", Toast.LENGTH_LONG).show();
        }
        mData = getData();
        MyAdapter adapter = new MyAdapter(mContext);
        setListAdapter(adapter);
    }

    //fragment切换时更新数据
    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd == false) {
            init();
        } else {
            //onpause
        }
    }
}
