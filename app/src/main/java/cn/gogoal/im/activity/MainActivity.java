package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SimpleFragmentPagerAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.fragment.ContactsFragment;
import cn.gogoal.im.fragment.FoundFragment;
import cn.gogoal.im.fragment.MessageFragment;
import cn.gogoal.im.fragment.MineFragment;

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

        final ContactsFragment contactsFragment = new ContactsFragment();     // TAB2 人脉

        FoundFragment foundFragment = new FoundFragment();              // TAB3 投研

        final MineFragment mineFragment = new MineFragment();                 // TAB4 我的

        Boolean needRefresh = getIntent().getExtras().getBoolean("isFromLogin", false);

        SPTools.saveBoolean("needRefresh", needRefresh);

        List<Fragment> tabFragments = new ArrayList<>();
        tabFragments.add(messageFragment);
        tabFragments.add(contactsFragment);
        tabFragments.add(foundFragment);
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
                        contactsFragment.refreshAdapter();
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
                contactsFragment.srcollShowIndexBar(state==0&&vpMain.getCurrentItem()==1);
            }
        });

    }
}
