package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.fragment.stock.BondFragment;
import cn.gogoal.im.fragment.stock.FundFragment;
import cn.gogoal.im.fragment.stock.HKFragment;
import cn.gogoal.im.fragment.stock.HuShenFragment;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.fragment.stock.HuShenFragment.REFRESH_TYPE_PARENT_BUTTON;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :行情.
 */
public class NewMarketActivity extends BaseActivity {
    @BindView(R.id.search_market)
    AppCompatTextView searchMarket;

    @BindView(R.id.tablayout_market)
    TabLayout tablayoutMarket;

    @BindView(R.id.vp_market)
    ViewPager vpMarket;

    @BindArray(R.array.market_tab_top)
    String[] marketTabTitles;
    private ArrayList<Fragment> marketFragments;

    private RotateAnimation rotateAnimation;

    private ImageView actionView;

    @Override
    public int bindLayout() {
        return R.layout.activity_new_market;
    }

    @Override
    public void doBusiness(final Context mContext) {

        final HuShenFragment huShenFragment = new HuShenFragment();
        final HKFragment hkFragment = new HKFragment();
        FundFragment fundFragment = new FundFragment();
        BondFragment bondFragment = new BondFragment();

        actionView = (ImageView) setMyTitle(getString(R.string.str_market), true)
                .addAction(
                        new XTitle.ImageAction(getResDrawable(R.mipmap.img_refresh)) {
                            @Override
                            public void actionClick(View view) {
                                huShenFragment.setRefreshType(REFRESH_TYPE_PARENT_BUTTON);
                                huShenFragment.getMarketInformation();

                                AppManager.getInstance().sendMessage("START_ANIMATIOM");
                            }
                        });


        marketFragments = new ArrayList<>();

        marketFragments.add(huShenFragment);
        marketFragments.add(hkFragment);
        marketFragments.add(fundFragment);
        marketFragments.add(bondFragment);

        vpMarket.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return marketFragments.get(position);
            }

            @Override
            public int getCount() {
                return marketFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return marketTabTitles[position];
            }
        });
        vpMarket.setOffscreenPageLimit(3);
        tablayoutMarket.setupWithViewPager(vpMarket);
    }

    @Subscriber(tag = "STOP_ANIMATION")
    void stopAnimation(String msg){
        if (rotateAnimation!=null){
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation,actionView,R.mipmap.img_refresh);
        }else {
            actionView.clearAnimation();
            actionView.setImageResource(R.mipmap.img_refresh);
        }
    }

    @Subscriber(tag = "START_ANIMATIOM")
    void startAnimation(String msg){
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(actionView, R.mipmap.loading_fresh);
        rotateAnimation.startNow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            rotateAnimation.cancel();
            rotateAnimation=null;
        }catch (Exception e){
            rotateAnimation=null;
            e.getMessage();
        }
    }
}
