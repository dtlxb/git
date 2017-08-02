package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class RouteDetailActivity extends AppCompatActivity implements AMap.OnMapLoadedListener{
    private Bundle bundle;
    KeyMapDailog dialog;
    private ArrayList<Comment> resultlist = new ArrayList<Comment>();
    private Integer result_praise;
    private TextView view_praise ;
    public static String ip;
    private MapView mapView;
    private Route route;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        //获取bundle

        bundle = this.getIntent().getExtras();
        ip = this.getString(R.string.ipv4);
        view_praise = (TextView)this.findViewById(R.id.routedetail_praise);
        //获取posto,user
        route = (Route) bundle.getSerializable("route");
        final User user = (User) bundle.getSerializable("user");
        //显示route信息
        ((TextView)this.findViewById(R.id.routedetail_name)).setText("name:"+route.getName());
        ((TextView)this.findViewById(R.id.routedetail_comment)).setText("comment:"+route.getComment());
        ((TextView)this.findViewById(R.id.routedetail_username)).setText("user:"+route.getUsername());
        ((TextView)this.findViewById(R.id.routedetail_date)).setText("time:"+route.getStart_time());
        //显示地图


        //继承地图的状态
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init(); //初始化地图
        //draw();
        aMap.setOnMapLoadedListener(this);


        //获取评论
        Posto posto_getcomments = new Posto();
        posto_getcomments.setPid(route.getRid());
        posto_getcomments.setUsername(user.getUsername());
        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getCommentsRoute");
        myThread1.setPosto(posto_getcomments);
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
        Posto getpraise = new Posto();//
        getpraise.setUsername(user.getUsername());
        getpraise.setPid(route.getRid());
        MyThread myThread2 = new MyThread();
        myThread2.setGetUrl("http://" + ip + "/rest/getPraisesRoute");
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

        ListView commentlist = (ListView) findViewById(R.id.routedetail_commentlist);
        commentlist.setAdapter(adapter);


        //浏览者发表评论的button
        findViewById(R.id.routedetail_make_comment).setOnClickListener(new View.OnClickListener() {
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

                                comment.setRid(((Route)bundle.getSerializable("route")).getRid());
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


                                Toast.makeText(RouteDetailActivity.this, "发表成功", Toast.LENGTH_LONG).show();

                                dialog.dismiss();

                            }
                        }, 2000);
                    }
                });

                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        //点赞button
        this.findViewById(R.id.routedetail_make_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result_praise < 0) {
                    Toast.makeText(RouteDetailActivity.this, "您已经点赞过", Toast.LENGTH_LONG).show();
                } else {
                    Praise praise = new Praise();
                    praise.setRid(route.getRid());
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


        this.findViewById(R.id.routedetail_postos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RouteDetailActivity.this,SearchPostoActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",user);
                intent_bundle.putSerializable("route",route);
                i.putExtras(intent_bundle);
                startActivity(i);
                //popupWindow.dismiss();
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



    private AMap aMap;

    /**、
     * 初始化地图
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
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
}


