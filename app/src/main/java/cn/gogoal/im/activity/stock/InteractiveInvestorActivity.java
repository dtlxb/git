package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/5/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :投资者互动
 */
public class InteractiveInvestorActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        Stock stock=getIntent().getParcelableExtra("stock_info");
        String titleText=stock.getStock_name()+"("+stock.getStock_code()+")"
                +"\n"+ StockUtils.getTreatState()+" "+ CalendarUtils.getCurrentTime("MM-dd HH:mm");
        XTitle xTitle = setMyTitle(titleText, true);
        xTitle.setSubTitleColor(Color.parseColor("#80ffffff"));
        xTitle.setTitleColor(Color.WHITE);


    }

}
