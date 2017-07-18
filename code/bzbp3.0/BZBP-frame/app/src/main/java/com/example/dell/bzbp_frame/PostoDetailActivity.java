package com.example.dell.bzbp_frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bzbp_frame.model.Comment;
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.KeyMapDailog;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostoDetailActivity extends AppCompatActivity {
    private Bundle bundle;
    KeyMapDailog dialog;
    private ArrayList<Comment> resultlist = new ArrayList<Comment>();
    public static String ip="192.168.1.97:8080/BookStore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_detail);
        //获取bundle
        bundle = this.getIntent().getExtras();
        //获取posto
        Posto posto = (Posto) bundle.getSerializable("posto");

        //获取并显示图片
        Bitmap bit;
        String picture=posto.getImage();
        byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView) findViewById(R.id.postodetail_image)).setImageBitmap(bit);
        //显示poto信息

        ((TextView)this.findViewById(R.id.postodetail_name)).setText("name："+posto.getName());
        ((TextView)this.findViewById(R.id.postodetail_comment)).setText("comment："+posto.getComment());
        ((TextView)this.findViewById(R.id.postodetail_username)).setText("username："+posto.getUsername());
        ((TextView)this.findViewById(R.id.postodetail_date)).setText("date："+posto.getDate());

        ListView commentlist = (ListView) findViewById(R.id.commentlist);


        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getComments");
        myThread1.setPosto(posto);
        myThread1.setWhat(6);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultlist = myThread1.getComments();

        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.commentlist,
                new String[]{
                        "commentlist_username","commentlist_comment",
                        "commentlist_date"},
                new int[]{
                        R.id.commentlist_username,
                        R.id.commentlist_comment,
                        R.id.commentlist_date,
                });

        commentlist.setAdapter(adapter);


        //浏览者发表评论的button
        findViewById(R.id.postodetail_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new KeyMapDailog("评论：", new KeyMapDailog.SendBackListener() {
                    @Override
                    public void sendBack(final String inputText) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.hideProgressdialog();
                                Comment comment = new Comment();

                                comment.setPid(((Posto)bundle.getSerializable("posto")).getPid());
                                comment.setUid(((User)bundle.getSerializable("user")).getId());
                                comment.setUsername(((User)bundle.getSerializable("user")).getUsername());
                                comment.setContext(inputText);

                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Long time = timestamp.getTime();// 直接转换成long
                                comment.setDate(time);

                                MyThread myThread1 = new MyThread();
                                myThread1.setGetUrl("http://"+ip+"/rest/addComment");
                                myThread1.setWhat(5);
                                myThread1.setComment(comment);
                                myThread1.start();

                                try {
                                    myThread1.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                Toast.makeText(PostoDetailActivity.this, "发表成功", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }, 2000);
                    }
                });

                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });




    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("commentlist_username", resultlist.get(i).getUsername());
            map.put("commentlist_comment", resultlist.get(i).getContext());
            map.put("commentlist_date", resultlist.get(i).getDate());
            list.add(map);
        }


        return list;
    }
}
