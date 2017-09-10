package com.example.dell.bzbp_frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
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
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.User;

import static com.example.dell.bzbp_frame.MainActivity.LOCATION_MARKER_FLAG;

public class GuideActivity extends BaseActivity implements OnMapLoadedListener, LocationSource
        ,AMapLocationListener{

    private MapView mMapView;
    private AMap aMap;

    private User user;
    private Posto posto;

    private Bundle bundle;
    private TextView mLocationErrText;


    @Override
    protected void initListener(){}

    @Override
    protected void initView(){}

    @Override
    protected void initData(){ }

    @Override
    protected void doBusiness(Bundle savedInstanceState){
        setContentView(R.layout.activity_guide);

        bundle = this.getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        posto = (Posto)bundle.getSerializable("posto");

        mMapView = (MapView)findViewById(R.id.map_guide);
        mMapView.onCreate(savedInstanceState);


        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);

        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        mLocationErrText.setVisibility(View.GONE);


    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMapLoadedListener(this);

        //my codes
        //显示比例尺
        aMap.getUiSettings().setScaleControlsEnabled(true);
        //去掉缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }


    @Override
    public void onMapLoaded(){
        addMarkerToMap(posto);
        LatLng ll = new LatLng(posto.getLatitude(),posto.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,17));
    }


    //把一个posto画在map上，并返回对应的marker对象.
    private Marker addMarkerToMap(Posto posto) {

        //提取出posto对象内部的经纬度信息
        LatLng position = new LatLng(posto.getLatitude(),posto.getLongitude());

        //取出posto内部的图片
        BitmapDescriptor bitmapDescriptor = ImageStringToBitmap(posto.getImage());

        //设置marker的信息
        MarkerOptions markerOption = new MarkerOptions()
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .icon(bitmapDescriptor)                     //图标是图片
                .position(position)         //Marker的位置就是posto的位置
                .draggable(false)           //不能拖动
                .title(Integer.toString(posto.getPid()));    //Marker的名字是pid
        return aMap.addMarker(markerOption);
    }

    //辅助函数：把posto里的image String转化成BitmapDescriptor

    public BitmapDescriptor ImageStringToBitmap(String image){
        //取出posto内部的图片
        Bitmap bit;
        BitmapDescriptor bitmapDescriptor;
        BitmapDescriptorFactory bitmapDescriptorFactory = new BitmapDescriptorFactory();

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        bitmapDescriptor = bitmapDescriptorFactory.fromBitmap(bit);
        return bitmapDescriptor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }




    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private SensorEventHelper mSensorHelper;
    private Marker mLocMarker;
    private Circle mCircle;
    private boolean mFirstFix = false;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
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
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
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

//    BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

}

