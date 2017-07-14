package com.example.dell.bzbp_frame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.dell.bzbp_frame.model.MyLatlng;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.sql.Timestamp;
import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener {

    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Bundle bundle;

    //记录上次定位的位置，在用户点击复位按钮时移到这里
    private MyLatlng last_location = null;

    //各个按钮
    private Button mButton_locate;
    private Button mButton_pause;
    private Button mButton_start;
    private Button mButton_stop;
    private Button mButton_posto;

    //这次route所形成的Route对象
    private Route route = null;

    //在地图上绘制的路线
    private Polyline polyline = null;

    //信号值，只有在该值为true的时候才会记录位置。用户通过操作ui可以进行开|关。
    private boolean is_locating = false;


    public static String ip="192.168.1.97:8080/BookStore";

    //定位功能所用
    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    public static final String LOCATION_MARKER_FLAG = "mylocation";


    //保存数据（切屏，锁屏，旋转设备时）
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

        //route对象
        outState.putSerializable("route",route);
        //最后一次定位信息
        outState.putDouble("latitude",last_location.latitude);
        outState.putDouble("longitude",last_location.longitude);
        //暂停/继续的状态
        outState.putBoolean("is_locating",is_locating);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.activity_route);

        bundle = this.getIntent().getExtras();

        if (bundle.getSerializable("route")!=null){
            //拿出route，并加入新的pid
            route = (Route)bundle.getSerializable("route");
            if ((Integer)bundle.getInt("last_pid")!=-1){
                route.getPids().add((Integer)bundle.getInt("last_pid"));
            }

            //拿出user，然后把其他的都删掉
            User u = (User) bundle.getSerializable("user");
            bundle.clear();
            bundle.putSerializable("user",u);
            //若是从posto回来的，就取出route，然后把bundle里的route删掉。避免把它带到别的地方去。
        }

        //继承地图的状态
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init(); //初始化地图


        //继承数据。（onpause，切屏，旋转）
        if (savedInstanceState!=null){
            //继承最后一次定位的信息
            last_location = new MyLatlng((Double) savedInstanceState.get("latitude"),
                    (Double)savedInstanceState.get("longitude"));

            //继承route的状态。
            route = (Route) savedInstanceState.getSerializable("route");

            is_locating = (boolean) savedInstanceState.getBoolean("is_locating");
        }

        //若无，则初始化Route对象
        if (route==null){
            route = new Route();

            //测试用：为route插入初始值
            //route.location_list.add(new LatLng(31.03,121.43));
            route.location_list.add(new MyLatlng(31.03,121.44));

            //把rid初始化成-1，后台可以知道这是一个新route。
            //route.setRid(-1);

            //在这里把route发送过去，并拿回真正的rid
            MyThread myThread1 = new MyThread();
            myThread1.setGetUrl("http://"+ip+"/rest/addRoute");
            myThread1.setWhat(3);
            myThread1.setRoute(route);
            myThread1.start();
            try {
                myThread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //赋值rid
            if (myThread1.getRid()!=null){
                route.setRid(myThread1.getRid());
            }

            //赋值username
            User u = (User) this.getIntent().getExtras().getSerializable("user");
            route.setUsername(u.getUsername());



        }
        else{//若在bundle里找到了route，则需要重新画一次

            //draw();
        }

        //初始化按钮
        initButtons();

        //保持高亮，禁止自动锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onBackPressed() {
        //TODO something
        new AlertDialog.Builder(this).setTitle("Warnning").setMessage("route信息将不会被保存，确认退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        Intent i = new Intent(RouteActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle intent_bundle = new Bundle();
                        intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                        i.putExtras(intent_bundle);
                        startActivity(i);
                    }
                }).show();
    }

    /**、
     * 初始化地图
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        //my codes
        //显示比例尺
        aMap.getUiSettings().setScaleControlsEnabled(true);
        //去掉缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    //初始化所有按钮
    private void initButtons(){

        //LOCATE:把地图归位到用户当前定位的位置。
        mButton_locate = (Button) findViewById(R.id.button_locate);
        mButton_locate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (last_location!=null){
                    //按下按钮时，把camera移到上次定位的位置。
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(last_location.toLatlng()));
                }
            }
        });

        //PAUSE:暂停记录路线
        mButton_pause = (Button) findViewById(R.id.button_route_pause);
        mButton_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                is_locating = false;
            }
        });

        //RESUME:恢复记录路线（在暂停之后）
        mButton_start = (Button) findViewById(R.id.button_route_start);
        mButton_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                is_locating = true;
                if (route.getStart_time()==null){//第一次时，要在这里初始化Route的start（开始时间）。
                    route.setStart_time(new Timestamp(System.currentTimeMillis()).getTime());
                }
            }
        });

        //STOP:停止记录路线，并跳转到确认&编辑&保存&分享界面
        mButton_stop = (Button) findViewById(R.id.button_route_stop);
        mButton_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //停止记录
                is_locating = false;

                //在这里设置结束时间（route.end）
                //
                if (route.getEnd_time()==null){//第一次时，要在这里初始化Route的start（开始时间）。
                    route.setEnd_time(new Timestamp(System.currentTimeMillis()).getTime());
                }

                //Route对象在这里传到下个Activity，并在那里设置comment和name
                // （name是不是要在一开始的时候先设置一下？）
                //
                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/addRoute");
                myThread1.setWhat(3);
                myThread1.setRoute(route);
                myThread1.start();

                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });


        //执行扩展流：转到posto功能。该posto对象与本次route将存在对应关系（relation）。
        mButton_posto = (Button) findViewById(R.id.button_route_posto);
        mButton_posto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(RouteActivity.this,CameraActivity.class);
                bundle.putBoolean("is_in_route",true);
                bundle.putInt("rid",route.getRid());
                bundle.putSerializable("route",route);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }


    /**
     * 每次定位成功后的回调函数
     * 实现核心功能，不要随便改！！！！
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转

                    //因为是第一次定位，所以把camera已过去
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }
                //每次定位都会把地图Camera移到定位点所在的位置。若注释掉下面这一行，用户就可以自己拖拽控制Camera
                //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));

                //my codes
                //取而代之的是在Activity中记录location，在另外的控制流中使用它定位camera.
                last_location = new MyLatlng(location.latitude,location.longitude);

                //当定位没有被暂停的时候
                if (is_locating) {
                    //把当前location加入List<Latlng>
                    route.location_list.add(last_location);

                    //在地图上更新route的绘制

                    draw();
                }

                //测试用：显示当前关于route对象的信息
                alert();


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    private void alert() {//更改前台界面上的debug信息
        TextView v = (TextView) findViewById(R.id.aalert);
        String text = "location list size : " + Integer.toString(route.location_list.size());
        text += '\n';
        text += "posto list size : " + Integer.toString(route.getPids().size());
        v.setText(text);

    }

    //在地图上绘制当前route对象的路线
    public void draw(){

        if (polyline!=null)polyline.remove();

        ArrayList<LatLng> draw_list = new ArrayList<LatLng>();
        for (int i = 0;i<route.location_list.size();i++){
            draw_list.add(route.location_list.get(i).toLatlng());
        }
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(draw_list).width(10).color(Color.argb(100, 1, 80, 255)));
    }

    public void drawOne(LatLng location){//在现有路线基础上追加一个点
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                add(location).width(10).color(Color.argb(100, 1, 80, 255)));
    }

    /**
     * 激活定位并进行相关设置（不要改！！
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            //在route功能中，定位间隔时间要较长。
            mLocationOption.setInterval(2000);

            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }


    /**
     * 停止定位（不要改！！！
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }
    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }


    /**
     * Activity生命周期相关方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();
        mFirstFix = false;

        //pause时不停止定位
        //deactivate();

    }

    /**
     * 方法必须重写
     */

    //保存数据


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

}
