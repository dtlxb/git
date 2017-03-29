package cn.gogoal.im.activity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

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
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/28.
 */

public class SearchPersonSquareActivity extends BaseActivity {

    @BindView(R.id.edit_your_message)
    EditText editYourMessage;

    XTitle xTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_search_person_square;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setMyTitle(R.string.title_search_anyone, true);
        xTitle.setLeftText(R.string.tv_cancle);
        //添加action
        XTitle.TextAction sendAction = new XTitle.TextAction("发送") {
            @Override
            public void actionClick(View view) {
                findSquare();
            }
        };
        xTitle.addAction(sendAction);
    }

    public void findSquare() {

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", editYourMessage.getText().toString());
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(SearchPersonSquareActivity.this, "群申请发送成功!");
                    editYourMessage.setText("");
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.APPLY_INTO_GROUP, ggHttpInterface).startGet();
    }
}
