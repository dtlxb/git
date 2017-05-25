package cn.gogoal.im.activity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.TimeCount;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;


/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :更换手机号绑定
 */
public class ChangePhoneNumberActivity extends BaseActivity {

    @BindView(R.id.et_change_phone)
    XEditText etChangePhone;

    @BindView(R.id.et_pase_code)
    EditText etPaseCode;

    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @BindView(R.id.btn_ok)
    SelectorButton btnOk;

    private TimeCount mTimeCount;
    private String mobile;

    @Override
    public int bindLayout() {
        return R.layout.activity_change_phone_number;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_change_phone_number, true);

        AppDevice.setEditTextInhibitInputSpace(etChangePhone);
        AppDevice.setEditTextInhibitInputSpace(etPaseCode);

        mTimeCount = new TimeCount(120000, 1000, tvGetCode);
    }

    @OnClick({R.id.btn_ok, R.id.tv_get_code})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                mobile = etChangePhone.getText().toString().trim();
                if (!StringUtils.checkPhoneString(mobile)) {
                    UIHelper.toast(view.getContext(), "手机号格式不正确");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("token", UserUtils.getToken());
                map.put("mobile", mobile);
                new GGOKHTTP(map, GGOKHTTP.SEND_CAPTCHA, new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {
                        KLog.e(responseInfo);
                        int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                        if (code==0){
                            mTimeCount.start();//倒计时
                            UIHelper.toast(getActivity(),"验证码已发送,请查收");
                        }else {
                            tvGetCode.setEnabled(true);
                            tvGetCode.setClickable(true);
                            tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                            tvGetCode.setText("获取验证码");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                }).startGet();

                break;
            case R.id.btn_ok:
                if (StringUtils.checkPhoneString(mobile) &&
                        StringUtils.checkVerificationCode(etPaseCode.getText().toString())){

                    Map<String, String> map0 = new HashMap<>();
                    map0.put("token", UserUtils.getToken());
                    map0.put("captcha", etPaseCode.getText().toString().trim());

                    new GGOKHTTP(map0, GGOKHTTP.BIND_MOBILE, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            KLog.e(responseInfo);
                            int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                            if (code==0){
                                JSONObject data=JSONObject.parseObject(responseInfo).getJSONObject("data");
                                if (data.getBoolean("success")){
                                    UIHelper.toast(getActivity(),"手机号绑定成功!");
                                    finish();
                                    AppManager.getInstance().sendMessage("updata_userinfo","更新数据咯");

                                    UserUtils.updataLocalUserInfo("mobile",mobile);
                                }else {
                                    UIHelper.toast(getActivity(),"手机号绑定失败!\n\r验证码不正确");
                                }
                            }else {
                                UIHelper.toastResponseError(getActivity(),responseInfo);
                            }
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    }).startGet();

                }else {
                    UIHelper.toast(getActivity(),"验证码格式不正确");
                    return;
                }
                break;
        }
    }
}
