package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import cn.gogoal.im.fragment.InvestmentResearchFragment;
import cn.gogoal.im.fragment.MessageFragment;
import cn.gogoal.im.fragment.MineFragment;
import cn.gogoal.im.fragment.MyStockFragment;
import cn.gogoal.im.fragment.SocialContactFragment;
import hply.com.niugu.autofixtext.AutofitTextView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @BindView(R.id.main_view_mask)
    View mainViewMask;

    private StatusBarUtil barUtil;

    private MyStockFragment myStockFragment;

    private AutofitTextView[] badgeView;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @BindArray(R.array.main_tab)
    String[] mainTabArray;


    @Override
    public void doBusiness(Context mContext) {

        KLog.e("width===" + AppDevice.getWidth(mContext) + ";height===" + AppDevice.getHeight(mContext));
        KLog.e("DpValueWidth===" + AppDevice.px2dp(mContext, AppDevice.getWidth(mContext)) +
                ";DpValueHeight===" + AppDevice.px2dp(mContext, AppDevice.getHeight(mContext)));

        if (BuildConfig.DEBUG) {
            FileUtil.writeRequestResponse(UserUtils.getToken(), "token_" + UserUtils.getUserName());
        }

        MessageFragment messageFragment = new MessageFragment();                     // TAB1 消息

        myStockFragment = new MyStockFragment();                     //自选股

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
        tabFragments.add(myStockFragment);
        tabFragments.add(foundFragment);
        tabFragments.add(socialContactFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this, tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        vpMain.setOffscreenPageLimit(mainTabArray.length - 1);
        tabMain.setupWithViewPager(vpMain);

        badgeView = new AutofitTextView[mainTabArray.length];

        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
                badgeView[i] = (AutofitTextView) tabAdapter.getTabView(i).findViewById(R.id.count_tv);
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
                myStockFragment.dismissMarket();
            }
        });
    }

//    @Override
//    public void setStatusBar(boolean light) {
//        StatusBarUtil.with(MainActivity.this).setTranslucentForImageViewInFragment(null);
//    }

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
            if (myStockFragment.isMaskViewVisiable()) {
                myStockFragment.dismissMarket();
            } else {
                exitBy2Click();
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

        KLog.e("index=="+index+";num=="+num);

        if (index >= 0 && index < mainTabArray.length) {
            badgeView[index].setText(num > 99 ? "99+" : String.valueOf(num));
        }
        if (num == 0) {
            badgeView[index].setVisibility(View.GONE);
        } else {
            badgeView[index].setVisibility(View.VISIBLE);
        } ;
    }
}
