package cn.gogoal.im.activity.copy;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.WebViewUtil;
import hply.com.niugu.view.GoGoalWebView;

/**
 * 新闻详情页
 * Created by lixs on 2015/10/8.
 */
public class NewsContentActivity extends BaseActivity implements View.OnClickListener {

  @BindView(R.id.webview)
  GoGoalWebView webView;

  private Activity mActivity = this;

  @BindView(R.id.news_content_reply)
  LinearLayout news_content_reply;

  @BindView(R.id.comments_btn)
  FrameLayout commentsBtn;

  @BindView(R.id.content_love_layout)
  RelativeLayout favorites;

  @BindView(R.id.thumb_up)
  RelativeLayout thumb_up;

  @BindView(R.id.Favorites)
  Button loveBtn;

  @BindView(R.id.content_share)
  RelativeLayout share;
  @BindView(R.id.sharebtn)
  Button sharebtn;

  @BindView(R.id.btnBack)
  LinearLayout btnBack;

  @BindView(R.id.favor_count)
  TextView favor_count;
  @BindView(R.id.praise_count)
  TextView praise_count;
  @BindView(R.id.share_count)
  TextView share_count;

  @BindView(R.id.reply_count)
  TextView reply_count;

  @BindView(R.id.praise_count_animation)
  TextView praise_count_animation;
  //用户头像
  @BindView(R.id.user_head_iv)
  ImageView user_head_iv;

  private Context context = this;
  private Activity activity = this;

  private String newsid;
  private String newstype;
  private String newstitle;

  private String url;

  private boolean isFavor = false;
  private String favor_type = null;

  private String specil_reply_type = null;

  private String text;

  private Integer favor_sum;
  private Integer praise_sum;
  private Integer share_sum;

  private Animation praise_animal;

  @Override
  public int bindLayout() {
    return R.layout.activity_newscontent;
  }

  @Override
  public void doBusiness(Context mContext) {
    init();
    renderFavor();
    registeReceiver();
  }

  private void init() {

    praise_animal = AnimationUtils.loadAnimation(this, R.anim.applaud_animation);

    if (SPTools.getString("wenben", null) != null) {
      SPTools.clearItem("wenben");
    }
    Intent intent = getIntent();
    newsid = intent.getStringExtra("id");
    newstype = intent.getStringExtra("type");
    newstitle = intent.getStringExtra("newstitle");
    favor_type = intent.getStringExtra("favor_type");

    favor_sum = intent.getIntExtra("favor_count", 0);
    praise_sum = intent.getIntExtra("praise_count", 0);
    share_sum = intent.getIntExtra("share_count", 0);

    favor_count.setText(favor_sum.toString());
    praise_count.setText(praise_sum.toString());
    share_count.setText(share_sum.toString());

    specil_reply_type = intent.getStringExtra("specil_type");
    url = AppConst.WEB_DAMIN + "#/news/detail?id=" + newsid + "&type=" + newstype;
//    Log.e("TAG","==="+url+"===");
    webView.addJavascriptInterface(new WebViewUtil(this, this), "contact");
    webView.setDownloadListener(new MyWebViewDownLoadListener());
    webView.loadUrl(url);
    WebSettings settings = webView.getSettings();
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    settings.setBuiltInZoomControls(true);
    settings.setSupportZoom(true);

    StatusBarUtil util = StatusBarUtil.with(getActivity());
    if (util.isMiUIV6()) {
      util.setColor(getResColor(R.color.white));
    } else {
      util.setColor(getResColor(R.color.black));
    }
    commentsBtn.setOnClickListener(this);
    news_content_reply.setOnClickListener(this);
    btnBack.setOnClickListener(this);
    share.setOnClickListener(this);
    sharebtn.setOnClickListener(this);
    favorites.setOnClickListener(this);
    thumb_up.setOnClickListener(this);
    loveBtn.setTag(R.drawable.content_love);
  }

