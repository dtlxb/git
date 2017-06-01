package cn.gogoal.im.activity;

import android.content.Context;
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
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
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
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.main.InvestmentResearchFragment;
import cn.gogoal.im.fragment.main.LiveListFragment;
import cn.gogoal.im.fragment.main.MainStockFragment;
import cn.gogoal.im.fragment.main.MessageFragment;
import cn.gogoal.im.fragment.main.MineFragment;
import cn.gogoal.im.ui.Badge.BadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.cb_show_live_list)
    AppCompatCheckBox acb;

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @BindView(R.id.main_view_mask)
    View mainViewMask;

    @BindArray(R.array.main_tab)
    String[] mainTabArray;

    @BindView(R.id.rv_live_classify)
    RecyclerView rvLiveClassify;

    public MainStockFragment mainStockFragment;
    private LiveListFragment liveListFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    /*@BindArray(R.array.emoji_array)
    String[] emojis;*/

    private BadgeView badge;

    @Override
    public void doBusiness(Context mContext) {

        setSupportActionBar(mToolbar);

        setTab();

        //登录成功获取投资顾问,缓存
        UserUtils.getAdvisers(null);

        boolean needRefresh = getIntent().getBooleanExtra("isFromLogin", false);
        SPTools.saveBoolean("squareNeedRefresh", needRefresh);

        if (needRefresh) {
            //拉取好友列表
            getFriendList();
        }

        mainViewMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainStockFragment.dismissMarket();
            }
        });

        setLiveData();
    }

    //底部tab
    private void setTab() {
        MessageFragment messageFragment = new MessageFragment();                     // TAB1 消息
        mainStockFragment = new MainStockFragment();                                //TAB2 自选股
        InvestmentResearchFragment foundFragment = new InvestmentResearchFragment(); // TAB3 投研
        //TAB4 直播
        liveListFragment = new LiveListFragment();
        final MineFragment mineFragment = new MineFragment();                       // TAB5 我的

        List<Fragment> tabFragments = new ArrayList<>();
        tabFragments.add(messageFragment);
        tabFragments.add(mainStockFragment);
        tabFragments.add(foundFragment);
        tabFragments.add(liveListFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this, tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        vpMain.setOffscreenPageLimit(mainTabArray.length - 1);

        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mToolbar.setVisibility(tab.getPosition() == 3 ? View.VISIBLE : View.GONE);
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tabMain.setupWithViewPager(vpMain);

        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }

        tabMain.getTabAt(2).select();
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

                KLog.e(responseInfo);

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
                liveListFragment.request(AppConst.REFRESH_TYPE_PARENT_BUTTON,pos==0?null:data.getProgramme_name());
                drawerLayout.closeDrawer(Gravity.END);
            }
        });
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            switch (tabMain.getSelectedTabPosition()) {
                case 1:
                    if (mainStockFragment.isMaskViewVisiable()) {
                        mainStockFragment.dismissMarket();
                        exitTime = 0;
                    }else {
                        exitBy2Click();
                    }
                    break;
                case 3:
                    if (acb.isChecked()) {
                        acb.setChecked(false);
                        exitTime = 0;
                    }else {
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

}
