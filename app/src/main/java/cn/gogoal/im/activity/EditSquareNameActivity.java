package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/6.
 */

public class EditSquareNameActivity extends BaseActivity {

    @BindView(R.id.edit_square_name)
    EditText editSquareName;
    XTitle xTitle;
    private TextView rightAction;
    private String conversationID;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_square_name;
    }

    @Override
    public void doBusiness(Context mContext) {
        String squareName = getIntent().getStringExtra("square_name");
        conversationID = getIntent().getStringExtra("conversation_id");
        editSquareName.setText(squareName);
        editSquareName.setSelection(squareName.length());
        xTitle = setMyTitle("群名称", true);
        XTitle.TextAction action = new XTitle.TextAction("完成") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("corrected_square_name", editSquareName.getText().toString());
                intent.putExtras(bundle);
                setResult(AppConst.SQUARE_ROOM_EDIT_NAME, intent);
                correctSquareName();
                finish();
            }
        };
        xTitle.addAction(action, 0);
        rightAction = (TextView) xTitle.getViewByAction(action);
        rightAction.setTextColor(getResColor(R.color.complete_text));
    }

    //修改群名称
    public void correctSquareName() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        params.put("group_name", editSquareName.getText().toString());
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(getActivity(), "群名称修改成功");
                    //刷新conversation
                    KLog.e(conversationID);
                    AVImClientManager.getInstance().refreshConversation(conversationID);
                    JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", new JSONArray());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.get("conversationID").equals(conversationID)) {
                            ((JSONObject) jsonArray.get(i)).put("nickname", editSquareName.getText().toString());
                        }
                    }
                    SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", jsonArray);
                } else {
                    UIHelper.toast(getActivity(), "群名称修改失败");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getActivity(), msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.UPDATE_GROUP_INFO, ggHttpInterface).startGet();
    }
}
