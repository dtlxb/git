package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :社交.
 */
public class SocialContactFragment extends BaseFragment {

    @Override
    public int bindLayout() {
        return R.layout.fragment_social_contact;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.imgFloatAction})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgFloatAction: //发起直播
                getUserValid();
                break;
        }
    }

    /*
    * 能否发起直播
    * */
    private void getUserValid() {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 1) {
                        if (data.getString("live_id") != null) {
                            Intent intent = new Intent(getContext(), LiveActivity.class);
                            intent.putExtra("live_id", data.getString("live_id"));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(getActivity(), CreateLiveActivity.class));
                        }
                    } else {
                        DialogHelp.getMessageDialog(getContext(), "您暂时没有权限直播，请联系客服申请！").show();
                    }
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VIDEO_MOBILE, ggHttpInterface).startGet();
    }
}
