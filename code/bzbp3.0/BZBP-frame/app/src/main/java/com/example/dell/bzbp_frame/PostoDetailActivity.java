package com.example.dell.bzbp_frame;

import android.content.Intent;
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
import com.example.dell.bzbp_frame.model.Praise;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.KeyMapDailog;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dell.bzbp_frame.R.id.postodetail_praise;
import static com.example.dell.bzbp_frame.R.id.routedetail_praise;
import static java.lang.Math.abs;

public class PostoDetailActivity extends AppCompatActivity {
    private Bundle bundle;
    KeyMapDailog dialog;
    private ArrayList<Comment> resultlist = new ArrayList<Comment>();
    private Integer result_praise;
    private TextView view_praise;
    public static String ip="192.168.1.97:8080/BookStore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_detail);
        //获取bundle
        init();
    }
    private void init(){
        //获取bundle
        bundle = this.getIntent().getExtras();
        view_praise = (TextView)this.findViewById(R.id.postodetail_praise);
        //获取posto,user
        final Posto posto = (Posto) bundle.getSerializable("posto");
        final User user = (User) bundle.getSerializable("user");
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



        //获取评论
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

        //没有评论的情况
        if(resultlist.size() == 0){
            Comment emptyComment = new Comment();
            emptyComment.setContext("暂无评论QAQ");
            resultlist.add(emptyComment);
        }

        //获取点赞信息,没有点过赞则为int 0
        Posto getpraise = new Posto();//仅上传username和posto，获取赞数和是否已经点赞
        getpraise.setUsername(user.getUsername());
        getpraise.setPid(posto.getPid());
        MyThread myThread2 = new MyThread();
        myThread2.setGetUrl("http://" + ip + "/rest/getPraises");
        myThread2.setPosto(getpraise);
        myThread2.setWhat(8);
        myThread2.start();
        try {
            myThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result_praise = myThread2.getRid();
        //显示赞数
        view_praise.setText("praise:"+abs(result_praise));


        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.commentlist,
                new String[]{
                        "commentlist_username","commentlist_comment",
                        "commentlist_date"},
                new int[]{
                        R.id.commentlist_username,
                        R.id.commentlist_comment,
                        R.id.commentlist_date,
                });

        ListView commentlist = (ListView) findViewById(R.id.postodetail_commentlist);
        commentlist.setAdapter(adapter);


        //浏览者发表评论的button
        findViewById(R.id.postodetail_make_comment).setOnClickListener(new View.OnClickListener() {
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
                                init();
                            }
                        }, 2000);
                    }
                });

                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        //点赞button
        this.findViewById(R.id.postodetail_make_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result_praise < 0) {
                    Toast.makeText(PostoDetailActivity.this, "您已经点赞过", Toast.LENGTH_LONG).show();
                } else {
                    Praise praise = new Praise();
                    praise.setPid(posto.getPid());
                    praise.setUsername(user.getUsername());



                    MyThread myThread1 = new MyThread();
                    myThread1.setGetUrl("http://" + ip + "/rest/addPraise");
                    myThread1.setWhat(7);
                    myThread1.setPraise(praise);
                    myThread1.start();

                    try {
                        myThread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //重新显示点赞数
                    result_praise += 1;
                    result_praise = -result_praise;
                    view_praise.setText("praise:"+abs(result_praise));
                }
            }
        });

    }
    //listview获取信息
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
