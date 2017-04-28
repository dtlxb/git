package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.adapter.SocialLiveAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BannerBean;
import cn.gogoal.im.bean.SocialLiveBean;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.ComingSoonDialog;
import cn.gogoal.im.ui.view.AutoScrollViewPager;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :社交.
 */
public class SocialContactFragment extends BaseFragment {

    @BindView(R.id.swiperefresh_social)
    SwipeRefreshLayout refreshSocial;

    @BindView(R.id.socialRecycler)
    RecyclerView socialRecycler;

    @BindView(R.id.socialViewPager)
    AutoScrollViewPager bannerPager;

    private SocialLiveAdapter adapter;

    /**
     * banner适配器和数据集
     */
    private List<BannerBean.Banner> bannerImageUrls;
    private BannerAdapter bannerAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_social_contact;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_social);

        BaseActivity.iniRefresh(refreshSocial);
        BaseActivity.initRecycleView(socialRecycler, 0);
        socialRecycler.setNestedScrollingEnabled(false);

        refreshSocial.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLiveData();
            }
        });

        getBannerImage();
        getLiveData();
    }

    @OnClick({R.id.imgFloatAction, R.id.linearLive, R.id.linearConference, R.id.linearRoadshow, R.id.linearClub})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgFloatAction: //发起直播
                getUserValid();
                break;
            case R.id.linearLive: //直播
                NormalIntentUtils.go2WebActivity(getActivity(), AppConst.GG_LIVE_LIST,"GoGoal直播");
                break;
            case R.id.linearConference: //会务
            case R.id.linearRoadshow: //路演
            case R.id.linearClub: //俱乐部
                new ComingSoonDialog().show(getFragmentManager());
                break;
        }
    }

    /*
    * 能否发起直播
    * */
    private void getUserValid() {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 1) {
                        if (data.getString("live_id") != null) {
                            Intent intent = new Intent(getContext(), LiveActivity.class);
                            intent.putExtra("live_id", data.getString("live_id"));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(getActivity(), CreateLiveActivity.class));
                        }
                    } else {
                        DialogHelp.getMessageDialog(getContext(), "您暂时没有权限直播，请联系客服申请！").show();
                    }
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VIDEO_MOBILE, ggHttpInterface).startGet();
    }

    /**
     * 获取直播列表数据
     */
    private void getLiveData() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("page", "1");
        param.put("rows", "100");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                SocialLiveBean object = JSONObject.parseObject(responseInfo, SocialLiveBean.class);
                if (object.getCode() == 0) {
                    List<SocialLiveData> listData = object.getData();
                    adapter = new SocialLiveAdapter(getActivity(), listData);
                    socialRecycler.setAdapter(adapter);
                } else if (object.getCode() == 1001) {

                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }

                refreshSocial.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                refreshSocial.setRefreshing(false);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
    }

    /**
     * 获取banner数据
     */
    private void getBannerImage() {
        AppDevice.setViewWidth$Height(bannerPager,
                ViewGroup.LayoutParams.MATCH_PARENT,
                AppDevice.getWidth(getContext()) / 3);

        bannerImageUrls = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerImageUrls);
        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setScrollFactgor(10);

        Map<String, String> map = new HashMap<>();
        map.put("ad_position", "4");
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
