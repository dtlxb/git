package com.gogoal.app.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gogoal.app.R;
import com.gogoal.app.adapter.SimpleFragmentPagerAdapter;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.fragment.ContactsFragment;
import com.gogoal.app.fragment.FoundFragment;
import com.gogoal.app.fragment.MessageFragment;
import com.gogoal.app.fragment.MineFragment;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

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

        MessageFragment messageFragment = new MessageFragment();        // TAB1 消息

        ContactsFragment contactsFragment = new ContactsFragment();     // TAB2 联系人

        FoundFragment foundFragment = new FoundFragment();              // TAB3 发现

        MineFragment mineFragment = new MineFragment();                 // TAB4 我的

        List<Fragment> tabFragments = new ArrayList<>();
        tabFragments.add(messageFragment);
        tabFragments.add(contactsFragment);
        tabFragments.add(foundFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(),getContext(),tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        vpMain.setOffscreenPageLimit(3);
        tabMain.setupWithViewPager(vpMain);

        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }
    }

}
