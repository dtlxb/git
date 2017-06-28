package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.fragment.stock.bigdata.EventChooseStockFragment;
import cn.gogoal.im.fragment.stock.bigdata.SubjectChooseStockFragment;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :大数据选股
 */
public class BigDataAcyivity extends BaseActivity {

    @BindView(R.id.tab_big_data)
    TabLayout tabBigData;

    @BindView(R.id.vp_big_data)
    ViewPager vpBigData;

    @Override
    public int bindLayout() {
        return R.layout.activity_bigdata;
    }

    private String[] bigDataTitle = {"主题选股", "事件选股"};

    @Override
    public void doBusiness(Context mContext) {

        setMyTitle("大数据选股", true);

        int index = getIntent().getIntExtra("big_index", 0);

        vpBigData.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? new SubjectChooseStockFragment() :
                        new EventChooseStockFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return bigDataTitle[position];
            }
        });

        tabBigData.setupWithViewPager(vpBigData);

        TabLayout.Tab tabAt = tabBigData.getTabAt(index);

        if (tabAt != null) {
            tabAt.select();
        }

        AppDevice.setTabLayoutWidth(tabBigData, 50);
    }

}
