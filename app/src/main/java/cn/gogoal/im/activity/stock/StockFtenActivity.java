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
import cn.gogoal.im.fragment.stock.CompanyFinanceFragment;
import cn.gogoal.im.fragment.stock.stockften.BusinessAnalysisFragment;
import cn.gogoal.im.fragment.stock.stockften.CompanyExecutivesFragment;
import cn.gogoal.im.fragment.stock.stockften.CompanyProfileFragment;
import cn.gogoal.im.fragment.stock.stockften.DividendFinancingFragment;
import cn.gogoal.im.fragment.stock.stockften.FinancialAnalysisFragment;
import cn.gogoal.im.fragment.stock.stockften.FinancialStatementsFragment;
import cn.gogoal.im.fragment.stock.stockften.ShareholderResearchFragment;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: F10
 */
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

        setMyTitle(stockName + "-资料F10", true);

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
                        return CompanyProfileFragment.getInstance(stockCode, stockName);
                    case 1:
                        return BusinessAnalysisFragment.getInstance(stockCode, stockName);
                    case 2:
                        return FinancialAnalysisFragment.getInstance(stockCode, stockName);
                    case 3:
                        return FinancialStatementsFragment.getInstance(stockCode, stockName);
                    case 4:
                        return DividendFinancingFragment.getInstance(stockCode, stockName);
                    case 5:
                        return ShareholderResearchFragment.getInstance(stockCode, stockName);
                    case 6:
                        return CompanyFinanceFragment.getInstance(stockCode, stockName);
                    case 7:
                        return CompanyExecutivesFragment.getInstance(stockCode, stockName);
                    default:
                        return CompanyProfileFragment.getInstance(stockCode, stockName);
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

        viewPagerFten.setCurrentItem(position);

        AppDevice.setTabLayoutWidth(tabLayoutFten, 5);
    }
}
