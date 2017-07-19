package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

public class FriendsActivity extends AppCompatActivity {

    private Bundle bundle;
    private User user;
    private ArrayList<User> resultlist = new ArrayList<User>();
    public static String ip="192.168.1.97:8080/BookStore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        //获取bundle
        bundle = this.getIntent().getExtras();
        //获取user
        user=(User)bundle.getSerializable("user");

        final EditText editText_name=(EditText)findViewById(R.id.edittext_share_name);

        //获取好友列表
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

        //没有好友的情况
        if(resultlist.size() == 0){
            User emptyUser = new User();
            emptyUser.setUsername("暂无好友QAQ");
            resultlist.add(emptyUser);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.friendlist,
                new String[]{
                        "friendlist_username"},
                new int[]{
                        R.id.friendlist_username
                });

        ListView friendlist = (ListView) findViewById(R.id.friends_list);
        friendlist.setAdapter(adapter);


        final EditText editText_username=(EditText)findViewById(R.id.edit_add_friend);

        this.findViewById(R.id.button_add_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------
                Posto temp = new Posto();//pid为自己，rid为输入的目标id
                temp.setPid(user.getId());
                temp.setBelong_rid(Integer.parseInt(editText_username.getText().toString()));

                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/applyFriend");
                myThread1.setWhat(0);
                myThread1.setPosto(temp);
                myThread1.start();

                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String apply_result = myThread1.getResult();
                int apply_result_int = Integer.parseInt(apply_result);
                if(apply_result_int<0){
                    
                }



            }
        });

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

}
