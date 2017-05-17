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
import cn.gogoal.im.ui.dialog.MessageFullScreen;
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

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageFullScreen.newInstance("一带一路 （国家级顶层战略） 锁定\n" +
                        "“一带一路”（英文：The Belt and Road，缩写B&R）是“丝绸之路经济带”和“21世纪海上丝绸之路”的简称。它将充分依靠中国与有关国家既有的双多边机制，借助既有的、行之有效的区域合作平台，一带一路旨在借用古代丝绸之路的历史符号，高举和平发展的旗帜，积极发展与沿线国家的经济合作伙伴关系，共同打造政治互信、经济融合、文化包容的利益共同体、命运共同体和责任共同体。\n" +
                        "2015年3月28日，国家发展改革委、外交部、商务部联合发布了《推动共建丝绸之路经济带和21世纪海上丝绸之路的愿景与行动》。[1-2] \n" +
                        "“一带一路\"经济区开放后，承包工程项目突破3000个。2015年，我国企业共对“一带一路”相关的49个国家进行了直接投资，投资额同比增长18.2%。2015年，我国承接“一带一路”相关国家服务外包合同金额178.3亿美元，执行金额121.5亿美元，同比分别增长42.6%和23.45%。\n" +
                        "2016年6月底，中欧班列累计开行1881列，其中回程502列，实现进出口贸易总额170亿美元。2016年6月起，中欧班列穿上了统一的“制服”，深蓝色的集装箱格外醒目，品牌标志以红、黑为主色调，以奔驰的列车和飘扬的丝绸为造型，成为丝绸之路经济带蓬勃发展的最好代言与象征。[3] ")
                .show(getSupportFragmentManager());
            }
        });

//        final ImageView img_bg= (ImageView) findViewById(R.id.image_bg);
//        final ImageView img_force= (ImageView) findViewById(R.id.image_force);
//        img_bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ViewGroup.LayoutParams params = img_force.getLayoutParams();
//                params.height=img_bg.getHeight();
//                params.width=img_bg.getWidth();
//                img_force.setLayoutParams(params);
//                img_bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });

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
