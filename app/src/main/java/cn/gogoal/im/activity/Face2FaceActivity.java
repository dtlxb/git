package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SoftKeyboardAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.SoftKeyboard;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.TextDrawable;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/6/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :面对面建群
 */
public class Face2FaceActivity extends BaseActivity {

    @BindViews({R.id.iv_dot_1, R.id.iv_dot_2, R.id.iv_dot_3, R.id.iv_dot_4})
    AppCompatImageView[] ivDots;

    private static final int face2faceBgColoe = 0xff1b1f22;

    @BindView(R.id.rv_soft_input)
    RecyclerView rvSoftInput;

    private List<SoftKeyboard> keyboardDatas;

    private SoftKeyboardAdapter adapter;

    private AMapLocationClient locationClient = null;//高德API定位管理类

    private AMapLocationClientOption locationOption = null;//高德API定位配置类

//    private double longitude;
//
//    private double latitude;


    @Override
    public int bindLayout() {
        return R.layout.activity_face2face;
    }

    @Override
    public void doBusiness(Context mContext) {
        setStatusColor(face2faceBgColoe);
        XTitle xTitle = setMyTitle("面对面建群", true);
        xTitle.setLeftImageResource(R.mipmap.image_title_back_255);
        xTitle.setBackgroundColor(face2faceBgColoe);
        xTitle.setLeftTextColor(Color.WHITE);
        xTitle.setTitleColor(Color.WHITE);

        keyboardDatas = new ArrayList<>();
        adapter = new SoftKeyboardAdapter(mContext, keyboardDatas);
        rvSoftInput.setAdapter(adapter);
        rvSoftInput.addItemDecoration(new SoftDivider());
        makeDatas();

        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
//        // 设置定位监听
//        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();


    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
//    AMapLocationListener locationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation location) {
//            if (null != location) {
//
//                KLog.e(JSONObject.toJSONString(location));
////
////                longitude=location.getLongitude();
////                latitude=location.getLatitude();
//            }
//        }
//    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        KLog.e("latitude="+locationClient.getLastKnownLocation().getLatitude()+"\nlatitude="+locationClient.getLastKnownLocation().getLatitude());
//        if (longitude == 0 && latitude == 0) {
//            NormalAlertDialog alertDialog = NormalAlertDialog.newInstance("如果你现在不是在非洲西部大西洋的几内亚湾," +
//                    "我们可能没有获取到你的位置信息", "设置", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                    startActivity(intent);
//                    AppDevice.go2AppDetail(v.getContext());
//                }
//            }, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//            alertDialog.setCancelable(false);
//            alertDialog.show(getSupportFragmentManager());
//        }
//    }
    private void makeDatas() {
        for (int i = 1; i < 10; i++) {
            keyboardDatas.add(new SoftKeyboard(String.valueOf(i), null));
        }

        keyboardDatas.add(new SoftKeyboard(null, null));
        keyboardDatas.add(new SoftKeyboard(String.valueOf(0), null));
        keyboardDatas.add(new SoftKeyboard(null, ContextCompat.getDrawable(getActivity(), R.mipmap.img_keyboard_del)));
        adapter.notifyDataSetChanged();
    }

    private Drawable getTextDrawable(String num) {
        TextDrawable.IBuilder iBuilder = TextDrawable
                .builder()
                .beginConfig()
                .textColor(getResColor(R.color.colorPrimary))
                .fontSize(AppDevice.dp2px(getActivity(), 25))
                .bold()
                .endConfig().rect();
        return iBuilder.build(num, face2faceBgColoe);
    }

    private class SoftDivider extends XDividerItemDecoration {

        private SoftDivider() {
            super(Face2FaceActivity.this, 1, 0xff292e31);
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] show = new boolean[4];
            show[1] = true;
            show[2] = (1 + itemPosition) % 3 != 0;
            return show;
        }
    }

    private int count;
    private List<String> pswList = new ArrayList<>();

    public void inputImagePsw(String psw) {
        if (count < 4) {
            ivDots[count].setImageDrawable(getTextDrawable(psw));
            pswList.add(psw);
            count++;
            if (count == 4) {
                //创建群、获取加入群
                creatGoupChart();
            }
        }
    }

    private void creatGoupChart() {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("password", ArrayUtils.mosaicListElement(pswList).replace(";", ""));
        params.put("longitude", String.valueOf(locationClient.getLastKnownLocation().getLongitude()));
        params.put("latitude", String.valueOf(locationClient.getLastKnownLocation().getLatitude()));

        KLog.e(StringUtils.map2ggParameter(params));

        new GGOKHTTP(params, GGOKHTTP.FTF_CREATE_GROUP, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    GroupData groupData = JSONObject.parseObject(object.getString("data"), GroupData.class);
                    Intent intent = new Intent(Face2FaceActivity.this, Face2FaceHolderActivity.class);
                    intent.putExtra("face2face_group_data", groupData);
                    startActivity(intent);
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    public void delImagePsw() {
        if (count > 0) {
            ivDots[count - 1].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.img_face2face_dot));
            pswList.remove(pswList.size() - 1);
            count--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * 如果AMapLocationClient是在当前Activity实例化的，
         * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
         */
        locationClient.onDestroy();
        locationClient = null;
        locationOption = null;
    }
}
