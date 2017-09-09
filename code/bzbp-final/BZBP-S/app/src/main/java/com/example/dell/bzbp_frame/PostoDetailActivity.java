package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.lang.Math.abs;

public class PostoDetailActivity extends BaseActivity {

    private Bundle bundle;
    private User user;
    private Posto posto;
    private Bitmap bit;

    private ArrayList<Comment> resultlist = new ArrayList<Comment>();
    private Integer result_praise;

    private ImageView image_view_postodetail_image;
    private TextView textView_postodetail_name;
    private TextView textView_postodetail_comment;
    private TextView textView_postodetail_username;
    private TextView textView_postodetail_date;
    private TextView textView_postodetail_praise;
    private Button button_postodetail_make_comment;
    private Button button_postodetail_make_praise;
    private Button button_postodetail_gps;
    private ListView list_postodetail_commentlist;

    public static String ip;
    KeyMapDailog dialog;

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
        bundle = this.getIntent().getExtras();
        posto = (Posto) bundle.getSerializable("posto");
        user = (User) bundle.getSerializable("user");
        //获取图片
        String picture=posto.getImage();
        byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        //获取评论
        MyThread myThread1 = new MyThread();
        submit(myThread1,ip+"/rest/getComments",posto,6);
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
        submit(myThread2,ip+"/rest/getPraises",getpraise,8);
        result_praise = myThread2.getRid();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_posto_detail);
        textView_postodetail_praise = (TextView)this.findViewById(R.id.text_view_postodetail_praise);
        textView_postodetail_name = (TextView)this.findViewById(R.id.text_view_postodetail_name);
        textView_postodetail_comment = (TextView)this.findViewById(R.id.text_view_postodetail_comment);
        textView_postodetail_username = (TextView)this.findViewById(R.id.text_view_postodetail_username);
        textView_postodetail_date = (TextView)this.findViewById(R.id.text_view_postodetail_date);

        image_view_postodetail_image = (ImageView)this.findViewById(R.id.image_view_postodetail_image);

        button_postodetail_make_comment = (Button)this.findViewById(R.id.button_postodetail_make_comment);
        button_postodetail_make_praise = (Button)this.findViewById(R.id.button_postodetail_make_praise);
        list_postodetail_commentlist = (ListView)this.findViewById(R.id.list_postodetail_commentlist);
    }

    @Override
    protected void initListener() {
        //浏览者发表评论的button
        button_postodetail_make_comment.setOnClickListener(new View.OnClickListener() {
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
                                comment.setPid(posto.getPid());
                                comment.setUsername(user.getUsername());
                                comment.setContext(inputText);

                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Long time = timestamp.getTime();// 直接转换成long
                                comment.setDate(time);

                                MyThread myThread1 = new MyThread();
                                submit(myThread1,ip+"/rest/addComment",comment,5);

                                Toast.makeText(PostoDetailActivity.this, "发表成功", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                                refresh();
                            }
                        }, 2000);
                    }
                });
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        //点赞button
        button_postodetail_make_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result_praise < 0) {
                    Toast.makeText(PostoDetailActivity.this, "您已经点赞过", Toast.LENGTH_LONG).show();
                } else {
                    Praise praise = new Praise();
                    praise.setPid(posto.getPid());
                    praise.setUsername(user.getUsername());

                    MyThread myThread1 = new MyThread();
                    submit(myThread1,ip + "/rest/addPraise",praise,7);
                    //重新显示点赞数
                    result_praise += 1;
                    result_praise = -result_praise;
                    textView_postodetail_praise.setText("praise:"+abs(result_praise));
                }
            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        //显示poto信息
        textView_postodetail_name.setText("name："+posto.getName());
        textView_postodetail_comment.setText("comment："+posto.getComment());
        textView_postodetail_username.setText("username："+posto.getUsername());

        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String time = format.format(posto.getDate());

        textView_postodetail_date.setText("date："+time);
        //显示图片
        image_view_postodetail_image.setImageBitmap(bit);
        //显示赞数
        textView_postodetail_praise.setText("praise:"+abs(result_praise));
        //adapter
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.commentlist,
                new String[]{
                        "commentlist_username",
                        "commentlist_comment",
                        "commentlist_date"},
                new int[]{
                        R.id.commentlist_username,
                        R.id.commentlist_comment,
                        R.id.commentlist_date,
                });
        list_postodetail_commentlist.setAdapter(adapter);
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
    //提交之后刷新评论区
    private void refresh() {
        //获取评论
        MyThread myThread1 = new MyThread();
        submit(myThread1,ip+"/rest/getComments",posto,6);
        resultlist = myThread1.getComments();
        //没有评论的情况
        if(resultlist.size() == 0){
            Comment emptyComment = new Comment();
            emptyComment.setContext("暂无评论QAQ");
            resultlist.add(emptyComment);
        }
        //adapter
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.commentlist,
                new String[]{
                        "commentlist_username",
                        "commentlist_comment",
                        "commentlist_date"},
                new int[]{
                        R.id.commentlist_username,
                        R.id.commentlist_comment,
                        R.id.commentlist_date,
                });
        list_postodetail_commentlist.setAdapter(adapter);
    }
}
