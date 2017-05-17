package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.BuildConfig;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SimpleFragmentPagerAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.BoxScreenData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.main.InvestmentResearchFragment;
import cn.gogoal.im.fragment.main.MainStockFragment;
import cn.gogoal.im.fragment.main.MessageFragment;
import cn.gogoal.im.fragment.main.MineFragment;
import cn.gogoal.im.fragment.main.SocialContactFragment;
import cn.gogoal.im.ui.Badge.BadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @BindView(R.id.main_view_mask)
    View mainViewMask;

    //侧滑模块
    @BindView(R.id.drawer_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.lay_right_menu)
    RelativeLayout menuLayout;
    @BindView(R.id.recyScreen)
    RecyclerView recyScreen;

    public MainStockFragment mainStockFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @BindArray(R.array.main_tab)
    String[] mainTabArray;

    /*@BindArray(R.array.emoji_array)
    String[] emojis;*/

    private BadgeView badge;

    @Override
    public void doBusiness(Context mContext) {

        KLog.e("width===" + AppDevice.getWidth(mContext) + ";height===" + AppDevice.getHeight(mContext));
        KLog.e("DpValueWidth===" + AppDevice.px2dp(mContext, AppDevice.getWidth(mContext)) +
                ";DpValueHeight===" + AppDevice.px2dp(mContext, AppDevice.getHeight(mContext)));

        if (BuildConfig.DEBUG) {
            FileUtil.writeRequestResponse(UserUtils.getToken(), "token_" + UserUtils.getUserName());
        }

        MessageFragment messageFragment = new MessageFragment();                     // TAB1 消息

        mainStockFragment = new MainStockFragment();                     //自选股

        InvestmentResearchFragment foundFragment = new InvestmentResearchFragment(); // TAB3 投研

        SocialContactFragment socialContactFragment = new SocialContactFragment();   //社交

        final MineFragment mineFragment = new MineFragment();                       // TAB4 我的

        boolean needRefresh = getIntent().getBooleanExtra("isFromLogin", false);
        SPTools.saveBoolean("squareNeedRefresh", needRefresh);

        if (needRefresh) {
            //拉取好友列表
            getFriendList();
        }
        /*for (int i=0;i<emojis.length;i++){
            int red=getResources().getIdentifier("img_emoji_"+i,"mipmap",getPackageName());
        }*/

        List<Fragment> tabFragments = new ArrayList<>();
        tabFragments.add(messageFragment);
        tabFragments.add(mainStockFragment);
        tabFragments.add(foundFragment);
        tabFragments.add(socialContactFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this, tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        vpMain.setOffscreenPageLimit(mainTabArray.length - 1);
        tabMain.setupWithViewPager(vpMain);

        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }

        tabMain.getTabAt(2).select();

        mainViewMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainStockFragment.dismissMarket();
            }
        });

        //侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                closeCallBack.closeDrawer();
            }
        });

        getScreenData();
    }

    public void changeItem(int index) {
        if (index > 0 && index < tabMain.getTabCount()) {
            tabMain.getTabAt(index).select();
        }
    }

    private void getFriendList() {

        Map<String, String> param = new HashMap<>();

        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    UserUtils.saveContactInfo(responseInfo);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    UserUtils.saveContactInfo("{\"code\":0,\"data\":[],\"message\":\"成功\"}");
                } else {
                    UIHelper.toastError(getActivity(), GGOKHTTP.getMessage(responseInfo));
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FRIEND_LIST, ggHttpInterface).startGet();
    }

    public void showMainMsk() {
        mainViewMask.setClickable(true);
        mainViewMask.setEnabled(true);
        mainViewMask.setVisibility(View.VISIBLE);
        mainViewMask.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.alpha_in));
    }

    public void hideMainMsk() {
        mainViewMask.setClickable(false);
        mainViewMask.setEnabled(false);
        mainViewMask.setVisibility(View.GONE);
        mainViewMask.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.alpha_out));
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(menuLayout)) {
                mDrawerLayout.closeDrawer(menuLayout);
                exitTime = 0;
            } else {
                if (mainStockFragment.isMaskViewVisiable()) {
                    mainStockFragment.dismissMarket();
                } else {
                    exitBy2Click();
                }
            }

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
//            startActivity(new Intent(getActivity(), TestActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitBy2Click() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Subscriber(tag = "correct_allmessage_count")
    public void setBadgeViewNum(BaseMessage<Integer> message) {
        int index = message.getOthers().get("index");
        int num = message.getOthers().get("number");

        initBadge(index, num);

        if (index >= 0 && index < mainTabArray.length) {
            if (num > 0) {
                badge.setBadgeNumber(num);
            } else {
                badge.hide(false);
            }
        }
    }

    private void initBadge(int index, int num) {
        if (badge != null) {
            if (num == 0) {
                badge.hide(false);
                //TODO
            } else {
                badge.setGravityOffset(0, 0, true);
                badge.setShowShadow(false);

                badge.bindTarget(tabMain.getTabAt(index).getCustomView());

                badge.setBadgeGravity(Gravity.TOP | Gravity.END);
                badge.setBadgeTextSize(12, true);
                badge.setBadgePadding(5, true);
                String uriStr = "android.resource://" + this.getPackageName() + "/" + R.raw.ding;

//                VoiceManager.getInstance(MainActivity.this)
//                        .startPlay(Uri.parse(uriStr));
            }
        } else {
            badge = new BadgeView(MainActivity.this);
            badge.bindTarget(tabMain.getTabAt(index).getCustomView());
        }
    }

    //暴露给外部展开菜单的方法
    public void openMenu() {
        mDrawerLayout.openDrawer(menuLayout);
    }

    //暴露给外部收起菜单的方法
    public void closeMenu() {
        mDrawerLayout.closeDrawer(menuLayout);
    }

    /**
     * 获取筛选列表数据
     */
    private void getScreenData() {

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONArray("data");
                    List<BoxScreenData> screenData = new ArrayList<>();

                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            screenData.add(new BoxScreenData(data.getJSONObject(i).getString("programme_name"),
                                    data.getJSONObject(i).getString("programme_id"), false));
                        }
                    }

                    screenData.add(0, new BoxScreenData("全部", null, true));
                    showBoxScreen(screenData);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getActivity(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(null, GGOKHTTP.GET_PROGRAMME_GUIDE, ggHttpInterface).startGet();
    }

    /**
     * 设置筛选弹窗
     */
    private void showBoxScreen(final List<BoxScreenData> screenData) {

        initRecycleView(recyScreen, null);
        recyScreen.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final BoxScreenAdapter adapter = new BoxScreenAdapter(getActivity(), screenData);
        recyScreen.setAdapter(adapter);
    }

    private class BoxScreenAdapter extends CommonAdapter<BoxScreenData, BaseViewHolder> {

        private List<BoxScreenData> mDatas;
        private int mSelectedPos = -1;

        private BoxScreenAdapter(Context context, List<BoxScreenData> list) {
            super(R.layout.item_box_screen, list);
            this.mDatas = list;

            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).isSelected()) {
                    mSelectedPos = i;
                }
            }
        }

        @Override
        protected void convert(BaseViewHolder holder, final BoxScreenData data, final int position) {
            final TextView textProName = holder.getView(R.id.textProName);

            textProName.setSelected(data.isSelected());
            textProName.setText(data.getProgramme_name());

            textProName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mSelectedPos != position) {
                        //先取消上个item的勾选状态
                        mDatas.get(mSelectedPos).setSelected(false);
                        notifyItemChanged(mSelectedPos);
                        //设置新Item的勾选状态
                        mSelectedPos = position;
                        mDatas.get(mSelectedPos).setSelected(true);
                        notifyItemChanged(mSelectedPos);
                    }

                    closeMenu();

                    vpMain.setCurrentItem(3);
                    AppManager.getInstance().sendMessage("setScreen", new BaseMessage("programme_id", data.getProgramme_id()));

                    /*if (mSelectedPos != 0) {
                        Intent intent = new Intent(getActivity(), ScreenActivity.class);
                        intent.putExtra("programme_name", data.getProgramme_name());
                        intent.putExtra("programme_id", data.getProgramme_id());
                        startActivity(intent);
                    }*/
                }
            });
        }
    }

    drawerCloseCallBack closeCallBack;

    public drawerCloseCallBack getCloseCallBack() {
        return closeCallBack;
    }

    public void setCloseCallBack(drawerCloseCallBack closeCallBack) {
        this.closeCallBack = closeCallBack;
    }

    public interface drawerCloseCallBack {
        void closeDrawer();
    }
}
