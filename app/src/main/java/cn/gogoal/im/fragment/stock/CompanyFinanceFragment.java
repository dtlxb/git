package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;

import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.StockFtenActivity;
import cn.gogoal.im.base.BaseFragment;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :公司财务（个股Tab）
 */
public class CompanyFinanceFragment extends BaseFragment {
    @Override
    public int bindLayout() {
        return R.layout.fragment_company_finance;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.btnFten})
    public void ftenOnClick() {
        Intent intent = new Intent(getActivity(), StockFtenActivity.class);
        intent.putExtra("stockCode", "000001");
        intent.putExtra("stockName", "平安银行");
        intent.putExtra("position", "0");
        startActivity(intent);
    }
}
