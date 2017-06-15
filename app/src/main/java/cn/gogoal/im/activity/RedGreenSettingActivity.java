package cn.gogoal.im.activity;

import android.content.Context;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.SPTools;

/**
 * Created by huangxx on 2017/4/26.
 */

public class RedGreenSettingActivity extends BaseActivity {

    //k线空实心
    @BindView(R.id.group_redgreen)
    RadioGroup group_redGreen;
    @BindView(R.id.redup_rb)
    RadioButton redUp_rb;
    @BindView(R.id.greenup_rb)
    RadioButton greenUp_rb;

    private boolean stockUnNormalShow;

    @Override
    public int bindLayout() {
        return R.layout.activity_redgreen_setting;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("涨跌显示设置", true);

        stockUnNormalShow = SPTools.getBoolean("stock_unNormal_show", false);
        if (stockUnNormalShow) {
            greenUp_rb.setChecked(true);
        } else {
            redUp_rb.setChecked(true);
        }

        group_redGreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int rbId = group.getCheckedRadioButtonId();
                SPTools.saveBoolean("stock_unNormal_show", rbId == greenUp_rb.getId());
            }
        });
    }
}
