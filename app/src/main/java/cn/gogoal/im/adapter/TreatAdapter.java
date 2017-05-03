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
import cn.gogoal.im.fragment.stock.TreatFragment;

import static cn.gogoal.im.activity.copy.CopyStockDetailActivity.TREAT_TYPE_MING_XI;
import static cn.gogoal.im.activity.copy.CopyStockDetailActivity.TREAT_TYPE_WU_DANG;

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

    public TreatAdapter(FragmentManager fm, Context context, String stockCode,boolean fromStockDetail) {
        super(fm);
        this.context = context;
        this.stockCode = stockCode;
        this.fromStockDetail=fromStockDetail;
    }

    @Override
    public Fragment getItem(int i) {
        return TreatFragment.getInstance(stockCode,
                i == 0 ? TREAT_TYPE_WU_DANG : TREAT_TYPE_MING_XI,
                fromStockDetail);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "五档" : "明细";
    }

    public View getTabView(int position) {
        TextView view = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        view.setBackgroundResource(R.drawable.selected_tab_wudang_mingxi);
        view.setText(getPageTitle(position));

        return view;
    }
}
