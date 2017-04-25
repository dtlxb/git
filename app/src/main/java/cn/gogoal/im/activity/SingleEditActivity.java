package cn.gogoal.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.KeyboardLaunchLinearLayout;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :编辑、公司，姓名、职位Activity
 */
public class SingleEditActivity extends BaseActivity {

    public static String EDIT_MY_INFO_TYPE = "edit_my_info_type";

    public static final int EDIT_MY_INFO_TYPE_NAME = 0;//名字

    public static final int EDIT_MY_INFO_TYPE_COMPANY = 1;//公司

    public static final int EDIT_MY_INFO_TYPE_DUTY = 2;//职位

    @BindView(R.id.sv_edit_info)
    SearchView svEditInfo;

    @BindView(R.id.tv_single_edit_flag)
    TextView tvSingleEditFlag;

    @BindView(R.id.keyboard_layout)
    KeyboardLaunchLinearLayout keyboardLayout;

    private String localCacheKey = "";//修改的信息对应的本地缓存字段

    @Override
    public int bindLayout() {
        return R.layout.activity_single_edit;
    }

    @Override
    public void doBusiness(Context mContext) {
        final int editType = getIntent().getIntExtra(EDIT_MY_INFO_TYPE, 0);//编辑类型

        final String[] mapReqKey = {"name", "company", "duty",};
        final String[] typeString = {getString(R.string.str_setting_name), getString(R.string.str_setting_company),
                getString(R.string.str_setting_duty)};

        String title = typeString[editType];

        switch (editType) {
            case EDIT_MY_INFO_TYPE_NAME:
                svEditInfo.setQuery(UserUtils.getNickname(), false);
                tvSingleEditFlag.setVisibility(View.VISIBLE);
                localCacheKey = "nickname";
                break;
            case EDIT_MY_INFO_TYPE_COMPANY:
                tvSingleEditFlag.setVisibility(View.GONE);
                svEditInfo.setQuery(UserUtils.getorgName(), false);
                localCacheKey = "organization_name";
                break;
            case EDIT_MY_INFO_TYPE_DUTY:
                tvSingleEditFlag.setVisibility(View.GONE);
                svEditInfo.setQuery(UserUtils.getDuty(), false);
                localCacheKey = "duty";
                break;
        }

        XTitle xTitle = setMyTitle(title, true);
        xTitle.addAction(new XTitle.TextAction(getString(R.string.complete), getResColor(R.color.colorPrimary)) {
            @Override
            public void actionClick(View view) {

                final WaitDialog loadingDialog = WaitDialog.getInstance("修改中...", R.mipmap.login_loading, true);
                loadingDialog.show(getSupportFragmentManager());

                if (!TextUtils.isEmpty(svEditInfo.getQuery())) {
                    Map<String, String> map = new HashMap<>();
                    map.put(mapReqKey[editType], svEditInfo.getQuery().toString());
                    UserUtils.updataNetUserInfo(map, new UserUtils.UpdataListener() {
                        @Override
                        public void success(String responseInfo) {

                            loadingDialog.dismiss();
                            WaitDialog.getInstance(typeString[editType] + "信息修改成功",
                                    R.mipmap.login_success, false).show(getSupportFragmentManager());

                            //TODO 更新本地缓存，刷新上一个页面
                            UserUtils.updataLocalUserInfo(localCacheKey,svEditInfo.getQuery().toString());
                            AppManager.getInstance().sendMessage("updata_userinfo","更新用户信息");

                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SingleEditActivity.this.finish();
                                }
                            }, 1000);
                        }

                        @Override
                        public void failed(String errorMsg) {
                            loadingDialog.dismiss();

                            final WaitDialog waitDialog = WaitDialog.getInstance("信息修改失败\n\r"+
                                    errorMsg,
                                    R.mipmap.login_error, false);

                            waitDialog.show(getSupportFragmentManager());

                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    waitDialog.dismiss();
                                }
                            }, 1000);
                        }
                    });
                }
            }
        });

        svEditInfo.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!keyboardLayout.isSoftKeyboardPop()) {
                    if (!hasFocus) {
                        finish();
                    }
                } else {
                    AppDevice.hideSoftKeyboard(svEditInfo);
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
