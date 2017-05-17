package cn.gogoal.im.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.PlayerActivity;
import cn.gogoal.im.activity.WatchLiveActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * Created by dave.
 * Date: 2017/5/10.
 * Desc: description
 */
public class InviteAuthDialog extends BaseCentDailog {

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_invite_auth;
    }

    @Override
    public int getWidth() {
        return 78 * AppDevice.getWidth(getActivity()) / 100;
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public float getDimAmount() {
        return super.getDimAmount();
    }

    public static InviteAuthDialog newInstance(String liveSource, String invite_id) {
        InviteAuthDialog dialog = new InviteAuthDialog();
        Bundle bundle = new Bundle();
        bundle.putString("liveSource", liveSource);
        bundle.putString("invite_id", invite_id);
        dialog.setArguments(bundle);
        return dialog;
    }

    private String liveSource;
    private String invite_id;
    private EditText editInvite;
    private TextView textError;

    @Override
    public void bindView(View view) {

        liveSource = getArguments().getString("liveSource");
        invite_id = getArguments().getString("invite_id");

        editInvite = (EditText) view.findViewById(R.id.editInvite);
        textError = (TextView) view.findViewById(R.id.textError);
        TextView textCancel = (TextView) view.findViewById(R.id.textCancel);
        TextView textSure = (TextView) view.findViewById(R.id.textSure);

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteAuthDialog.this.dismiss();
                //textError.setVisibility(View.GONE);
            }
        });

        textSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInviteCode(view.getContext());
            }
        });
    }

    /*
    * 验证邀请码
    * */
    private void validateInviteCode(final Context mContext) {

        final String invite_code = editInvite.getText().toString();

        if (invite_code.equals("")) {
            textError.setVisibility(View.VISIBLE);
            textError.setText("请输入邀请码");
            return;
        }
        if (!invite_code.matches(".*[^ ].*")) {
            textError.setVisibility(View.VISIBLE);
            textError.setText("邀请码输入错误");
            return;
        }

        final String filename= StringUtils.getRandomString(10);

        Map<String, String> param = new HashMap<>();
        param.put("user_token", filename);
        param.put("video_id", invite_id);
        param.put("invite_code", invite_code);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 101) {

                        SPTools.saveString(invite_id, filename);

                        InviteAuthDialog.this.dismiss();

                        if (liveSource.equals("live")) {
                            Intent intent = new Intent(mContext, WatchLiveActivity.class);
                            intent.putExtra("live_id", invite_id);
                            mContext.startActivity(intent);
                        } else if (liveSource.equals("video")) {
                            Intent intent = new Intent(mContext, PlayerActivity.class);
                            intent.putExtra("live_id", invite_id);
                            mContext.startActivity(intent);
                        }
                    } else if (data.getIntValue("code") == 202) {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("邀请码已被使用");
                    } else {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("邀请码输入错误");
                    }
                } else {
                    textError.setVisibility(View.VISIBLE);
                    textError.setText("邀请码输入错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                textError.setVisibility(View.VISIBLE);
                textError.setText("邀请码输入错误");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VALIDATE_CODE, ggHttpInterface).startGet();
    }

}
