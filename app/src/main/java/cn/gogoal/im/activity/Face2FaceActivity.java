package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SoftKeyboardAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.SoftKeyboard;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.bean.group.GroupMemberInfo;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.R.id.rv_soft_input;

/**
 * author wangjd on 2017/6/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :面对面建群
 */
public class Face2FaceActivity extends BaseActivity {

    @BindViews({R.id.iv_dot_1, R.id.iv_dot_2, R.id.iv_dot_3, R.id.iv_dot_4})
    AppCompatImageView[] ivDots;

    @BindView(R.id.tv_flag)
    TextView tvFlag;

    @BindView(rv_soft_input)
    RecyclerView rvSoftInput;

    @BindView(R.id.rv_member)
    RecyclerView rvMember;

    @BindView(R.id.layout_members)
    LinearLayout layoutMembers;

    @BindView(R.id.btn_goin)
    SelectorButton btnGoin;

    private int[] numbersImage;

    private MemberAdapter memberAdapter;

    private List<GroupMemberInfo> memberDatas;

    private List<SoftKeyboard> keyboardDatas;

    private SoftKeyboardAdapter adapter;

    private AMapLocationClient locationClient = null;//高德API定位管理类

    private AMapLocationClientOption locationOption = null;//高德API定位配置类

    private double latitude;//纬度

    private double longitude;//经度

    @Override
    public int bindLayout() {
        return R.layout.activity_face2face;
    }

    @Override
    public void doBusiness(Context mContext) {
        setStatusColor(getResColor(R.color.face2faceBg));

        XTitle xTitle = setMyTitle("面对面建群", true);
        xTitle.setLeftImageResource(R.mipmap.image_title_back_255);
        xTitle.setBackgroundColor(getResColor(R.color.face2faceBg));
        xTitle.setLeftTextColor(Color.WHITE);
        xTitle.setTitleColor(Color.WHITE);

        numbersImage=new int[10];

        for (int i=0;i<10;i++){
            numbersImage[i]=getResources().getIdentifier("img_face2face_password_"+i,"mipmap",getPackageName());
        }

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
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
            }
        });
        locationClient.startLocation();

        memberDatas = new ArrayList<>();
        memberAdapter = new MemberAdapter(memberDatas);
        rvMember.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvMember.setNestedScrollingEnabled(false);
        rvMember.setAdapter(memberAdapter);
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

    private void makeDatas() {
        for (int i = 1; i < 10; i++) {
            keyboardDatas.add(new SoftKeyboard(String.valueOf(i), null));
        }

        keyboardDatas.add(new SoftKeyboard(null, null));//左下角空按键
        keyboardDatas.add(new SoftKeyboard(String.valueOf(0), null));//0
        keyboardDatas.add(new SoftKeyboard(null, ContextCompat.getDrawable(getActivity(), R.mipmap.img_keyboard_del)));
        adapter.notifyDataSetChanged();
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
//            ivDots[count].setImageDrawable(getTextDrawable(psw));
            ivDots[count].setImageResource(numbersImage[Integer.parseInt(psw)]);
            pswList.add(psw);
            count++;
            if (count == 4) {
                //创建群、获取加入群
                if (locationClient != null) {
                    if (locationClient.getLastKnownLocation()!=null &&
                            locationClient.getLastKnownLocation()!=null) {
                        creatGoupChart();
                    }else {
                        dialogNoLocation();
                    }
                } else {
                    dialogNoLocation();
                }
            }
        }
    }

    private void dialogNoLocation() {
        WaitDialog dialog = WaitDialog.getInstance("获取位置信息出错，请重新进入重试", R.mipmap.login_error, false);
        dialog.show(getSupportFragmentManager());
        dialog.setCancelOutside(false);
        dialog.dismiss(false, true);
    }

    private void creatGoupChart() {
        final WaitDialog waitDialog = WaitDialog.getInstance("创建中...", R.mipmap.login_loading, true);
        waitDialog.show(getSupportFragmentManager());

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
                    final GroupData groupData = JSONObject.parseObject(object.getString("data"), GroupData.class);

                    //视图变换
                    changeViewTree();
                    //添加成员——自己，和拉取到的成员

                    final GroupMemberInfo contactMe = new GroupMemberInfo();
                    contactMe.setAccount_id(UserUtils.getMyAccountId());
                    contactMe.setAvatar(UserUtils.getUserAvatar());
                    contactMe.setNickname(UserUtils.getNickname());

                    memberDatas.add(contactMe);

                    if (groupData.getM_info() != null) {
                        for (GroupMemberInfo info:groupData.getM_info()){
                            if (!memberDatas.contains(info)) {
                                memberDatas.add(info);
                            }
                        }
                    }

                    memberAdapter.notifyDataSetChanged();
                    waitDialog.dismiss();

                    btnGoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goInNewGroup(groupData.getConv_id());
                        }
                    });

                } else {

                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    private void goInNewGroup(final String conversionId) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("conv_id", conversionId);
        new GGOKHTTP(params, GGOKHTTP.ADD_FTF_MEMBER, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject jsonObject = JSONObject.parseObject(responseInfo);
                if (jsonObject.getIntValue("code") == 0) {
                    if (jsonObject.getJSONObject("data").getBoolean("success")) {
                        Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                        intent.putExtra("square_action", AppConst.CREATE_SQUARE_ROOM_BUILD);
                        intent.putExtra("conversation_id", conversionId);
                        startActivity(intent);
                        Face2FaceActivity.this.finish();
                    } else {
                        UIHelper.toast(Face2FaceActivity.this, "面对面创群失败！");
                    }
                } else {
                    UIHelper.toast(Face2FaceActivity.this, "面对面创群失败！");
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    private void changeViewTree() {
        tvFlag.setVisibility(View.GONE);
        layoutMembers.setVisibility(View.VISIBLE);
        rvSoftInput.setVisibility(View.GONE);
        btnGoin.setVisibility(View.VISIBLE);
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


    private class MemberAdapter extends CommonAdapter<GroupMemberInfo, BaseViewHolder> {

        private MemberAdapter(List<GroupMemberInfo> data) {
            super(R.layout.item_face2face_member, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, GroupMemberInfo data, int position) {
            AppCompatImageView avatar = holder.getView(R.id.iv_item_avatar);
            if (!StringUtils.isActuallyEmpty(data.getAvatar())) {
                ImageDisplay.loadImage(Face2FaceActivity.this, data.getAvatar(), avatar);
            } else {
                ImageDisplay.loadImage(Face2FaceActivity.this, R.mipmap.logo, avatar);
            }

            holder.setText(R.id.tv_item_name, data.getNickname());
        }
    }
}
