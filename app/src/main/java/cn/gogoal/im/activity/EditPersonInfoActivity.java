package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/18.
 */

public class EditPersonInfoActivity extends BaseActivity {

    private XTitle xTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness(Context mContext) {

        initTitle();
    }

    private void initTitle() {
        xTitle = setMyTitle(R.string.str_edit_person_info, true);
        //添加action
        XTitle.TextAction jumpAction = new XTitle.TextAction("跳过") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        xTitle.addAction(jumpAction, 0);
        TextView rigisterView = (TextView) xTitle.getViewByAction(jumpAction);
        rigisterView.setTextColor(getResColor(R.color.colorPrimary));
    }
}
