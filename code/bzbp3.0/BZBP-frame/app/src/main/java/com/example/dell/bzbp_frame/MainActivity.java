package com.example.dell.bzbp_frame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.dell.bzbp_frame.model.*;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.sql.Timestamp;
import java.util.List;

import static com.amap.api.mapcore.util.ag.v;

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, OnMarkerClickListener {


    private Button mCameraButton;
    private Button mStartRouteButton;
    private PopupWindow popupWindow;
    private MyLatlng last_location = new MyLatlng(-1.0,-1.0);
    private Bundle bundle;
    public static String ip = "192.168.1.97:8080/BookStore";

    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    public static final String LOCATION_MARKER_FLAG = "mylocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //用户信息
        bundle = this.getIntent().getExtras();
        //Toast.makeText(MainActivity.this,"id:"+((User)bundle.getSerializable("user")).getId(), Toast.LENGTH_LONG).show();
        //测试用户信息

        User user = new User();
        user.setUsername(((User)bundle.getSerializable("user")).getUsername());
        user.setPassword(((User)bundle.getSerializable("user")).getPassword());
/*
        TextView test_username = (TextView) findViewById(R.id.test_username);
        TextView test_password = (TextView) findViewById(R.id.test_password);
        test_username.setText(user.getUsername());
        test_password.setText(user.getPassword());*/

/*
        this.findViewById(R.id.button_main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MenuActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                //获取位置信息 Double latitude,longitude

                intent_bundle.putDouble("latitude",last_location.latitude);
                intent_bundle.putDouble("longitude",last_location.longitude);
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });*/

        this.findViewById(R.id.button_main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != popupWindow) {
                    popupWindow.dismiss();
                    //return;
                }
                    initPopuptWindow_menu();

                popupWindow.showAsDropDown(view);

            }
        });

        this.findViewById(R.id.button_main_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CameraActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                //获取位置信息 Double latitude,longitude

                intent_bundle.putDouble("latitude",last_location.latitude);
                intent_bundle.putDouble("longitude",last_location.longitude);

                //时间
                intent_bundle.putLong("time",new Timestamp(System.currentTimeMillis()).getTime());

                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });

        //route

        mStartRouteButton = (Button) findViewById(R.id.button_main_route_start);
        mStartRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,RouteActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });


        //init map

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        //测试：显示附近posto
        //已通过。接下来应该改成通过点击按钮触发。
        //showPostos(getNearbyPostos());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Warnning").setMessage("Are you sure left?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        MainActivity.this.finish();
                    }
                }).show();
    }


    /**
     * 初始化
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

    protected void initPopuptWindow_menu() {

        // 获取menu的布局
        View popupWindow_view = getLayoutInflater().inflate(R.layout.activity_menu, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, 600, 600, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        //点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
        // menu视图里面的控件
        Button search_posto = (Button) popupWindow_view.findViewById(R.id.button_menu_search_posto);
        Button search_route = (Button) popupWindow_view.findViewById(R.id.button_menu_search_route);
        Button friends = (Button) popupWindow_view.findViewById(R.id.button_menu_friends);
        Button nearby_posto = (Button) popupWindow_view.findViewById(R.id.button_menu_nearby_posto);
        // menu视图里面的控件触发的事件
        //
        search_posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchPostoActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                //获取位置信息 Double latitude,longitude
                intent_bundle.putDouble("latitude",last_location.latitude);
                intent_bundle.putDouble("longitude",last_location.longitude);
                i.putExtras(intent_bundle);
                startActivity(i);
                //popupWindow.dismiss();
            }
        });
        //
        search_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchRouteActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                intent_bundle.putDouble("latitude",last_location.latitude);
                intent_bundle.putDouble("longitude",last_location.longitude);
                i.putExtras(intent_bundle);
                startActivity(i);
                //popupWindow.dismiss();
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FriendsActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(intent_bundle);
                startActivity(i);
                //popupWindow.dismiss();
            }
        });

        nearby_posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostos(getNearbyPostos());
            }
        });
    }

    protected void initPopuptWindow_posto() {

        // 获取布局
        View popupWindow_view = getLayoutInflater().inflate(R.layout.activity_main_posto_marker, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, 400, 300, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        //点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });

    }



    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);//设置Marker点击的监听器
    }


    /**
     * 方法必须重写
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
        deactivate();
        mFirstFix = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

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

    /**
     * 定位成功后回调函数
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

                    //第一次定位时，把镜头移到定位点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }
                //是否把镜头固定在定位的位置？
                //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));
                last_location = new MyLatlng(location.latitude,location.longitude);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
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
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
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
     * 停止定位
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

    /*
    这两个add是定位图标专用的add函数。
    如果要自己加marker，不要用这个。
    不要在任何地方复用。
     */
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



    /*
     *  下面的部分是“在地图上显示附近postos”功能的代码。
     */



    private List<Posto> getNearbyPostos(){
        //把自己的位置发送给服务器，得到一个与自己临近的posto的List。（计算过程在服务器上完成）

        //传一个posto。事实上，服务器只需要得到一个位置信息即可。
        Posto temp = new Posto();
        temp.setLatitude(0.0);
        temp.setLongitude(0.0);

        //
        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getPostosBy");
        myThread1.setPosto(temp);
        myThread1.setWhat(2);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Posto> resultlist = myThread1.getPostos();

        return resultlist;
    }

    private int showPostos(List<Posto> postos){
        //从服务器获得自己附近的posto之后，在地图上显示成为Marker。返回显示的个数（？）

        if (postos==null)return -1;     //空指针检查

        for (int i = 0;i<postos.size();++i){
            addMarkerToMap(postos.get(i));
        }

        return postos.size();
    }

    private void addMarkerToMap(Posto posto) {

        //提取出posto对象内部的经纬度信息
        LatLng position = new LatLng(posto.getLatitude(),posto.getLongitude());

        //取出posto内部的图片
        Bitmap bit;
        BitmapDescriptor bitmapDescriptor;
        BitmapDescriptorFactory bitmapDescriptorFactory = new BitmapDescriptorFactory();

        byte[] decodedString = Base64.decode(posto.getImage(), Base64.DEFAULT);
        bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        bitmapDescriptor = bitmapDescriptorFactory.fromBitmap(bit);

        //设置marker的信息
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(position)         //Marker的位置就是posto的位置
                .draggable(false)           //不能拖动
                .title(Integer.toString(posto.getPid()));    //Marker的名字是pid
                //.icon(bitmapDescriptor);                    //图标是图片
        aMap.addMarker(markerOption);
    }


    //点击marker的回调函数
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        Toast.makeText(MainActivity.this, "您点击了Posto:"+marker.getTitle(), Toast.LENGTH_LONG).show();
        //if (null == popupWindow){
            initPopuptWindow_posto();

        popupWindow.showAsDropDown(findViewById(R.id.marker_pop));
        return true;
    }




    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


}
