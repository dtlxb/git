package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hply.imagepicker.view.StatusBarUtil;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/**
 * author wangjd on 2017/3/23 0023.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class TestActivity extends BaseActivity {

    Toolbar toolbar;

    View rootView;

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void setStatusBar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        StatusBarUtil.with(this).setTranslucentForView(R.id.view_need_offset);
    }
}
