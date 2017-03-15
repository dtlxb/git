package cn.gogoal.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/13.
 */

public class IMAddFriendActivity extends BaseActivity {

    XTitle titleBar;
    @BindView(R.id.edit_your_message)
    EditText editYourMessage;

    @Override
    public int bindLayout() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void doBusiness(Context mContext) {
        titleBar = setMyTitle("朋友验证", true);
        titleBar.setLeftText("取消");
        //添加action
        XTitle.TextAction sendAction = new XTitle.TextAction("发送") {
            @Override
            public void actionClick(View view) {

            }
        };
        titleBar.addAction(sendAction);
    }

}
