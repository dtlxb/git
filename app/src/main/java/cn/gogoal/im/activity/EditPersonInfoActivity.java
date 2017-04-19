package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/18.
 */

public class EditPersonInfoActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.layout_person_headpic)
    FrameLayout layoutPersonHeadpic;

    @BindView(R.id.image_person_headpic)
    ImageView imagePersonHeadpic;

    @BindView(R.id.edit_person_name)
    EditText editPersonName;

    @BindView(R.id.edit_company_name)
    EditText editCompanyName;

    @BindView(R.id.edit_job_name)
    EditText editJobName;

    @BindView(R.id.login_cofirm)
    SelectorButton loginCofirm;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        ImageDisplay.loadResAvatar(EditPersonInfoActivity.this, R.mipmap.login_gogoal, imagePersonHeadpic);
    }

    private void editPersonInfos() {
        final Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("avatar", "20");
        params.put("name", editPersonName.getText().toString());
        params.put("company", editCompanyName.getText().toString());
        params.put("duty", editJobName.getText().toString());
        loginCofirm.setEnabled(false);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginCofirm.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    boolean success = data.getBoolean("success");
                    if (success) {
                        UIHelper.toast(EditPersonInfoActivity.this, "资料修改成功！");
                        Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
                        intent.putExtra("isFromLogin", true);
                        startActivity(intent);
                    } else {
                        UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                    }
                } else {
                    UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                }
            }

            @Override
            public void onFailure(String msg) {
                loginCofirm.setEnabled(true);
                UIHelper.toast(EditPersonInfoActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.UPDATE_ACCOUNT_INFO, ggHttpInterface).startGet();
    }

    private void initTitle() {
        xTitle = setMyTitle(R.string.str_edit_person_info, true);
        //添加action
        XTitle.TextAction jumpAction = new XTitle.TextAction("跳过") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
                intent.putExtra("isFromLogin", true);
                startActivity(intent);
                finish();
            }
        };
        xTitle.addAction(jumpAction, 0);
        TextView rigisterView = (TextView) xTitle.getViewByAction(jumpAction);
        rigisterView.setTextColor(getResColor(R.color.colorPrimary));
    }

    @OnClick({R.id.login_cofirm, R.id.layout_person_headpic})
    void function(View view) {
        switch (view.getId()) {
            case R.id.login_cofirm:
                editPersonInfos();
                break;
            case R.id.layout_person_headpic:
                break;
            default:
                break;
        }
    }
}
