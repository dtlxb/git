package cn.gogoal.im.activity;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.format.DateUtils;
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
    RadioGroup group_redgreen;
    @BindView(R.id.redup_rb)
    RadioButton redup_rb;
    @BindView(R.id.greenup_rb)
    RadioButton greenup_rb;

    private boolean redUp;

    @Override
    public int bindLayout() {
        return R.layout.activity_redgreen_setting;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("涨跌显示设置", true);

        redUp = SPTools.getBoolean("stock_redup_greendown", false);
        if (redUp) {
            redup_rb.setChecked(true);
        } else {
            greenup_rb.setChecked(true);
        }

        group_redgreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int rbId = group.getCheckedRadioButtonId();
                SPTools.saveBoolean("stock_redup_greendown", rbId == redup_rb.getId());
            }
        });
    }
}
