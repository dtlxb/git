package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.socks.library.KLog;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
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

    @BindView(R.id.image_test)
    ImageView imageView;

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

        findViewById(R.id.btn_test_match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatGroupHelper.setGroupAvatar("5912baf58d6d810058278bd3", new AvatarTakeListener() {
                    @Override
                    public void success(Bitmap bitmap) {
                        KLog.e(bitmap);
                    }

                    @Override
                    public void failed(Exception e) {

                    }
                });
            }
        });

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
