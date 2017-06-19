package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.activity.ToolsListActivity;
import cn.gogoal.im.activity.ToolsSettingActivity;
import cn.gogoal.im.adapter.InvestmentResearchAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BannerBean;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.BannerUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.AutoScrollViewPager;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.NoAlphaItemAnimator;

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

    @BindView(R.id.tv_flag_tools)
    TextView tvFlagTools;

    private List<ToolData.Tool> mGridData;
    private InvestmentResearchAdapter toolsAdapter;

    /**
     * banner适配器和数据集
     */
    private List<BannerBean.Banner> bannerPagerDatas;
    private BannerAdapter bannerAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_investmentresearch;
    }

    @Override
    public void doBusiness(Context mContext) {
        XTitle xTitle = setFragmentTitle(R.string.title_found);

        XTitle.ImageAction settingAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.img_setting)) {
            @Override
            public void actionClick(View view) {
                //跳设置小工具
                Intent intent = new Intent(view.getContext(), ToolsSettingActivity.class);
                ArrayList<ToolData.Tool> data = new ArrayList<>();
                for (ToolData.Tool tool : mGridData) {
                    if (!tool.isSimulatedArg()) {
                        data.add(tool);
                    }
                }
                intent.putParcelableArrayListExtra("selected_tools", data);
                startActivity(intent);
            }
        };

        XTitle.ImageAction messageAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.home_bottom_tab_icon_message_normal)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(), MessageHolderActivity.class));
            }
        };

        xTitle.addAction(settingAction, 0);
        xTitle.addAction(messageAction, 1);

//        setFragmentTitle(R.string.title_found).addAction(new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.img_setting)) {
//            @Override
//            public void actionClick(View view) {
//
//                /*//跳帮助
//                NormalIntentUtils.go2WebActivity(
//                        view.getContext(),
//                        AppConst.GG_HELP,
//                        getString(R.string.str_helper));*/
//                //跳设置小工具
//                Intent intent = new Intent(view.getContext(), ToolsSettingActivity.class);
//                ArrayList<ToolData.Tool> data = new ArrayList<>();
//                for (ToolData.Tool tool : mGridData) {
//                    if (!tool.isSimulatedArg()) {
//                        data.add(tool);
//                    }
//                }
//                intent.putParcelableArrayListExtra("selected_tools", data);
//                startActivity(intent);
//            }
//        });


        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                AppDevice.isLowDpi() ? 3 : 4,
                StaggeredGridLayoutManager.VERTICAL));

        mGridData = new ArrayList<>();
        toolsAdapter = new InvestmentResearchAdapter(getActivity(), mGridData);
        mRecyclerView.setAdapter(toolsAdapter);

        getBannerImage();
        getTouYan();
    }

    private void getBannerImage() {
        AppDevice.setViewWidth$Height(bannerPager,
                ViewGroup.LayoutParams.MATCH_PARENT,
                245 * AppDevice.getWidth(getContext()) / 750);

        bannerPagerDatas = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerPagerDatas);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setScrollFactgor(5);

        Map<String, String> map = new HashMap<>();
        map.put("ad_position", "3");

        new GGOKHTTP(map, GGOKHTTP.GET_AD_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    bannerPagerDatas.addAll(JSONObject.parseObject(responseInfo, BannerBean.class).getData());

                    if (bannerPagerDatas.size() > 1) {
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
                    bannerPagerDatas.add(spaceBanner);
                    bannerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
    }

    public void getTouYan() {
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        map.put("isShow", "1");

        new GGOKHTTP(map, GGOKHTTP.GET_USERCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                int code = object.getIntValue("code");
                if (code == 0) {
                    showView(true);
                    mGridData.clear();
                    List<ToolData.Tool> tools = JSONObject.parseArray(
                            object.getJSONArray("data").toJSONString(), ToolData.Tool.class);
                    mGridData.addAll(tools);

                    addSpace();

                    toolsAdapter.notifyDataSetChanged();

                } else if (code == 1001) {
                    showView(false);
                    mGridData.clear();
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
    }

    private void showView(boolean show) {
        try {
            mRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvFlagTools.setVisibility(show ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void addSpace() {
        ToolData.Tool clone = mGridData.get(0).clone();
        clone.setSimulatedArg(true);
        if (AppDevice.isLowDpi()) {
            switch (mGridData.size() % 3) {
                case 0:
                    //不添加
                    break;
                case 1:
                    mGridData.add(clone);
                    mGridData.add(clone);
                    break;
                case 2:
                    mGridData.add(clone);
                    break;
            }
        } else {
            switch (mGridData.size() % 4) {
                case 0:
                    //不添加
                    break;
                case 1:
                    mGridData.add(clone);
                    mGridData.add(clone);
                    mGridData.add(clone);
                    break;
                case 2:
                    mGridData.add(clone);
                    mGridData.add(clone);
                    break;
                case 3:
                    mGridData.add(clone);
                    break;
            }

        }
    }

    @OnClick(R.id.btn_tools_center)
    void click(View view) {
        startActivity(new Intent(view.getContext(), ToolsListActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        getTouYan();
    }

    private class BannerAdapter extends PagerAdapter {

        private List<BannerBean.Banner> banners;

        private BannerAdapter(List<BannerBean.Banner> banners) {
            this.banners = banners;
        }

        @Override
        public int getCount() {
            return banners == null ? 0 : banners.size();
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
            Glide.with(getActivity()).load(banners.get(position).getImage()).into(view);
            container.addView(view);

            final String[] params = {banners.get(position).getTarget_url()};

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BannerUtils.getInstance(
                            v.getContext(),
                            banners.get(position).getType(),
                            params).go();
                }
            });
            return view;
        }
    }
}
