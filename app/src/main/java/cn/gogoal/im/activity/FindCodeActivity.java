package cn.gogoal.im.activity;

import android.content.Context;
import android.widget.EditText;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/18.
 */

public class FindCodeActivity extends BaseActivity {

    private XTitle xTitle;

    //手机号
    @BindView(R.id.edit_new_code)
    EditText editNewCode;
    //验证码
    @BindView(R.id.edit_confirm_code)
    EditText editConfirmCode;

    @BindView(R.id.login_cofirm)
    SelectorButton loginCofirm;

    @Override
    public int bindLayout() {
        return R.layout.activity_find_code;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
    }

    private void initTitle() {
        xTitle = setMyTitle(R.string.str_correct_code, true);
    }
}
