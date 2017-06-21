package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.BoxScreenAdapter;
import cn.gogoal.im.adapter.SimpleFragmentPagerAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.BoxScreenData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.main.InfomationFragment;
import cn.gogoal.im.fragment.main.LiveListFragment;
import cn.gogoal.im.fragment.main.MainStockFragment;
import cn.gogoal.im.fragment.main.MineFragment;
import cn.gogoal.im.ui.Badge.BadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.cb_show_live_list)
    AppCompatCheckBox acb;

    @BindView(R.id.iv_message_tag)
    ImageView ivMessageTag;

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @BindArray(R.array.main_tab)
    String[] mainTabArray;

    @BindView(R.id.rv_live_classify)
    RecyclerView rvLiveClassify;

    public MainStockFragment mainStockFragment;
    private LiveListFragment liveListFragment;

    //消息
    private BadgeView badge;
    private int unReadCount;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    @Override
    public void doBusiness(Context mContext) {

        setSupportActionBar(mToolbar);

        setTab();

        boolean needRefresh = getIntent().getBooleanExtra("isFromLogin", false);
        SPTools.saveBoolean("squareNeedRefresh", needRefresh);

        if (needRefresh) {
            //拉取好友列表
            getFriendList();
        }

        setLiveData();

        badge = new BadgeView(MainActivity.this);
        initBadge(unReadCount, badge);
    }

    //底部tab
    private void setTab() {
        //MessageFragment messageFragment = new MessageFragment();                     // TAB1 消息
        mainStockFragment = new MainStockFragment();                                //TAB2 自选股
//        InvestmentResearchFragment foundFragment = new InvestmentResearchFragment(); // TAB3 投研
        InfomationFragment infomationFragment = new InfomationFragment();              //TAb3 资讯
        //TAB4 直播
        liveListFragment = new LiveListFragment();
        final MineFragment mineFragment = new MineFragment();                       // TAB5 我的

        List<Fragment> tabFragments = new ArrayList<>();
        //tabFragments.add(messageFragment);
        tabFragments.add(mainStockFragment);
        tabFragments.add(infomationFragment);
        tabFragments.add(liveListFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this, tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        vpMain.setOffscreenPageLimit(mainTabArray.length - 1);

        //进入聊天
        ivMessageTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MessageHolderActivity.class));
            }
        });

        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mToolbar.setVisibility(tab.getPosition() == 2 ? View.VISIBLE : View.GONE);
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tabMain.setupWithViewPager(vpMain);

        for (int i = 0; i < tabFragments.size(); i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }
        tabMain.getTabAt(1).select();
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
                    UserInfoUtils.saveAllUserInfo(responseInfo);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    UserInfoUtils.saveAllUserInfo("{\"code\":0,\"data\":[],\"message\":\"成功\"}");
                } else {
                    UIHelper.toastError(getActivity(), "获取好友列表失败");
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

    //获取直播数据
    private void setLiveData() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        AppDevice.setViewWidth$Height(rvLiveClassify, 3 * AppDevice.getWidth(this) / 4, -1);

        rvLiveClassify.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));

        rvLiveClassify.setPadding(AppDevice.dp2px(this, 7), AppDevice.dp2px(this, 26), AppDevice.dp2px(this, 7), 0);

        final ArrayList<BoxScreenData> menuData = new ArrayList<>();
        BoxScreenData allData = new BoxScreenData();
        allData.setSelected(true);
        allData.setProgramme_name("全部");
        allData.setProgramme_id(10086);
        menuData.add(0, allData);

        final BoxScreenAdapter boxScreenAdapter = new BoxScreenAdapter(menuData);

        rvLiveClassify.setAdapter(boxScreenAdapter);

        acb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    drawerLayout.openDrawer(Gravity.END, true);
                } else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            public void onDrawerOpened(View drawerView) {
                acb.setChecked(true);
            }

            public void onDrawerClosed(View drawerView) {
                acb.setChecked(false);
            }

            public void onDrawerStateChanged(int newState) {
            }
        });

        new GGOKHTTP(null, GGOKHTTP.GET_PROGRAMME_GUIDE, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                JSONObject object = JSONObject.parseObject(responseInfo);

                if (object.getIntValue("code") == 0) {
                    List<BoxScreenData> data = JSONObject.parseArray(object.getString("data"), BoxScreenData.class);
                    menuData.addAll(data);
                    boxScreenAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e("获取直播分类失败");
            }
        }).startGet();

        findViewById(R.id.tv_do_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtils.checkLivePermission(MainActivity.this);
            }
        });

        boxScreenAdapter.setOnClassifyChangeListener(new BoxScreenAdapter.ClassifyItemClickListener() {
            @Override
            public void itemClick(BoxScreenData data, int pos) {
                liveListFragment.request(AppConst.REFRESH_TYPE_PARENT_BUTTON, pos == 0 ? null : String.valueOf(data.getProgramme_id()));
                drawerLayout.closeDrawer(Gravity.END);
            }
        });
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            switch (tabMain.getSelectedTabPosition()) {
                case 3:
                    if (acb.isChecked()) {
                        acb.setChecked(false);
                        exitTime = 0;
                    } else {
                        exitBy2Click();
                    }
                    break;
                default:
                    exitBy2Click();
                    break;
            }
            return true;
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

    private void initBadge(int num, BadgeView badge) {
        badge.setGravityOffset(2, 7, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(num);
    }


    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        unReadCount++;
        badge.setBadgeNumber(unReadCount);
    }

}
