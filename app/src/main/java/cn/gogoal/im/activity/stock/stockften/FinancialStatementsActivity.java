package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.stockften.FinancialStatementsLeftAdapter;
import cn.gogoal.im.adapter.stockften.FinancialStatementsRightAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.view.MySyncHorizontalScrollView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务报表
 */
public class FinancialStatementsActivity extends BaseActivity {

    public static final int GENRE_PROFIT_STATEMENTS = 1; //利润表
    public static final int GENRE_BALANCE_SHEET = 2; //负债表
    public static final int GENRE_CASH_FLOW = 3; //现金表

    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.checkFrom)
    AppCompatCheckBox checkFrom;

    @BindView(R.id.rightHorscrollView)
    MySyncHorizontalScrollView rightHorscrollView;

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;
    private int genre;
    private String stock_finance_type;

    private JSONObject financialData;

    private String season = "0";
    private String stype = "1";

    private FinancialStatementsLeftAdapter leftAdapter;
    private FinancialStatementsRightAdapter rightAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_financial_statements;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");
        genre = getIntent().getIntExtra("genre", GENRE_PROFIT_STATEMENTS);
        stock_finance_type = getIntent().getStringExtra("stock_finance_type");

        if (genre == GENRE_PROFIT_STATEMENTS) {
            textTitle.setText(stockName + "-利润表");
        } else if (genre == GENRE_BALANCE_SHEET) {
            textTitle.setText(stockName + "-资产负债表");
        } else if (genre == GENRE_CASH_FLOW) {
            textTitle.setText(stockName + "-现金流量表");
        }

        checkFrom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    checkFrom.setTextColor(getResColor(R.color.colorPrimary));
                } else {
                    checkFrom.setTextColor(getResColor(R.color.textColor_333333));
                }
            }
        });

        setLeftListData();
        getStatementsData(season, stype);
    }

    /**
     * 设置左边列表数据
     */
    private void setLeftListData() {
        ArrayList<String> titleList = new ArrayList<>();
        String[] stringList = null;

        if (genre == GENRE_PROFIT_STATEMENTS) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.profitForm1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.profitForm2;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.profitForm3;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.profitForm4;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.profitForm5;
            }

            titleList.add("利润表");
        } else if (genre == GENRE_BALANCE_SHEET) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.assetsForm1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.assetsForm2;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.assetsForm3;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.assetsForm4;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.assetsForm5;
            }

            titleList.add("资产负债表");
        } else if (genre == GENRE_CASH_FLOW) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.cashForm1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.cashForm2;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.cashForm3;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.cashForm4;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.cashForm5;
            }

            titleList.add("现金流量表");
        }

        for (int i = 0; i < stringList.length; i++) {
            titleList.add(stringList[i]);
        }

        leftAdapter = new FinancialStatementsLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }

    private void getStatementsData(String season, final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("season", season);
        param.put("stock_finance_type", stock_finance_type);
        param.put("page", "1");
        if (season.equals("0")) {
            param.put("stype", "1");
        } else {
            param.put("stype", "0");
        }

        if (genre != GENRE_BALANCE_SHEET) {
            param.put("report_stype", "1");
        }

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    financialData = object.getJSONObject("data");
                    setRightListData(financialData, stype);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(FinancialStatementsActivity.this, R.string.net_erro_hint);
            }
        };

        if (genre == GENRE_PROFIT_STATEMENTS) {
            new GGOKHTTP(param, GGOKHTTP.FIREPORT_PROFIT_DIST, ggHttpInterface).startGet();
        } else if (genre == GENRE_BALANCE_SHEET) {
            new GGOKHTTP(param, GGOKHTTP.FIREPORT_BALANCE_SHEET, ggHttpInterface).startGet();
        } else if (genre == GENRE_CASH_FLOW) {
            new GGOKHTTP(param, GGOKHTTP.FIREPORT_FLOW_CASH, ggHttpInterface).startGet();
        }
    }

    /**
     * 设置右边列表数据
     */
    private void setRightListData(JSONObject data, String stype) {
        ArrayList<JSONObject> contList = new ArrayList<>();
        contList.add(data.getJSONObject("title"));

        String[] stringList = null;

        if (genre == GENRE_PROFIT_STATEMENTS) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.profitForm1_1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.profitForm2_1;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.profitForm3_1;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.profitForm4_1;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.profitForm5_1;
            }
        } else if (genre == GENRE_BALANCE_SHEET) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.assetsForm1_1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.assetsForm2_1;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.assetsForm3_1;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.assetsForm4_1;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.assetsForm5_1;
            }
        } else if (genre == GENRE_CASH_FLOW) {
            if (stock_finance_type.equals("1")) {
                stringList = FtenUtils.cashForm1_1;
            } else if (stock_finance_type.equals("2")) {
                stringList = FtenUtils.cashForm2_1;
            } else if (stock_finance_type.equals("3")) {
                stringList = FtenUtils.cashForm3_1;
            } else if (stock_finance_type.equals("4")) {
                stringList = FtenUtils.cashForm4_1;
            } else if (stock_finance_type.equals("5")) {
                stringList = FtenUtils.cashForm5_1;
            }
        }

        for (int i = 0; i < stringList.length; i++) {
            contList.add(data.getJSONObject(stringList[i]));
        }

        rightAdapter = new FinancialStatementsRightAdapter(FinancialStatementsActivity.this,
                contList, stype);
        lsvRight.setAdapter(rightAdapter);
    }

    @OnClick({R.id.btn_back, R.id.checkFrom, R.id.radioBtn0, R.id.radioBtn1, R.id.radioBtn2,
            R.id.radioBtn3, R.id.radioBtn4})
    public void ChartTabClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.checkFrom:
                if (checkFrom.isChecked()) {
                    stype = "0";
                    setRightListData(financialData, stype);
                } else {
                    stype = "1";
                    setRightListData(financialData, stype);
                }
                break;
            case R.id.radioBtn0:
                season = "0";
                stype = "1";
                getStatementsData(season, stype);
                checkFrom.setVisibility(View.GONE);
                checkFrom.setChecked(false);
                break;
            case R.id.radioBtn1:
                season = "4";
                getStatementsData(season, stype);
                checkFrom.setVisibility(View.VISIBLE);
                break;
            case R.id.radioBtn2:
                season = "2";
                getStatementsData(season, stype);
                checkFrom.setVisibility(View.VISIBLE);
                break;
            case R.id.radioBtn3:
                season = "1";
                getStatementsData(season, stype);
                checkFrom.setVisibility(View.VISIBLE);
                break;
            case R.id.radioBtn4:
                season = "3";
                getStatementsData(season, stype);
                checkFrom.setVisibility(View.VISIBLE);
                break;
        }
    }
}