  private void reply() {
//    if (UserUtils.isLogin()) {
//      showCustomDialog();
//    } else {
//      startActivity(new Intent(context, WechartLoginActivity.class));
//    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.comments_btn:
        gotocomment(0);
        break;
      case R.id.news_content_reply:
        reply();
        break;
      case R.id.btnBack:
        if (!AppDevice.isNetworkConnected(mActivity)) {
          finish();
        } else if (webView.canGoBack()) {
          webView.goBack();
        } else {
          finish();
        }
        break;
      case R.id.sharebtn:
        cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(sharebtn, -1);
        shareAction();
        break;
      case R.id.content_love_layout:
        if (UserUtils.isLogin()) {
          if (!AppDevice.isFastClick()) operateFavor();
        } else {
//          UIHelper.showWXLogin(activity);
        }
        break;
      case R.id.thumb_up:
        cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(sharebtn, -1);
        addPraise();
        break;
    }
  }

  private void addPraise() {
    praise_count_animation.setVisibility(View.VISIBLE);
    praise_animal.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        
      }
      @Override
      public void onAnimationEnd(Animation animation) {

        praise_count_animation.setVisibility(View.GONE);

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });

    praise_count_animation.startAnimation(praise_animal);


    praise_sum++;
    praise_count.setText(String.valueOf(praise_sum));

    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String responseInfo) {
      }

      @Override
      public void onFailure(String msg) {
      }
    };
    Map<String, String> params = new HashMap<String, String>();
    if (UserUtils.isLogin()) {
      params.put("token", UserUtils.getToken());
    }
    ;
    params.put("target_id", newsid);
    params.put("type", getNewsType());
    new GGOKHTTP(params, GGOKHTTP.PRAISE_ADD, ggHttpInterface).startGet();
  }

  private void shareAction() {
//    UIHelper.showShareBox(this, R.id.news_content, url, newstitle, ConstantUtils.SHARE_CONTENT1, null);
//    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//      @Override
//      public void onSuccess(String responseInfo) {
//        share_sum++;
//        share_count.setText(String.valueOf(share_sum));
//      }
//
//      @Override
//      public void onFailure(String msg) {
//      }
//    };
//    Map<String, String> params = new HashMap<String, String>();
//    params.put("type", getNewsType());
//    params.put("platform", "1");
//    params.put("target_id", newsid);
//    new GGOKHTTP(params, GGOKHTTP.SHARE_ADD, ggHttpInterface).startGet();
  }

  private void gotocomment(int num) {
    Intent intent = new Intent(context, CommentActivity.class);
    intent.putExtra("id", newsid);
    intent.putExtra("type", getNewsType());
    intent.putExtra("newstitle", newstitle);
    intent.putExtra("url", url);
    intent.putExtra("replycount", Integer.parseInt(reply_count.getText().toString()) + num);
    intent.putExtra("favor_count", favor_sum);
    intent.putExtra("praise_count", praise_sum);
    intent.putExtra("share_count", share_sum);
    startActivity(intent);
  }

  private void getReplyCount() {
    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String responseInfo) {
        JSONObject result = JSONObject.parseObject(responseInfo);
        int code = result.getInteger("code");
        if (code == 0) {
          com.alibaba.fastjson.JSONArray data = result.getJSONArray("data");
          reply_count.setText(((JSONObject) data.get(0)).getString("count"));
        } else {
          reply_count.setText("0");
        }
      }

      @Override
      public void onFailure(String msg) {
        reply_count.setText("0");
      }
    };
    Map<String, String> params = new HashMap<String, String>();
    params.put("theme_id", newsid);
    params.put("type", getNewsType());
    params.put("get_count", "1");
    new GGOKHTTP(params, GGOKHTTP.COMMENT_LIST, ggHttpInterface).startGet();
  }

  private void renderFavor() {
    if (!UserUtils.isLogin()) return;
    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String responseInfo) {
        JSONObject result = JSONObject.parseObject(responseInfo);
        int code = result.getInteger("code");
        if (code == 0) {
          if (result.getJSONObject("data").getBoolean("result")) {
            loveBtn.setBackgroundResource(R.drawable.news_content_favorites_already);
            isFavor = true;
            if ("0".equals(favor_count.getText().toString())) {
              favor_count.setText("1");
            }
          } else {
            loveBtn.setBackgroundResource(R.drawable.content_love);
            isFavor = false;
          }
        }
      }

      @Override
      public void onFailure(String msg) {
      }
    };

    new GGOKHTTP(getFavorParams(), GGOKHTTP.FAVOR_IS_FAVORED, ggHttpInterface).startGet();
  }

  private void operateFavor() {
    if (isFavor) deletFavor();
    else addFavor();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getReplyCount();
    isShowHeard();
    webView.loadUrl("javascript:angular._app.GetNewComments();");
    StatusBarUtil.with(this).setStatusBarFontDark(true);
  }

  private void registeReceiver() {
    registerReceiver(receiver, new IntentFilter("news_content_reply_action"));
  }

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      int message = intent.getIntExtra("message", 0);
      if (message == 1) {
        reply();
      }
    }
  };

  @Override
  protected void onPause() {
    super.onPause();
    webView.loadUrl("javascript:angular._app.Quit();");
  }

  protected void showCustomDialog() {
    final Dialog dialog = new Dialog(NewsContentActivity.this);

    Window win = dialog.getWindow();
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    params.gravity = Gravity.BOTTOM;

    win.setAttributes(params);

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    dialog.setContentView(R.layout.reply_dialog);

    final EditText editText = (EditText) dialog.findViewById(R.id.editText1);

    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        closeSoftKeyBoard(editText);
      }
    });

    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        closeSoftKeyBoard(editText);
      }
    });

    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
      }
    });
    //再打开
    editText.setText(SPTools.getString("wenben", null));

    editText.setFocusable(true);
    editText.setFocusableInTouchMode(true);
    editText.requestFocus();

    TextView button = (TextView) dialog.findViewById(R.id.reply_publish);
    button.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        if (SPTools.getString("wenben", null) != null) {
          SPTools.clearItem("wenben");
        }
        text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
          UIHelper.toast(context, "评论内容不能为空");
        } else {
          GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
              UIHelper.toast(context, "评论成功!");
              gotocomment(1);
            }

            @Override
            public void onFailure(String msg) {
              UIHelper.toast(context, "评论失败!请检查网络设置后重试");
            }
          };
          new GGOKHTTP(getparams(text), GGOKHTTP.COMMENT_ADD, ggHttpInterface).startPost();
        }
        dialog.dismiss();
      }
    });
    dialog.show();

    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.width = AppDevice.getWidth(activity);
    dialog.getWindow().setAttributes(lp);
  }

  private Map<String, String> getparams(String text) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("theme_id", newsid);
    params.put("type", getNewsType());
    params.put("content", text);
    params.put("token", UserUtils.getToken());
    return params;
  }


  private String getNewsType() {
    if ("100".equals(newstype)) return "1";
    if ("101".equals(newstype)) return "2";
    if ("109".equals(newstype)) return "6";
    if ("102".equals(newstype)) return "5";
    if ("103".equals(newstype)) return "4";
    if ("104".equals(newstype)) return "3";
    if ("106".equals(newstype)) return "13";
    if ("107".equals(newstype)) return "12";
    if ("108".equals(newstype) || "105".equals(newstype)) return "8";
    return null;
  }

  private void closeSoftKeyBoard(EditText editText) {
    text = editText.getText().toString();
    SPTools.saveString("wenben", text);

    editText.clearFocus();
    editText.setFocusable(false);
    InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
    if (null != manager && null != this.getCurrentFocus())
      manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  private void deletFavor() {
    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String responseInfo) {
        JSONObject result = JSONObject.parseObject(responseInfo);
        int code = result.getInteger("code");
        if (code == 0) {

          cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(loveBtn, R.drawable.content_love);

          isFavor = false;
          UIHelper.toast(context, "删除收藏成功");
          Integer favorSum = Integer.parseInt(favor_count.getText().toString());
          if (favorSum > 0) {
            favorSum--;
            favor_count.setText(favorSum.toString());
          }
        }
      }

      @Override
      public void onFailure(String msg) {
        UIHelper.toast(context, "删除收藏成功");
      }
    };

    new GGOKHTTP(getFavorParams(), GGOKHTTP.FAVOR_DELETE, ggHttpInterface).startGet();
  }

  private void addFavor() {
    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String responseInfo) {
        JSONObject result = JSONObject.parseObject(responseInfo);
        int code = result.getInteger("code");
        if (code == 1001) {
          UIHelper.toast(context, "收藏成功");
          cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(loveBtn, R.drawable.news_content_favorites_already);
          isFavor = true;
          Integer favorSum = Integer.parseInt(favor_count.getText().toString());
          favorSum++;
          favor_count.setText(favorSum.toString());
        } else {
          loveBtn.setBackgroundResource(R.drawable.content_love);
          UIHelper.toast(context, "收藏失败");
        }
      }

      @Override
      public void onFailure(String msg) {
        loveBtn.setBackgroundResource(R.drawable.content_love);
        UIHelper.toast(context, "收藏失败");
      }
    };
    new GGOKHTTP(getFavorParams(), GGOKHTTP.FAVOR_ADD, ggHttpInterface).startGet();
  }

  private Map<String, String> getFavorParams() {
    Map<String, String> params = new HashMap<String, String>();
    params.put("target_id", newsid);
    if (!TextUtils.isEmpty(favor_type)) {
      params.put("type", favor_type);
    } else {
      params.put("type", getNewsType());
    }
    params.put("token", UserUtils.getToken());
    return params;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (!AppDevice.isNetworkConnected(mActivity)) {
      finish();
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
      webView.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  private void isShowHeard() {
    if (!UserUtils.isLogin()) {
      user_head_iv.setVisibility(View.GONE);
    } else {
      user_head_iv.setVisibility(View.VISIBLE);
      ImageDisplay.loadCircleNetImage(getActivity(),UserUtils.getUserAvatar(), user_head_iv);
    }
  }

  private class MyWebViewDownLoadListener implements DownloadListener {

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
      Uri uri = Uri.parse(url);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      startActivity(intent);
    }
  }
}