package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.fragment.stock.TreatFragment;

/**
 * author wangjd on 2017/5/3 0003.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class TreatAdapter extends FragmentPagerAdapter {

    private Context context;
    private String stockCode;
    private boolean fromStockDetail;

    public TreatAdapter(FragmentManager fm, Context context, String stockCode, boolean fromStockDetail) {
        super(fm);
        this.context = context;
        this.stockCode = stockCode;
        this.fromStockDetail = fromStockDetail;
    }

    @Override
    public Fragment getItem(int i) {
        TreatFragment treatFragment = null;
        switch (i) {
            case 0:
                treatFragment = TreatFragment.getInstance(stockCode, AppConst.TREAT_TYPE_WU_DANG,
                        fromStockDetail);
                break;
            case 1:
                treatFragment = TreatFragment.getInstance(stockCode, AppConst.TREAT_TYPE_MING_XI,
                        fromStockDetail);
                break;
            case 2:
                treatFragment = TreatFragment.getInstance(stockCode, AppConst.TREAT_TYPE_MONEY,
                        fromStockDetail);
                break;
        }
        return treatFragment;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "五档";
                break;
            case 1:
                title = "明细";
                break;
            case 2:
                title = "资金";
                break;
        }
        return title;
    }

    public View getTabView(int position) {
        TextView view = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        view.setBackgroundResource(R.drawable.selected_tab_wudang_mingxi);
        view.setText(getPageTitle(position));

        return view;
    }
}
