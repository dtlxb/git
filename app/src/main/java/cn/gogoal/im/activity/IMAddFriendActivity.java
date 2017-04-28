package cn.gogoal.im.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/13.
 */

public class IMAddFriendActivity extends BaseActivity {

    XTitle titleBar;
    @BindView(R.id.edit_your_message)
    SearchView editYourMessage;
    private int userId = -1;
    private Handler handler;

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

        userId = getIntent().getIntExtra("user_id", -1);

        titleBar.setLeftText(R.string.tv_cancle);

        editYourMessage.setQuery(String.format(getString(R.string.str_add_friend_remark), UserUtils.getUserName()), false);

        //添加action
        XTitle.TextAction sendAction = new XTitle.TextAction("发送") {
            @Override
            public void actionClick(View view) {
                addFirend(userId);
            }
        };
        titleBar.addAction(sendAction);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                IMAddFriendActivity.this.finish();
            }
        };
    }

    public void addFirend(int userId) {

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("friend_id", String.valueOf(userId));
        params.put("text", editYourMessage.getQuery().toString());
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                Log.e("=========responseInfo", responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                JSONObject data = result.getJSONObject("data");
                if (data.getBoolean("success")) {
                    UIHelper.toast(IMAddFriendActivity.this, "好友请求发送成功!");
                    handler.sendEmptyMessageDelayed(0x01, 1000);
                } else {
                    UIHelper.toast(IMAddFriendActivity.this, "好友请求发送失败!请重试");
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
