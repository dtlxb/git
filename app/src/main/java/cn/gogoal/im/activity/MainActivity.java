package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SimpleFragmentPagerAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.FoundFragment;
import cn.gogoal.im.fragment.MessageFragment;
import cn.gogoal.im.fragment.MineFragment;
import cn.gogoal.im.fragment.MyStockFragment;
import cn.gogoal.im.fragment.SocialContactFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @BindArray(R.array.main_tab)
    String[] mainTabArray;


    @Override
    public void doBusiness(Context mContext) {

        KLog.e(UserUtils.getToken());

        MessageFragment messageFragment = new MessageFragment();                // TAB1 消息

        MyStockFragment myStockFragment = new MyStockFragment();                //自选股

//        final ContactsActivity contactsFragment = new ContactsActivity();     // TAB2 人脉

        FoundFragment foundFragment = new FoundFragment();                      // TAB3 投研

        SocialContactFragment socialContactFragment = new SocialContactFragment();//社交

        final MineFragment mineFragment = new MineFragment();                 // TAB4 我的

        Boolean needRefresh = getIntent().getBooleanExtra("isFromLogin", false);
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
        vpMain.setOffscreenPageLimit(3);
        tabMain.setupWithViewPager(vpMain);
        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                contactsFragment.srcollShowIndexBar(state == 0 && vpMain.getCurrentItem() == 1);
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
                    SPTools.saveString(UserUtils.getUserAccountId() + "_contact_beans", responseInfo);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    SPTools.saveString(UserUtils.getUserAccountId() + "_contact_beans", "{\"code\":0,\"data\":[],\"message\":\"成功\"}");
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
}
