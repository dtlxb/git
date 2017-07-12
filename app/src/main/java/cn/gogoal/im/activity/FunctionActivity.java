package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.PdfData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.WebViewUtil;
import cn.gogoal.im.common.linkUtils.PlayDataStatistics;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.dialog.AdvisersDialog;
import cn.gogoal.im.ui.view.XTitle;

/*
* web页面
* */
public class FunctionActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    XTitle title_bar;

    @BindView(R.id.webView)
    BridgeWebView webView;

    private String title;
    private boolean needShare;
    private XTitle xTitle;

    private ImageView ivMessageTag;
    //消息
    private BadgeView badge;
    private int unReadCount;

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public void onResume() {
        super.onResume();
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    //    d4d0f74e3c87483cb3bf91dd10dc53f8
    @Override
    public void doBusiness(final Context mContext) {

        //接收传值
        title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("function_url");
        KLog.e(url);
        needShare = getIntent().getBooleanExtra("need_share", false);

        //初始化标题
        if (!StringUtils.isActuallyEmpty(title)) {
            xTitle = setMyTitle(title, true);
        } else {
            xTitle = setMyTitle("", true);
        }
        xTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        XTitle.ImageAction messageAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.message_dark)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(), MessageHolderActivity.class));
            }
        };

        if (needShare) {
            XTitle.ImageAction shareAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.img_share)) {
                @Override
                public void actionClick(View view) {
                    webView.callHandler("shareWeb", "", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            KLog.e(value);

//                            if (StringUtils.isActuallyEmpty(value)) {//TODO 目前没有分享，肯定是空咯
//                                GGShareEntity entity = new GGShareEntity();
//                                entity.setDesc("测试数据测试数据测试数据测试数据测试数据测试数据测试数据");
//                                entity.setIcon("http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_C317F15BB2B3AA91.jpg");
//                                entity.setTitle("测试数据-标题");
//
//                                value = JSONObject.toJSONString(entity);
////
////                                if ()
//                            }

                            if (StringUtils.isActuallyEmpty(value)) {
                                UIHelper.toast(getContext(), "暂无分享信息");
                                return;
                            }

                            GGShareEntity shareEntity = JSONObject.parseObject(value, GGShareEntity.class);
                            shareEntity.setShareType(GGShareEntity.SHARE_TYPE_WEB);
                            if (StringUtils.isActuallyEmpty(shareEntity.getLink())) {
                                shareEntity.setLink(webView.getUrl());
                            }
                            Intent intent = new Intent(getContext(), ShareMessageActivity.class);
                            intent.putExtra("share_web_data", shareEntity);
                            startActivity(intent);
                        }
                    });
                }
            };
            xTitle.addAction(shareAction, 0);
            xTitle.addAction(messageAction, 1);
        } else {
            xTitle.addAction(messageAction);
        }

        ivMessageTag = (ImageView) xTitle.getViewByAction(messageAction);
        badge = new BadgeView(getActivity());
        initBadge(unReadCount, badge);

        initWebView(webView);

        if (!StringUtils.isActuallyEmpty(url)) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("file:///android_asset/demo.html");
            title_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.callHandler("methodFromJs", "java调用js方法:这是java转给js参数", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            KLog.e(value);
                        }
                    });
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

