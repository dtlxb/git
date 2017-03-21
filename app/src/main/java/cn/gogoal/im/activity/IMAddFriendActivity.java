package cn.gogoal.im.activity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/13.
 */

public class IMAddFriendActivity extends BaseActivity {

    XTitle titleBar;
    @BindView(R.id.edit_your_message)
    EditText editYourMessage;

    @Override
    public int bindLayout() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void doBusiness(Context mContext) {

        titleBar = setMyTitle("朋友验证", true);

        titleBar.setLeftText("取消");
        //添加action
        XTitle.TextAction sendAction = new XTitle.TextAction("发送") {
            @Override
            public void actionClick(View view) {
                AddFirend();
            }
        };
        titleBar.addAction(sendAction);
    }

    public void AddFirend() {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("friend_id", editYourMessage.getText().toString());
        params.put("text", "你好啊！！！");
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(IMAddFriendActivity.this, "好友请求发送成功!");
                    editYourMessage.setText("");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_FRIEND, ggHttpInterface).startGet();
    }

}
