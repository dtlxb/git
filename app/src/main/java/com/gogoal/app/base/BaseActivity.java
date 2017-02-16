package com.gogoal.app.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gogoal.app.R;
import com.gogoal.app.common.BuildProperties;
import com.gogoal.app.ui.view.StatusBarUtil;
import com.gogoal.app.ui.view.XTitle;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * 通用的activity页面
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    private View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);

        setContentView(mContentView);

        setStatusBar();

        ButterKnife.bind(this);

        initView(mContentView);

        doBusiness(this);
    }

    public void setContentView(View contextView) {
        super.setContentView(mContentView);
        setStatusBarColor(R.color.colorTitle);
    }

    private void setStatusBarColor(@ColorRes int color) {
        StatusBarUtil.setColor(this,ContextCompat.getColor(this,color));
    }

    /**通配 状态栏
         *
         * if(miui){
         *     执行miui方案
         * }else if(Flyme){
         *     执行魅族方案
         * }else{
         *     VERSION >=Android6.0{
         *         执行原生方案，设置黑色调状态栏图标
         *     }else{
         *         直接将状态栏背景设置黑色
         *     }
         * }
         * */
    private void setStatusBar() {
        if (isMiUIV6()){
            setMIUIStatusBarTextColor(this,1);
        }else if (isFlyme()){
            setMeizuStatusBarDarkIcon(this,true);
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {
                setStatusBarColor(android.R.color.black);
            }
        }
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void resume() {
        onResume();
    }

    public XTitle setMyTitle(String title, boolean canBack){
        XTitle xTitle= (XTitle) findViewById(R.id.title_bar);
        xTitle.setImmersive(true);
        if (!TextUtils.isEmpty(title)){
            xTitle.setTitle(title);
            xTitle.setTitleColor(Color.BLACK);
        }
        if (canBack){
            xTitle.setLeftImageResource(R.mipmap.image_title_back_b);
            xTitle.setLeftText("返回");
            xTitle.setLeftTextColor(Color.BLACK);
            xTitle.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        return xTitle;
    }

    /**
     * 初始化垂直列表的RecycleView
     * @param recyclerView:初始化对象;
     * @param dividerId:分割线对象 : 0时为默认一条直线;int值 shape资源；null(不要分割线)
     */
    public void initRecycleView(RecyclerView recyclerView, Integer dividerId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (dividerId != null) {
            if (dividerId != 0x00) {
                try {
                    DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
                    itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), dividerId));//R.drawable.shape_divider
                    recyclerView.addItemDecoration(itemDecoration);
                } catch (Exception e) {
                    throw new IllegalArgumentException("initRecycleView(RecyclerView,Integer)第二个参数必须是一个分割线shape资源或者填0或者null");
                }
            } else {
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
            }
        }
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /*
  * 针对特殊页面通知栏颜色设置--MIUI
  * */
    public void setMIUIStatusBarTextColor(Activity context, int type) {
        if (!isMiUIV6()) {
            return;
        }
        Window window = context.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 针对特殊页面通知栏颜色设置-- Flyme
    * */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    protected boolean isMiUIV6() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            String name = prop.getProperty("ro.miui.ui.version.name", "");
            if (null != name && name.length() >= 2) {
                String str = name.substring(1, name.length());
                int version_vode = Integer.parseInt(str);
                return version_vode >= 6;
            } else {
                return false;
            }
        } catch (final IOException e) {
            return false;
        }
    }

    public boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public BaseActivity getActivity() {
        return this;
    }

}
