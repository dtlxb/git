package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.adapter.SectionAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BannerBean;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.AutoScrollViewPager;

import static cn.gogoal.im.R.id.recyclerView;

/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :投研
 * <p>
 * investment research
 */
public class InvestmentResearchFragment extends BaseFragment {

    @BindView(recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.vp_fund_banner)
    AutoScrollViewPager bannerPager;

    private List<SectionTouYanData> mData;
    private SectionAdapter sectionAdapter;


    /**
     * banner适配器和数据集
     */
    private List<BannerBean.Banner> bannerImageUrls;
    private BannerAdapter bannerAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_investmentresearch;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_found);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                AppDevice.isLowDpi() ? 3 : 4,
                StaggeredGridLayoutManager.VERTICAL));

        mData = new ArrayList<>();
        sectionAdapter = new SectionAdapter(getActivity(), mData);
        mRecyclerView.setAdapter(sectionAdapter);

        getBannerImage();
        getTouYan();
    }

    private void getBannerImage() {
        AppDevice.setViewWidth$Height(bannerPager,
                ViewGroup.LayoutParams.MATCH_PARENT,
                AppDevice.getWidth(getContext()) / 3);

        bannerImageUrls = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerImageUrls);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setScrollFactgor(10);

        Map<String, String> map = new HashMap<>();
        //map.put("ad_position", "7");
        map.put("ad_position", "3");
        new GGOKHTTP(map, GGOKHTTP.GET_AD_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    bannerImageUrls.addAll(JSONObject.parseObject(responseInfo, BannerBean.class).getData());
                    if (bannerImageUrls.size() > 1) {
                        bannerPager.startAutoScroll(3000);
                        bannerPager.showIndicator(true);
                    } else {
                        bannerPager.stopAutoScroll();
                        bannerPager.showIndicator(false);
                    }
                    try {
                        bannerAdapter.notifyDataSetChanged();
                        bannerPager.setAdapter(bannerAdapter);
                    } catch (Exception e) {
                        e.getMessage();
                    }
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

    @OnClick(R.id.tv_banner_2search)
    void click(View view) {
        startActivity(new Intent(view.getContext(), StockSearchActivity.class));
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
                    FileUtil.writeRequestResponse(responseInfo);

                    List<TouYan.DataBean> touYanList = JSONObject.parseObject(responseInfo, TouYan.class).getData();
                    for (int i = 0; i < touYanList.size(); i++) {
                        TouYan.DataBean dataBean = touYanList.get(i);
                        mData.add(new SectionTouYanData(true, dataBean.getTitle()));
                        List<TouYan.DataBean.Item> itemList = dataBean.getDatas();
                        for (TouYan.DataBean.Item item : itemList) {
                            mData.add(new SectionTouYanData(item, false));
                        }

                        //模拟填充空数据
                        creatSpaceItem(itemList);
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

    private void creatSpaceItem(List<TouYan.DataBean.Item> itemList) {
        TouYan.DataBean.Item spaceItem = new TouYan.DataBean.Item();
        spaceItem.setDesc("");
        spaceItem.setIconUrl("");
        spaceItem.setIsShow(0);
        spaceItem.setPosition(0);
        spaceItem.setType(0);

        if (AppDevice.isLowDpi()) {
            switch (itemList.size() % 3) {
                case 1:
                    mData.add(new SectionTouYanData(spaceItem, true));
                    mData.add(new SectionTouYanData(spaceItem, true));
                    break;
                case 2:
                    mData.add(new SectionTouYanData(spaceItem, true));
                    break;
            }
        } else {
            switch (itemList.size() % 4) {
                case 1:
                    mData.add(new SectionTouYanData(spaceItem, true));
                    mData.add(new SectionTouYanData(spaceItem, true));
                    mData.add(new SectionTouYanData(spaceItem, true));
                    break;
                case 2:
                    mData.add(new SectionTouYanData(spaceItem, true));
                    mData.add(new SectionTouYanData(spaceItem, true));
                    break;
                case 3:
                    mData.add(new SectionTouYanData(spaceItem, true));
                    break;
            }
        }
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
            ImageDisplay.loadNetImage(container.getContext(), imageUrls.get(position).getImage(), view, 0);
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
