package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/13.
 */

public class IMAddFriendActivity extends BaseActivity {

    XTitle titleBar;
    @BindView(R.id.edit_your_message)
    SearchView editYourMessage;
    private String userId;

    @Override
    public int bindLayout() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void doBusiness(Context mContext) {

        titleBar = setMyTitle(R.string.title_add_friend, true);

        /*titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0x10);
                finish();
            }
        });*/

        userId = getIntent().getStringExtra("user_id");

        titleBar.setLeftText(R.string.tv_cancle);

        editYourMessage.setQuery(String.format(getString(R.string.str_add_friend_remark), UserUtils.getNickname()), false);

        //添加action
        XTitle.TextAction sendAction = new XTitle.TextAction("发送") {
            @Override
            public void actionClick(View view) {
                addFriend(userId);
            }
        };
        titleBar.addAction(sendAction);
    }

    public void addFriend(String userId) {
        final WaitDialog waitDialog = WaitDialog.getInstance("请求发送中...", R.mipmap.login_loading, true);
        waitDialog.show(getSupportFragmentManager());
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("friend_id", userId);
        params.put("text", editYourMessage.getQuery().toString());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                JSONObject data = result.getJSONObject("data");
                if (result.getInteger("code") == 0) {
                    if (data.getBoolean("success")) {
                        waitDialog.dismiss(true);
                        WaitDialog successDialog = WaitDialog.getInstance("好友请求发送成功",
                                R.mipmap.login_success, false);
                        successDialog.show(getSupportFragmentManager());
                        successDialog.dismiss(false, true);
                    } else {
                        waitDialog.dismiss(true);
                        final WaitDialog errorDialog = WaitDialog.getInstance("好友请求发送失败!请重试",
                                R.mipmap.login_error, false);
                        errorDialog.show(getSupportFragmentManager());
                        errorDialog.dismiss(false);
                    }
                } else {
                    waitDialog.dismiss(true);
                    final WaitDialog errorDialog = WaitDialog.getInstance("好友请求发送失败!请重试",
                            R.mipmap.login_error, false);
                    errorDialog.show(getSupportFragmentManager());
                    errorDialog.dismiss(false);
                }
            }

            @Override
            public void onFailure(String msg) {
                waitDialog.dismiss(true);
                final WaitDialog errorDialog = WaitDialog.getInstance(msg,
                        R.mipmap.login_error, false);
                errorDialog.show(getSupportFragmentManager());
                errorDialog.dismiss(false);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_FRIEND, ggHttpInterface).startGet();
    }

}
