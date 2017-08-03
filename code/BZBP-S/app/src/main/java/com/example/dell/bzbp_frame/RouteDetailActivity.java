package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.dell.bzbp_frame.fragment.FriendsAddFragment;
import com.example.dell.bzbp_frame.model.Comment;
import com.example.dell.bzbp_frame.model.MyLatlng;
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


import static java.lang.Math.abs;

public class RouteDetailActivity extends BaseActivity implements AMap.OnMapLoadedListener{

    private Bundle bundle;
    private User user;
    private Route route;

    private ArrayList<Comment> resultlist = new ArrayList<Comment>();
    private Integer result_praise;

    private MapView mapView_routedetail;

    private TextView textView_routedetail_name;
    private TextView textView_routedetail_comment;
    private TextView textView_routedetail_username;
    private TextView textView_routedetail_date;
    private TextView textView_postodetail_praise;
    private Button button_routedetail_make_comment;
    private Button button_routedetail_make_praise;
    private Button button_routedetail_postos;
    private ListView list_routedetail_commentlist;

    public static String ip;
    KeyMapDailog dialog;


    @Override
    protected void initData() {
        //获取bundle
        bundle = this.getIntent().getExtras();
        ip = this.getString(R.string.ipv4);
        //获取posto,user
        route = (Route) bundle.getSerializable("route");
        user = (User) bundle.getSerializable("user");

        //获取评论
        Posto route_getcomments = new Posto();
        route_getcomments.setPid(route.getRid());
        route_getcomments.setUsername(user.getUsername());
        MyThread myThread1 = new MyThread();
        submit(myThread1,ip + "/rest/getCommentsRoute",route_getcomments,6);
        resultlist = myThread1.getComments();

        //没有评论的情况
        if(resultlist.size() == 0){
            Comment emptyComment = new Comment();
            emptyComment.setContext("暂无评论QAQ");
            resultlist.add(emptyComment);
        }

        //获取点赞信息,没有点过赞则为int 0
        Posto route_getpraise = new Posto();//
        route_getpraise.setUsername(user.getUsername());
        route_getpraise.setPid(route.getRid());
        MyThread myThread2 = new MyThread();
        submit(myThread2,ip + "/rest/getPraisesRoute",route_getcomments,8);
        result_praise = myThread2.getRid();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_route_detail);
        textView_routedetail_name = (TextView)findViewById(R.id.text_view_routedetail_name);
        textView_routedetail_comment = (TextView)findViewById(R.id.text_view_routedetail_comment);
        textView_routedetail_username = (TextView)findViewById(R.id.text_view_routedetail_username);
        textView_routedetail_date = (TextView)findViewById(R.id.text_view_routedetail_date);
        textView_postodetail_praise = (TextView)findViewById(R.id.text_view_routedetail_praise);

        button_routedetail_make_praise = (Button)findViewById(R.id.button_routedetail_make_praise);
        button_routedetail_make_comment = (Button)findViewById(R.id.button_routedetail_make_comment);
        button_routedetail_postos = (Button)findViewById(R.id.button_routedetail_postos);

        list_routedetail_commentlist = (ListView) findViewById(R.id.list_routedetail_commentlist);

        mapView_routedetail = (MapView) findViewById(R.id.map_routedetail);

    }

    @Override
    protected void initListener() {
        //浏览者发表评论的button
        button_routedetail_make_comment.setOnClickListener(new View.OnClickListener() {
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

                                comment.setRid(route.getRid());
                                comment.setUsername(user.getUsername());
                                comment.setContext(inputText);

                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Long time = timestamp.getTime();// 直接转换成long
                                comment.setDate(time);

                                MyThread myThread1 = new MyThread();
                                submit(myThread1,ip+"/rest/addComment",comment,5);

                                Toast.makeText(RouteDetailActivity.this, "发表成功", Toast.LENGTH_LONG).show();
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
        button_routedetail_make_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result_praise < 0) {
                    Toast.makeText(RouteDetailActivity.this, "您已经点赞过", Toast.LENGTH_LONG).show();
                } else {
                    Praise praise = new Praise();
                    praise.setRid(route.getRid());
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


        button_routedetail_postos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RouteDetailActivity.this,SearchPostoActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putString("source","routedetail");
                intent_bundle.putSerializable("user",user);
                intent_bundle.putSerializable("route",route);
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        //显示route信息
        textView_routedetail_name.setText("name:"+route.getName());
        textView_routedetail_comment.setText("comment:"+route.getComment());
        textView_routedetail_username.setText("user:"+route.getUsername());
        textView_routedetail_date.setText("time:"+route.getStart_time());

        mapView_routedetail.onCreate(savedInstanceState);// 此方法必须重写
        initmap(); //初始化地图
        //draw();
        aMap.setOnMapLoadedListener(this);

        //显示赞数
        textView_postodetail_praise.setText("praise:"+abs(result_praise));

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
        list_routedetail_commentlist.setAdapter(adapter);
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

    private AMap aMap;
    /**、
     * 初始化地图
     */
    private void initmap() {
        if (aMap == null) {
            aMap = mapView_routedetail.getMap();
            setUpMap();
        }
        //mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
        //mLocationErrText.setVisibility(View.GONE);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //aMap.setLocationSource(this);// 设置定位监听
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        //aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        //my codes
        //显示比例尺
        aMap.getUiSettings().setScaleControlsEnabled(true);
        //去掉缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }


    private Polyline polyline;

    //在地图上绘制当前route对象的路线
    public void draw() {

        if (polyline!=null)polyline.remove();

        ArrayList<LatLng> draw_list = new ArrayList<LatLng>();
        for (int i = 0;i<route.location_list.size();i++){
            draw_list.add(route.location_list.get(i).toLatlng());
        }

        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(draw_list).width(10).color(Color.argb(100, 1, 80, 255)));


        LatLngBounds latLngBounds = null;
        try {
            latLngBounds = new LatLngBounds(draw_list.get(0),draw_list.get(0));
        } catch (AMapException e) {
            e.printStackTrace();
        }
        for (int i = 1;i<draw_list.size();i++){
            latLngBounds = latLngBounds.including(draw_list.get(i));
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));



    /*
    //计算显示的范围
        ArrayList<MyLatlng> location_list = new ArrayList<MyLatlng>(route.getLocation_list());
        MyLatlng northeast = new MyLatlng(location_list.get(0).latitude,location_list.get(0).longitude);
        MyLatlng southwest = new MyLatlng(location_list.get(0).latitude,location_list.get(0).longitude);
        MyLatlng buf;

        for (int i = 1;i<location_list.size();i++){
            buf = location_list.get(i);
            if (buf.latitude>northeast.latitude) northeast.latitude = buf.latitude;
            if (buf.longitude>northeast.longitude) northeast.longitude = buf.longitude;
            if (buf.latitude<southwest.latitude) southwest.latitude = buf.latitude;
            if (buf.longitude<southwest.longitude) southwest.longitude = buf.longitude;
        }
     */
    }


    @Override
    public void onMapLoaded() {
        draw();
    }

    //提交之后刷新评论区
    private void refresh() {
        //获取评论
        Posto route_getcomments = new Posto();
        route_getcomments.setPid(route.getRid());
        route_getcomments.setUsername(user.getUsername());
        MyThread myThread1 = new MyThread();
        submit(myThread1,ip + "/rest/getCommentsRoute",route_getcomments,6);
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
        list_routedetail_commentlist.setAdapter(adapter);
    }
}


