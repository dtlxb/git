package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hply.imagepicker.view.StatusBarUtil;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.widget.LeftWindowPopu;

/**
 * author wangjd on 2017/3/23 0023.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class TestActivity extends BaseActivity {

    Toolbar toolbar;

    @BindView(R.id.checkbox)
    CheckBox checkBox;

    @BindView(R.id.test_root_view)
    DrawerLayout drawerLayout;

    @BindView(R.id.layout_menu)
    View  menu;

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void doBusiness(Context mContext) {
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.END,true);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                drawerLayout.setDrawerLockMode(isChecked?DrawerLayout.LOCK_MODE_LOCKED_CLOSED:DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
    }

    @Override
    public void setStatusBar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        if (drawerLayout!=null) {
            StatusBarUtil.with(TestActivity.this).setTranslucentForDrawerLayout(drawerLayout);
        }else {
            UIHelper.toast(getActivity(),"出错");
        }
    }

    public static class MyLeftDialog extends LeftWindowPopu {

        @Override
        public int getLayoutRes() {
            return R.layout.dialog_share_layout;
        }

        @Override
        public void bindView(View v) {

        }
    }
}