//        // H5页面跳转时获取页面title和url
        webView.setOnWebChangeListener(new BridgeWebView.WebChangeListener() {
            @Override
            public void getWebInfo(String url, String title) {
//                KLog.e("url==="+url + ";title==="+title);
            }
        });

        //1.添加原生方法，测试弹窗
        webView.registerHandler("naviveDialog", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this, data).show();
            }
        });

        //1.添加原生方法，带回调
        webView.registerHandler("naviveDialogWithCallBack", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this, data)
                        .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                function.onReceiveValue("原生点击了[是]");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                function.onReceiveValue("原生点击了[否]");
                            }
                        })
                        .show();
            }
        });

        //添加Go_Goal直播中的页面跳转
        webView.registerHandler("JumpPlayerDetial", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {

                JSONObject object = JSONObject.parseObject(data);
                if (object.getString("source").equals("live")) {
                    PlayDataStatistics.enterLiveAuthorize(false, getContext(), object.getString("live_id"), false);
                } else if (object.getString("source").equals("video")) {
                    Intent intent = new Intent(getContext(), PlayerActivity.class);
                    intent.putExtra("live_id", object.getString("live_id"));
                    startActivity(intent);
                }


            }
        });

        /*pdf阅读*/
        webView.registerHandler("loadPdfFromWeb", new BridgeHandler() {
            @Override
            public void handler(final String data, ValueCallback<String> function) {

                FileUtil.writeRequestResponse(data, "pdf");

                showPdf(data.replaceAll("\\s", ""));

            }
        });

        //添加自选股
        webView.registerHandler("addMyStock", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                KLog.e("添加", data);
                JSONObject stockObject = JSONObject.parseObject(data);
                StockUtils.addMyStock(stockObject.getString("stock_code"), new Impl<Boolean>() {
                    @Override
                    public void response(int code, Boolean data) {
                        UIHelper.toast(getContext(), data ? "添加成功" : "添加失败");
                    }
                });
            }
        });

        //移除自选股
        webView.registerHandler("deleteMyStock", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                KLog.e("移除", data);
                JSONObject stockObject = JSONObject.parseObject(data);
                StockUtils.deleteMyStockOld(stockObject.getString("stock_code"), new Impl<String>() {
                    @Override
                    public void response(int code, String data) {
                        UIHelper.toast(getContext(), "删除自选股" +
                                (code == Impl.RESPON_DATA_SUCCESS ? "成功" : "失败"));
                    }
                });
            }
        });

        //是否是我的自选股
        webView.registerHandler("makeEnquiries", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                KLog.e("查询", data);
                String stockCode = JSONObject.parseObject(data).getString("stock_code");
                function.onReceiveValue(StockUtils.isMyStock(stockCode) ? "0" : "1");

                KLog.e(StockUtils.isMyStock(stockCode) ? "0" : "1");
            }
        });

        //添加发起直播填写信息中的页面跳转
        webView.registerHandler("applyComplete", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                JSONObject object = JSONObject.parseObject(data);

                PlayDataStatistics.enterLiveAuthorize(true, getContext(), object.getString("live_id"), true);
            }
        });

        //原生添加弹窗
        webView.registerHandler("showJSAlert", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                DialogHelp.getMessageDialog(getContext(), data).show();
            }
        });

        //投资顾问弹窗
        webView.registerHandler("exclusiveConsultant", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                new AdvisersDialog().show(getSupportFragmentManager());
            }
        });
    }

    private void showPdf(String data) {
        if (!StringUtils.isActuallyEmpty(data)) {
            PdfData pdfData = JSONObject.parseObject(data, PdfData.class);
            NormalIntentUtils.go2PdfDisplayActivity(
                    getActivity(),
                    pdfData.getPdfUrl(),
                    pdfData.getTitle());
        }
    }

    private FunctionActivity getContext() {
        return FunctionActivity.this;
    }

    private void initWebView(BridgeWebView mWebView) {
        mWebView.addJavascriptInterface(new WebViewUtil(this, this), "interaction");

        WebSettings settings = mWebView.getSettings();
        mWebView.setDefaultHandler(new DefaultHandler());

        settings.setBuiltInZoomControls(false);

        String ua = mWebView.getSettings().getUserAgentString() + " GoGoaler/" + AppDevice.getAppVersionName(getContext());
        mWebView.getSettings().setUserAgentString(ua);

        // 开启DOM缓存。
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
    }

    private void initBadge(int num, BadgeView badge) {
        badge.setGravityOffset(10, 7, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(num);
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        AVIMConversation conversation = (AVIMConversation) map.get("conversation");
        //获取免打扰
        List<String> muList = (List<String>) conversation.get("mu");
        boolean noBother = muList.contains(UserUtils.getMyAccountId());
        int chatType = (int) conversation.getAttribute("chat_type");
        if (!noBother && chatType != AppConst.IM_CHAT_TYPE_STOCK_SQUARE) {
            unReadCount++;
        }
        badge.setBadgeNumber(unReadCount);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            // 返回上一页面
//            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
