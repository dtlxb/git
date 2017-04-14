package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.fragment.stock.MyStockTabNewsFragment;
import cn.gogoal.im.ui.view.XLayout;


/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:自选股中的[新闻，公告,研报]
 */

public class MyStockNewsActivity extends BaseActivity {
    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindArray(R.array.mystock_news_title)
    String[] newsTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_mystock_news;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(getIntent().getStringExtra("news_title"), true);

        int index = getIntent().getIntExtra("showTabIndex", 0);//展示tab的index

        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MyStockTabNewsFragment.getInstance(position);
            }

            @Override
            public int getCount() {
                return newsTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return newsTitle[position];
            }
        });

        tabs.setupWithViewPager(pager);
        try {
            tabs.getTabAt(index).select();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
