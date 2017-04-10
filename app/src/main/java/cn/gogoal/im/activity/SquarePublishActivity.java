package cn.gogoal.im.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/5.
 */

public class SquarePublishActivity extends BaseActivity {

    @BindView(R.id.xLayout)
    XLayout xLayout;
    @BindView(R.id.button_edit)
    SelectorButton buttonEdit;
    @BindView(R.id.tv_published_brief)
    TextView tvPublishedBrief;
    @BindView(R.id.et_published)
    EditText etPublished;
    private XTitle xTitle;
    private TextView rightAction;

    private Boolean isCreater;
    private String conversationID;

    @Override
    public int bindLayout() {
        return R.layout.activity_square_publish;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setMyTitle(R.string.im_str_square_notice, true);
        isCreater = getIntent().getBooleanExtra("is_creater", false);
        conversationID = getIntent().getStringExtra("conversation_id");
        getPublishInfo();
        initLayout();

        etPublished.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPublished.requestFocus();
                if (etPublished.getText().length() > 0) {
                    etPublished.setSelection(etPublished.getText().length());
                }
                AppDevice.showSoftKeyboard(etPublished);
                rightAction.setText("发布");
            }
        });
    }

    private void initLayout() {
        if (isCreater) {
            xLayout.setEmptyText("暂无群公告");
        } else {
            etPublished.setFocusable(false);
            etPublished.setEnabled(false);
            xLayout.setEmptyText("暂无群公告\n\r群公告适用于发布群规、群活动等信息");
        }
    }

    private void initTitle(final int type) {
        String text = "";
        if (type == 0x01) {
            text = "创建";
        } else if (type == 0x02) {
            text = "发布";
        } else if (type == 0x03) {
            text = "清空";
        }

        XTitle.TextAction textAction = new XTitle.TextAction(text) {
            @Override
            public void actionClick(View view) {
                if (((TextView) view).getText().toString().equals("创建")) {
                    xLayout.setStatus(XLayout.Success);
                    etPublished.requestFocus();
                } else if (((TextView) view).getText().toString().equals("发布")) {
                    publishNotice();
                } else if (((TextView) view).getText().toString().equals("清空")) {
                    etPublished.setText("");
                    etPublished.requestFocus();
                }
            }
        };
        xTitle.addAction(textAction, 0);
        rightAction = (TextView) xTitle.getViewByAction(textAction);
    }

    //拉取群公告
    public void getPublishInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        xLayout.setStatus(XLayout.Loading);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                Log.e("=====notice", responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject jsonObject = (JSONObject) ((JSONObject) result.get("data")).get("attr");
                    if (jsonObject.get("notice") != null) {
                        xLayout.setStatus(XLayout.Success);
                        etPublished.setText(jsonObject.getString("notice"));
                        if (isCreater) {
                            initTitle(0x02);
                        } else {
                            initTitle(0x01);
                        }
                    } else {
                        xLayout.setStatus(XLayout.Empty);
                        if (isCreater) {
                            initTitle(0x01);
                        } else {
                            initTitle(0x01);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, ggHttpInterface).startGet();
    }


    //发布公告
    public void publishNotice() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        params.put("group_notice", etPublished.getText().toString());
        xLayout.setStatus(XLayout.Loading);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    xLayout.setStatus(XLayout.Success);
                    finish();
                    UIHelper.toast(getActivity(), "发布成功");
                } else {
                    UIHelper.toast(getActivity(), "发布失败");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.UPDATE_GROUP_INFO, ggHttpInterface).startGet();
    }
}
