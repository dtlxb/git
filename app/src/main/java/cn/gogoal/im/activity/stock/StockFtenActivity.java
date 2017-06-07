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
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.fragment.stock.stockften.BusinessAnalysisFragment;
import cn.gogoal.im.fragment.stock.stockften.CompanyProfileFragment;

public class StockFtenActivity extends BaseActivity {

    @BindView(R.id.tablayout_ften)
    TabLayout tabLayoutFten;
    @BindView(R.id.vp_ften)
    ViewPager viewPagerFten;
    @BindArray(R.array.stock_ften_array)
    String[] arrStockFten;

    private String stockName;
    private String stockCode;
    private int position;

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_ften;
    }

    @Override
    public void doBusiness(Context mContext) {

        position = getIntent().getIntExtra("position", 0);
        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");

        setMyTitle(stockName + "-F10", true);

        setNewsTab();
    }

    /**
     * 设置新闻-F10的页面
     */
    private void setNewsTab() {

        viewPagerFten.setOffscreenPageLimit(arrStockFten.length);
        viewPagerFten.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new CompanyProfileFragment();
                    case 1:
                        return new BusinessAnalysisFragment();
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        return new CompanyProfileFragment();
                }
            }

            public int getCount() {
                return arrStockFten.length;
            }

            public CharSequence getPageTitle(int position) {
                return arrStockFten[position];
            }
        });
        tabLayoutFten.setupWithViewPager(viewPagerFten);

        AppDevice.setTabLayoutWidth(tabLayoutFten, 5);
    }
}
