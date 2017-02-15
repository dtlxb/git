package com.gogoal.app.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.fragment.ContactsFragment;
import com.gogoal.app.fragment.FunctionsFragment;
import com.gogoal.app.fragment.MessageFragment;
import com.gogoal.app.fragment.MineFragment;

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
        MessageFragment messageFragment = new MessageFragment();
        ContactsFragment contactsFragment = new ContactsFragment();
        FunctionsFragment functionsFragment = new FunctionsFragment();
        MineFragment mineFragment = new MineFragment();

        List<Fragment> tabFragments = new ArrayList<>();
        tabFragments.add(messageFragment);
        tabFragments.add(contactsFragment);
        tabFragments.add(functionsFragment);
        tabFragments.add(mineFragment);

        SimpleFragmentPagerAdapter tabAdapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), tabFragments, mainTabArray);

        vpMain.setAdapter(tabAdapter);
        tabMain.setupWithViewPager(vpMain);

        for (int i = 0; i < mainTabArray.length; i++) {
            TabLayout.Tab tab = tabMain.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }

    }

    class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        private String[] titles;

        private SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public View getTabView(int position) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_layout_item, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_main_tab);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_main_tab);
            switch (position) {
                case 0:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_message);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_contact);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_function);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_mine);
                    break;
            }
            tv.setText(titles[position]);
            return view;
        }
    }
}
