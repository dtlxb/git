package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SectionAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BannerBean;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.AutoScrollViewPager;
import cn.gogoal.im.ui.view.XTitle;

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
        setFragmentTitle(R.string.title_found).addAction(new XTitle.TextAction(getString(R.string.str_helper)) {
            @Override
            public void actionClick(View view) {

                NormalIntentUtils.go2WebActivity(
                        view.getContext(),
                        AppConst.GG_HELP,
                        getString(R.string.str_helper));
            }
        });



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
                245*AppDevice.getWidth(getContext()) / 750);

        bannerImageUrls = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerImageUrls);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setScrollFactgor(5);

        Map<String, String> map = new HashMap<>();
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
            Glide.with(getActivity()).load(imageUrls.get(position).getImage()).into(view);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.toast(v.getContext(), "banner::" + position);
                    NormalIntentUtils.go2WebActivity(v.getContext(),AppConst.WEB_URL_LLJ,"this is title");
                }
            });
            return view;
        }
    }
}
