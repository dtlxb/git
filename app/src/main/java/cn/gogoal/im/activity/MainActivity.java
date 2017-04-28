package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;
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
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
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
import cn.gogoal.im.ui.Badge.Badge;
import cn.gogoal.im.ui.Badge.BadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @BindView(R.id.main_view_mask)
    View mainViewMask;

    private StatusBarUtil barUtil;

    private MainStockFragment mainStockFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @BindArray(R.array.main_tab)
    String[] mainTabArray;

    private BadgeView badge;

    @Override
    public void doBusiness(Context mContext) {


        KLog.e("width===" + AppDevice.getWidth(mContext) + ";height===" + AppDevice.getHeight(mContext));
        KLog.e("DpValueWidth===" + AppDevice.px2dp(mContext, AppDevice.getWidth(mContext)) +
                ";DpValueHeight===" + AppDevice.px2dp(mContext, AppDevice.getHeight(mContext)));

        if (BuildConfig.DEBUG) {
            FileUtil.writeRequestResponse(UserUtils.getToken(), "token_" + UserUtils.getUserName());
        }
        badge = new BadgeView(MainActivity.this);

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

        barUtil = StatusBarUtil.with(MainActivity.this);

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (barUtil.isOperableDevice()) {
                    barUtil.setStatusBarFontDark(position != 4);
                    barUtil.setColor(position == 4 ? getResColor(R.color.colorMineHead) : getResColor(android.R.color.white));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                contactsFragment.srcollShowIndexBar(state == 0 && vpMain.getCurrentItem() == 1);
            }
        });

        mainViewMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainStockFragment.dismissMarket();
            }
        });
    }

    private void getFriendList() {

        Map<String, String> param = new HashMap<>();

        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", responseInfo);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", "{\"code\":0,\"data\":[],\"message\":\"成功\"}");
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
            if (mainStockFragment.isMaskViewVisiable()) {
                mainStockFragment.dismissMarket();
            } else {
                exitBy2Click();
            }
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_MENU){
            startActivity(new Intent(getActivity(),TestActivity.class));
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

        initBadge(num);

        if (index >= 0 && index < mainTabArray.length) {
            if (num > 0) {
                badge.bindTarget(tabMain.getTabAt(index).getCustomView());
                badge.setBadgeNumber(num);
            } else {
                badge.hide(false);
            }
        }
    }

    private void initBadge(int num) {
        if (badge != null) {
            if (num == 0) {
                badge.hide(false);
            }else {
                badge.setGravityOffset(0, 0, true);
                badge.setShowShadow(false);

                badge.setBadgeGravity(Gravity.TOP | Gravity.END);
                badge.setBadgeTextSize(12, true);
                badge.setBadgePadding(5, true);

                String uriStr = "android.resource://" + this.getPackageName() + "/"+R.raw.ding;

//                VoiceManager.getInstance(MainActivity.this)
//                        .startPlay(Uri.parse(uriStr));
            }
        }
    }
}
