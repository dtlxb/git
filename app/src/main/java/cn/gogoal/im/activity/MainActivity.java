package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;
import com.socks.library.KLog;

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

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.tab_main)
    TabLayout tabMain;
    private StatusBarUtil barUtil;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @BindArray(R.array.main_tab)
    String[] mainTabArray;


    @Override
    public void doBusiness(Context mContext) {

        KLog.e("width==="+AppDevice.getWidth(mContext)+";height==="+AppDevice.getHeight(mContext));
//            //测试使用
//        String userInfo="{\n" +
//                "                                                               \"phone\" : \"\",\n" +
//                "                                                               \"department\" : \"技术部\",\n" +
//                "                                                               \"simple_avatar\" : \"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.gif\",\n" +
//                "                                                               \"duty\" : \"android开发\",\n" +
//                "                                                               \"token\" : \"9a35baeb6c154dc9ab1632f3abd8c6fd\",\n" +
//                "                                                               \"gender\" : \"未\",\n" +
//                "                                                               \"parent_account_id\" : 0,\n" +
//                "                                                               \"login_type\" : 0,\n" +
//                "                                                               \"is_parent_account\" : 0,\n" +
//                "                                                               \"login_id\" : 301117,\n" +
//                "                                                               \"account_id\" : 357006,\n" +
//                "                                                               \"nickname\" : \"隔壁王叔叔\",\n" +
//                "                                                               \"code\" : 0,\n" +
//                "                                                               \"avatar\" : \"nullHead/NOFTX.gif\",\n" +
//                "                                                               \"account_status\" : 1,\n" +
//                "                                                               \"photo\" : \"nullPhoto/ucloud_3FF7A10884D1D8FD.jpg\",\n" +
//                "                                                               \"account_name\" : \"E00003645\",\n" +
//                "                                                               \"email\" : \"\",\n" +
//                "                                                               \"organization_id\" : 4,\n" +
//                "                                                               \"organization_name\" : \"朝阳永续\",\n" +
//                "                                                               \"organization_address\" : \"\",\n" +
//                "                                                               \"mobile\" : \"\",\n" +
//                "                                                               \"full_name\" : \"王洁东\",\n" +
//                "                                                               \"weibo\" : \"\",\n" +
//                "                                                               \"is_tc_org\" : 0\n" +
//                "                                                             }";
//        SPTools.saveString("userInfo",userInfo);

        if (BuildConfig.DEBUG) {
            FileUtil.writeRequestResponse("token_" + UserUtils.getUserName(), UserUtils.getToken());
        }

        if (null!=UserUtils.getUserInfo()) {
            KLog.e(UserUtils.getUserInfo().toString());
        }

        MessageFragment messageFragment = new MessageFragment();                     // TAB1 消息

        MyStockFragment myStockFragment = new MyStockFragment();                     //自选股

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
        vpMain.setOffscreenPageLimit(3);
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
                barUtil.setStatusBarFontDark(position != 4);
                barUtil.setColor(position == 4 ? getResColor(R.color.colorMineHead) : getResColor(android.R.color.white));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                contactsFragment.srcollShowIndexBar(state == 0 && vpMain.getCurrentItem() == 1);
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
}
