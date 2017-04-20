package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.adapter.SectionAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BannerBean;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.DrawableCenterTextView;

/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :投研
 * <p>
 * investment research
 */
public class InvestmentResearchFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<SectionTouYanData> mData;
    private SectionAdapter sectionAdapter;

    /**
     * banner适配器和数据集
     */
    private List<BannerBean.Banner> bannerImageUrls;
    private BannerAdapter bannerAdapter;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("投研");
        mRecyclerView.setBackgroundColor(getResColor(R.color.stock_market_bg));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mData = new ArrayList<>();
        sectionAdapter = new SectionAdapter(getActivity(), mData);
        mRecyclerView.setAdapter(sectionAdapter);

        getBannerImage();

        getTouYan();
    }

    private void getBannerImage() {
        final View bannerView = creatBannerView();
        sectionAdapter.addHeaderView(bannerView);

        Map<String, String> map = new HashMap<>();
        map.put("ad_position", "7");
        new GGOKHTTP(map, GGOKHTTP.GET_AD_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                KLog.e(responseInfo);

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    bannerImageUrls.addAll(JSONObject.parseObject(responseInfo, BannerBean.class).getData());
                    bannerAdapter.notifyDataSetChanged();
                } else {
                    BannerBean.Banner spaceBanner = new BannerBean.Banner();
                    spaceBanner.setImage("");
                    bannerImageUrls.add(spaceBanner);
                    bannerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    @NonNull
    /**动态创建banner视图*/
    private View creatBannerView() {
        LinearLayout bannerView = new LinearLayout(getContext());
        final ViewPager bannerPager = new ViewPager(getContext());
        LinearLayout.LayoutParams root = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bannerView.setOrientation(LinearLayout.VERTICAL);
        bannerView.setBackgroundResource(R.drawable.shape_line_bottom);
        bannerView.setLayoutParams(root);

        bannerPager.setId(R.id.banner_pager_id);
        bannerImageUrls = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerImageUrls);
        bannerPager.setAdapter(bannerAdapter);
        LinearLayout.LayoutParams pagerParams = new LinearLayout.LayoutParams(
                AppDevice.getWidth(getContext()),
                235 * AppDevice.getWidth(getContext()) / 740);
        bannerView.addView(bannerPager, 0, pagerParams);

        DrawableCenterTextView searchView = new DrawableCenterTextView(getContext());
        searchView.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, AppDevice.dp2px(getContext(), 35));
        searchParams.setMargins(AppDevice.dp2px(getContext(), 10), AppDevice.dp2px(getContext(), 10),
                AppDevice.dp2px(getContext(), 10), AppDevice.dp2px(getContext(), 10));
        searchView.setBackgroundResource(R.drawable.shape_search_activity_edit);
        searchView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.mipmap.img_search)
                , null, null, null);
        searchView.setCompoundDrawablePadding(AppDevice.dp2px(getContext(), 5));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StockSearchActivity.class));
            }
        });
        searchView.setText("股票代码/名称/拼音");
        searchView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        searchView.setId(R.id.banner_search_id);
        bannerView.addView(searchView, 1, searchParams);
        return bannerView;
    }

    public void getTouYan() {
        mData.clear();
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());

        new GGOKHTTP(map, GGOKHTTP.GET_TOUYAN_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    List<TouYan.DataBean> touYanList = JSONObject.parseObject(responseInfo, TouYan.class).getData();
                    for (int i = 0; i < touYanList.size(); i++) {
                        TouYan.DataBean dataBean = touYanList.get(i);
                        mData.add(new SectionTouYanData(true, dataBean.getTitle()));
                        List<TouYan.DataBean.Item> itemList = dataBean.getDatas();
                        for (TouYan.DataBean.Item item : itemList) {
                            mData.add(new SectionTouYanData(item));
                        }

                        TouYan.DataBean.Item spaceItem = creatSpaceItem();

                        if (itemList.size() % 4 == 1) {
                            mData.add(new SectionTouYanData(spaceItem));
                            mData.add(new SectionTouYanData(spaceItem));
                            mData.add(new SectionTouYanData(spaceItem));
                        } else if (itemList.size() % 4 == 2) {
                            mData.add(new SectionTouYanData(spaceItem));
                            mData.add(new SectionTouYanData(spaceItem));
                        } else if (itemList.size() % 4 == 3) {
                            mData.add(new SectionTouYanData(spaceItem));
                        }
                    }
                    sectionAdapter.notifyDataSetChanged();
                } else if (code == 1001) {

                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();

    }

    @NonNull
    private TouYan.DataBean.Item creatSpaceItem() {
        TouYan.DataBean.Item spaceItem = new TouYan.DataBean.Item();
        spaceItem.setDesc("");
        spaceItem.setIconUrl("");
        spaceItem.setIsShow(0);
        spaceItem.setPosition(0);
        spaceItem.setType(0);
        return spaceItem;
    }

    private class BannerAdapter extends PagerAdapter {

        private List<BannerBean.Banner> imageUrls;

        private BannerAdapter(List<BannerBean.Banner> imageUrls) {
            this.imageUrls = imageUrls;
        }

        @Override
        public int getCount() {
            return imageUrls == null ? 0 : imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setAdjustViewBounds(true);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageDisplay.loadNetImage(container.getContext(), imageUrls.get(position).getImage(), view);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.toast(v.getContext(), "banner::" + position);
                }
            });
            return view;
        }
    }
}
