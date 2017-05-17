package cn.gogoal.im.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.base.MyApp.getAppContext;

/**
 * 通用的activity页面
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private View mContentView;

    private static IPermissionListner mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);

        setContentView(mContentView);

        if (isFullScreem()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        ButterKnife.bind(this);

        initView(mContentView);

        EventBus.getDefault().register(this);

        doBusiness(this);

    }

    public boolean isFullScreem() {
        return false;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(mContentView);
        setOrientation();
    }

    public void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //封装运行时权限
    public static void requestRuntimePermission(String[] permissions, IPermissionListner listener) {
        mListener = listener;
        Activity activity = AppManager.getInstance().currentActivity();
        if (activity == null) {
            return;
        }
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onUserAuthorize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> deniedPermissionList = new ArrayList<>();
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissions[i]);
                            return;
                        }
                    }
                    if (deniedPermissionList.isEmpty()) {
                        mListener.onUserAuthorize();
                    } else {
                        mListener.onRefusedAuthorize(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initView(View view) {

    }

    public View getRootView() {
        return mContentView;
    }

    @Override
    public void resume() {
        onResume();
    }

    public XTitle setMyTitle(String title, boolean canBack) {
        XTitle xTitle = (XTitle) findViewById(R.id.title_bar);
        xTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            xTitle.setTitle(title);
            xTitle.setTitleColor(Color.BLACK);
        }
        if (canBack) {
            xTitle.setLeftImageResource(R.mipmap.image_title_back_0);
            xTitle.setLeftText(getString(R.string.str_title_back));
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

    public XTitle setMyTitle(@StringRes int titleString, boolean canBack) {
        String title = getString(titleString);
        XTitle xTitle = (XTitle) findViewById(R.id.title_bar);
        xTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            xTitle.setTitle(title);
            xTitle.setTitleColor(Color.BLACK);
        }
        if (canBack) {
            xTitle.setLeftImageResource(R.mipmap.image_title_back_0);
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
     *
     * @param recyclerView:初始化对象;
     * @param dividerId:分割线对象     : 0时为默认一条直线;int值 shape资源；null(不要分割线)
     */
    public static void initRecycleView(RecyclerView recyclerView, Integer dividerId) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApp.getAppContext());
        if (dividerId != null) {
            if (dividerId != 0x00) {
                try {
                    recyclerView.addItemDecoration(new NormalItemDecoration(recyclerView.getContext()));
                } catch (Exception e) {
                    throw new IllegalArgumentException("initRecycleView(RecyclerView,Integer)第二个参数必须是一个分割线shape资源或者填0或者null");
                }
            } else {
                recyclerView.addItemDecoration(new NormalItemDecoration(getAppContext()));
            }
        }
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 全局的下拉刷新样式
     */
    public static void iniRefresh(SwipeRefreshLayout mSwipeRefreshLayout) {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, AppManager.getInstance().currentActivity().getResources()
                        .getDisplayMetrics()));
    }

    /**
     * 修复输入法内存泄露
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * IM挤人逻辑
     */
    @Subscriber(tag = "show_client_status")
    public void imClientLoad(String msg) {
        DialogHelp.getMessageDialog(getActivity(), "账号已经在其他设备登录，点击\"确定\"跳转登录页面，重新登录。", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserUtils.logout(getActivity());
            }
        }).setCancelable(false)
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        fixInputMethodManagerLeak(this);
        try {
            Glide.with(this).pauseRequests();
        } catch (Exception e) {
        }
    }

    public BaseActivity getActivity() {
        return this;
    }

    public int getResColor(@ColorRes int colorId) {
        return ContextCompat.getColor(BaseActivity.this, colorId);
    }

    public Drawable getResDrawable(@DrawableRes int drawableid) {
        return ContextCompat.getDrawable(BaseActivity.this, drawableid);
    }
}
