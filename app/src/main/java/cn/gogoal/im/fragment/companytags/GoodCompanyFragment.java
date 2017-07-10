package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.companytags.CompanyTagData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/30.
 */

public class GoodCompanyFragment extends BaseFragment {

    @BindView(R.id.rv_goodCompany)
    RecyclerView recyclerView;

    @BindView(R.id.layout_goodCompany)
    LinearLayout layout_goodCompany;

    private String stockCode;
    private int fragmentType;
    private List<CompanyTagData> companyTagDatas;
    private GoodCompanyAdapter goodCompanyAdapter;

    public static GoodCompanyFragment newInstance(String code, int type) {
        GoodCompanyFragment fragment = new GoodCompanyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_good_company;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        layout_goodCompany.setVisibility(View.GONE);

        companyTagDatas = new ArrayList<>();
        goodCompanyAdapter = new GoodCompanyAdapter(companyTagDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //绑定
        recyclerView.setAdapter(goodCompanyAdapter);
        getGoodCompany();
    }

    public void getGoodCompany() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JsonObject result = JsonUtils.parseJsonObject(responseInfo);
                KLog.e(responseInfo);
                if (result.get("code").getAsInt() == 0) {

                    Gson gson = new Gson();
                    BaseBeanList<CompanyTagData> beanList = gson.fromJson(responseInfo,
                            new TypeToken<BaseBeanList<CompanyTagData>>() {
                            }.getType());

                    companyTagDatas.addAll(beanList.getData());
                    if (companyTagDatas.size() > 0) {
                        layout_goodCompany.setVisibility(View.VISIBLE);
                        CompanyTagData companyTagData = new CompanyTagData(0, 0, 0, 0, 0);
                        companyTagDatas.add(0, companyTagData);
                        if (companyTagDatas.size() > 1) {
                            companyTagDatas.remove(companyTagDatas.size() - 1);
                        }
                        goodCompanyAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_GOOD_COMPANY_HISTORY, ggHttpInterface).startGet();
    }

    private class GoodCompanyAdapter extends CommonAdapter<CompanyTagData, BaseViewHolder> {

        GoodCompanyAdapter(List<CompanyTagData> datas) {
            super(R.layout.item_company_tag, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, CompanyTagData data, int position) {
            View line = holder.getView(R.id.line);
            TextView tvYear = holder.getView(R.id.tv_year);
            TextView tv_Q1 = holder.getView(R.id.tv_Q1);
            TextView tv_Q2 = holder.getView(R.id.tv_Q2);
            TextView tv_Q3 = holder.getView(R.id.tv_Q3);
            TextView tv_Q4 = holder.getView(R.id.tv_Q4);
            if (position == companyTagDatas.size() - 1) {
                line.setVisibility(View.GONE);
            }
            if (position == 0) {
                tvYear.setText("历史年份");
                tv_Q1.setText("Q1");
                tv_Q2.setText("Q2");
                tv_Q3.setText("Q3");
                tv_Q4.setText("Q4");
            } else {
                tvYear.setText(data.getYear() + "年");
                tv_Q1.setText(stringParse(data.getQ1() + ""));
                tv_Q2.setText(stringParse(data.getQ2() + ""));
                tv_Q3.setText(stringParse(data.getQ3() + ""));
                tv_Q4.setText(stringParse(data.getQ4() + ""));
            }
        }
    }

    private SpannableStringBuilder stringParse(String tag) {
        String text;
        int colorRes = R.color.textColor_333333;
        switch (tag) {
            case "-2":
                text = "未出报表";
                break;
            case "-1":
                colorRes = R.color.company_normal;
                text = "平凡";
                break;
            case "0":
                colorRes = R.color.company_history;
                text = "历史好公司";
                break;
            case "1":
                colorRes = R.color.company_good;
                text = "好公司";
                break;
            case "2":
                colorRes = R.color.company_hope;
                text = "希望之星";
                break;
            default:
                text = "未出报表";
                break;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder(text); // 包装字体内容
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(colorRes)); // 设置字体颜色
        sb.setSpan(fcs, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }
}
