package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.CompanyProfileAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ProfileData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 公司概况
 */
public class CompanyProfileFragment extends BaseFragment {

    @BindView(R.id.sticky_recyView)
    RecyclerView recySticky;
    @BindView(R.id.tv_sticky_header_view)
    TextView textSticky;

    private String stockCode;
    private String stockName;

    private CompanyProfileAdapter adapter;

    public static CompanyProfileFragment getInstance(String stockCode, String stockName) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_profile;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        initRecyView();
        getProfileData();
    }

    private void initRecyView() {

        textSticky.setVisibility(View.VISIBLE);

        initRecycleView(recySticky, null);

        recySticky.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View stickyInfoView = recyclerView.findChildViewUnder(
                        textSticky.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    textSticky.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }

                View transInfoView = recyclerView.findChildViewUnder(
                        textSticky.getMeasuredWidth() / 2, textSticky.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {

                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - textSticky.getMeasuredHeight();

                    if (transViewStatus == FtenUtils.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            textSticky.setTranslationY(dealtY);
                        } else {
                            textSticky.setTranslationY(0);
                        }
                    } else if (transViewStatus == FtenUtils.NONE_STICKY_VIEW) {
                        textSticky.setTranslationY(0);
                    }
                }
            }
        });
    }

    private void getProfileData() {

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);

                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONObject basic_data = data.getJSONObject("basic_data");
                    JSONObject issue_data = data.getJSONObject("issue_data");
                    JSONObject profileData = new JSONObject();
                    profileData.putAll(basic_data);
                    profileData.putAll(issue_data);

                    List<ProfileData> listData = new ArrayList<>();

                    for (int i = 0; i < FtenUtils.profileName.length; i++) {
                        if (i < 31) {
                            listData.add(new ProfileData("基本资料", FtenUtils.profileName[i],
                                    profileData.getString(FtenUtils.profileContent[i])));
                        } else {
                            listData.add(new ProfileData("发行相关", FtenUtils.profileName[i],
                                    profileData.getString(FtenUtils.profileContent[i])));
                        }
                    }

                    adapter = new CompanyProfileAdapter(getActivity(), listData);
                    recySticky.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SUMMARY, ggHttpInterface).startGet();
    }
}
