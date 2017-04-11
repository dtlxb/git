package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;


/**
 * Created by huangxx on 2017/4/6.
 */

public class EditSquareBriefActivity extends BaseActivity {

    @BindView(R.id.button_edit)
    SelectorButton buttonEdit;
    @BindView(R.id.tv_published_brief)
    TextView tvPublishedBrief;
    @BindView(R.id.et_group_info)
    EditText etGroupInfo;

    @BindView(R.id.noinfo_layout)
    RelativeLayout noInfoLayout;
    @BindView(R.id.text_count_layout)
    RelativeLayout textCountLayout;
    @BindView(R.id.tv_noinfo_tag)
    TextView tvNoInfo;
    @BindView(R.id.tv_text_count)
    TextView tvCount;
    @BindView(R.id.edit_layout)
    LinearLayout editLayout;
    @BindView(R.id.load_pb)
    ProgressBar loadPb;

    private XTitle xTitle;
    private TextView rightAction;

    private Boolean isCreater;
    private String conversationID;
    private Boolean isNotice;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_square_brief;
    }

    @Override
    public void doBusiness(Context mContext) {
        final InputMethodManager inputMethodManager = (InputMethodManager) EditSquareBriefActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        isCreater = getIntent().getBooleanExtra("is_creater", false);
        conversationID = getIntent().getStringExtra("conversation_id");
        isNotice = getIntent().getBooleanExtra("is_notice", false);
        if (isNotice) {
            xTitle = setMyTitle(R.string.im_str_square_notice, true);
            tvCount.setVisibility(View.GONE);
            etGroupInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        } else {
            xTitle = setMyTitle(R.string.im_str_square_brief, true);
            tvCount.setVisibility(View.VISIBLE);
            etGroupInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        }

        getGroupInfo();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etGroupInfo.requestFocus();
                if (etGroupInfo.getText().length() > 0) {
                    etGroupInfo.setSelection(etGroupInfo.getText().length());
                }
                inputMethodManager.showSoftInput(etGroupInfo, 0);
            }
        });
        etGroupInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLenth = etGroupInfo.getText().toString().length();
                if (textLenth > 50) {

                } else {
                    tvCount.setText("字数" + ":" + textLenth + "/" + 50);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void initTitle(final int type) {
        if (isCreater) {
            String text = "";
            if (type == 0x01) {
                text = "创建";
            } else if (type == 0x02) {
                text = "发布";
            } else if (type == 0x03) {
                text = "清空";
            }

            if (isNotice) {
                tvNoInfo.setText("暂无群公告\n群公告适用于发布群规、群活动等信息");
            } else {
                tvNoInfo.setText("暂无群简介\n群简介综合群性质，创建时间等信息");
            }

            XTitle.TextAction textAction = new XTitle.TextAction(text) {
                @Override
                public void actionClick(View view) {
                    if (((TextView) view).getText().toString().equals("创建")) {
                        noInfoLayout.setVisibility(View.GONE);
                        editLayout.setVisibility(View.VISIBLE);
                        rightAction.setText("发布");
                    } else if (((TextView) view).getText().toString().equals("发布")) {
                        publishInfo();
                        if (!etGroupInfo.getText().toString().equals("")) {
                            sendStockMessage(etGroupInfo.getText().toString());
                        }
                    } else if (((TextView) view).getText().toString().equals("清空")) {
                        etGroupInfo.setText("");
                    }
                }
            };
            xTitle.addAction(textAction, 0);
            rightAction = (TextView) xTitle.getViewByAction(textAction);
        } else {
            if (isNotice) {
                tvNoInfo.setText("暂无群公告");
            } else {
                tvNoInfo.setText("暂无群简介");
            }
            buttonEdit.setVisibility(View.GONE);
            etGroupInfo.clearFocus();
            etGroupInfo.setEnabled(false);
            textCountLayout.setVisibility(View.GONE);
        }
    }

    //发送群消息
    private void sendStockMessage(String messageText) {
        //股票消息(消息type:8,加上公告信息);

        //添加公告信息
        Map<String, String> lcattrsMap = new HashMap<>();
        lcattrsMap = AVImClientManager.getInstance().userBaseInfo();

        //消息基本信息
        Map<Object, Object> messageMap = new HashMap<>();
        messageMap.put("_lctype", "8");
        messageMap.put("_lctext", messageText);
        messageMap.put("_lcattrs", lcattrsMap);

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        params.put("chat_type", "1002");
        params.put("message", JSONObject.toJSONString(messageMap));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {

                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();
    }


    //拉取群公告
    public void getGroupInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                Log.e("=====notice", responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject jsonObject = (JSONObject) ((JSONObject) result.get("data")).get("attr");
                    if (null != jsonObject.get("intro") || null != jsonObject.get("notice")) {
                        loadPb.setVisibility(View.GONE);
                        noInfoLayout.setVisibility(View.GONE);
                        editLayout.setVisibility(View.VISIBLE);
                        if (isNotice) {
                            etGroupInfo.setText(jsonObject.getString("notice"));
                        } else {
                            etGroupInfo.setText(jsonObject.getString("intro"));
                        }
                        initTitle(0x02);
                    } else {
                        loadPb.setVisibility(View.GONE);
                        noInfoLayout.setVisibility(View.VISIBLE);
                        editLayout.setVisibility(View.GONE);
                        initTitle(0x01);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                noInfoLayout.setVisibility(View.VISIBLE);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, ggHttpInterface).startGet();
    }

    //发布公告
    public void publishInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationID);
        if (isNotice) {
            params.put("group_notice", etGroupInfo.getText().toString());
        } else {
            params.put("group_intro", etGroupInfo.getText().toString());
        }
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    if (isNotice) {
                        bundle.putString("group_notice", etGroupInfo.getText().toString());
                        intent.putExtras(bundle);
                        setResult(AppConst.SQUARE_ROOM_EDIT_NOTICE, intent);
                    } else {
                        bundle.putString("group_intro", etGroupInfo.getText().toString());
                        intent.putExtras(bundle);
                        setResult(AppConst.SQUARE_ROOM_EDIT_BRIEF, intent);
                    }
                    finish();
                } else {
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(params, GGOKHTTP.UPDATE_GROUP_INFO, ggHttpInterface).startGet();
    }
}
