package cn.gogoal.im.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hply.imagepicker.view.StatusBarUtil;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.base.MyApp.getAppContext;

/**
 * 通用的activity页面
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    private View mContentView;

    private static IPermissionListner mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);

        setContentView(mContentView);

//        setStatusBar();

        AppManager.getInstance().addActivity(this);

        ButterKnife.bind(this);

        initView(mContentView);

        EventBus.getDefault().register(this);

        doBusiness(this);

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(mContentView);
        setStatusBar();
    }

    public void setStatusBar(){
        StatusBarUtil.with(BaseActivity.this).initForGogoal(false);
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

    public XTitle setMyTitle(@StringRes int titleString, boolean canBack) {
        String title = getString(titleString);
        XTitle xTitle = (XTitle) findViewById(R.id.title_bar);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApp.getAppContext());
        if (dividerId != null) {
            if (dividerId != 0x00) {
                try {
                    DividerItemDecoration itemDecoration = new DividerItemDecoration(getAppContext(), LinearLayoutManager.VERTICAL);
                    itemDecoration.setDrawable(ContextCompat.getDrawable(getAppContext(), dividerId));//R.drawable.shape_divider
                    recyclerView.addItemDecoration(itemDecoration);
                } catch (Exception e) {
                    throw new IllegalArgumentException("initRecycleView(RecyclerView,Integer)第二个参数必须是一个分割线shape资源或者填0或者null");
                }
            } else {
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getAppContext(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
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
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,AppManager.getInstance().currentActivity().getResources()
                        .getDisplayMetrics()));
    }

    private ProgressDialog mDialog;

    protected ProgressDialog showWaitDialog(@StringRes int messageId) {
        if (mDialog == null) {
            if (messageId <= 0) {
                mDialog = DialogHelp.getProgressDialog(this, true);
            } else {
                String message = getResources().getString(messageId);
                mDialog = DialogHelp.getProgressDialog(this, message, true);
            }
        }
        mDialog.show();

        return mDialog;
    }

    /**
     * hide waitDialog
     */
    protected void hideWaitDialog() {
        ProgressDialog dialog = mDialog;
        if (dialog != null) {
            mDialog = null;
            try {
                dialog.cancel();
                // dialog.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 修复输入法内存泄露
     *
     * @param destContext
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AppManager.getInstance().finishActivity(this);
        fixInputMethodManagerLeak(this);
    }

    public BaseActivity getActivity(){
        return this;
    }

    public int getResColor(@ColorRes int colorId){
        return ContextCompat.getColor(BaseActivity.this,colorId);
    }

    public Drawable getResDrawable(@DrawableRes int drawableid){
        return ContextCompat.getDrawable(BaseActivity.this, drawableid);
    }
}
