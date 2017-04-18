package cn.gogoal.im.activity.copy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.adapter.copy.CommentListAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import hply.com.niugu.bean.CommentAdd_bean;
import hply.com.niugu.bean.CommentAdd_data;
import hply.com.niugu.bean.CommentBean;
import hply.com.niugu.bean.CommentData;
import hply.com.niugu.bean.CommentData_Sons;
import hply.com.niugu.view.GGListView;


/**
 * 评论显示区
 * Created by lixs on 2015/10/8.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener {
  //返回
  @BindView(R.id.btnBack)
  LinearLayout btnBack;
  //收藏
  @BindView(R.id.content_love_layout)
  RelativeLayout Favorites;
  @BindView(R.id.Favorites)
  Button loveBtn;
  //点赞
  @BindView(R.id.thumb_up)
  RelativeLayout thumb_up;
  //分享
  @BindView(R.id.content_share)
  RelativeLayout share;
  //收藏数
  @BindView(R.id.favor_count)
  TextView favor_count;
  @BindView(R.id.praise_count)
  TextView praise_count;
  @BindView(R.id.share_count)
  TextView share_count;
  //列表
  @BindView(R.id.comment_list)
  GGListView comment_list;
  //评论
  @BindView(R.id.news_content_reply)
  LinearLayout news_content_reply;
  //评论数
  @BindView(R.id.reply_count)
  TextView reply_count;
  //无数据时
  @BindView(R.id.stock_no_data)
  LinearLayout stock_no_data;
  //页面加载动画
  @BindView(R.id.load_animation)
  RelativeLayout load_animation;

  @BindView(R.id.praise_count_animation)
  TextView praise_count_animation;
  //用户头像
  @BindView(R.id.user_head_iv)
  ImageView user_head_iv;

  private Animation praise_animal;

  private String news_id;
  private String type;
  private String text;

  private ArrayList<CommentData> list = new ArrayList<CommentData>();
  private CommentListAdapter adapter;

  private String useName;
  private String list_id;
  private String account_id;
  private String comment;
  private String newstitle;
  private int replycount;
  private String url;
  private ArrayList<CommentData_Sons> sons;
  private int page = 1;
  private boolean isFavor = false;

  private Integer favor_sum;
  private Integer praise_sum;
  private Integer share_sum;
  private StatusBarUtil barUtil;

  @Override
  public int bindLayout() {
    return R.layout.activity_comments;
  }

  @Override
  public void doBusiness(Context mContext) {
    barUtil = StatusBarUtil.with(getActivity());

    init();
    renderFavor();
    getComment(page, news_id, type);
  }

  //初始化数据
  private void init() {
    praise_animal = AnimationUtils.loadAnimation(this, R.anim.applaud_animation);

    if (SPTools.getString("wenben", null) != null) {
      SPTools.clearItem("wenben");
    }
    news_id = getIntent().getStringExtra("id");
    type = getIntent().getStringExtra("type");
    newstitle = getIntent().getStringExtra("newstitle");
    replycount = getIntent().getIntExtra("replycount", 0);
    url = getIntent().getStringExtra("url");

    reply_count.setText(String.valueOf(replycount));

    favor_sum = getIntent().getIntExtra("favor_count", 0);
    praise_sum = getIntent().getIntExtra("praise_count", 0);
    share_sum = getIntent().getIntExtra("share_count", 0);

    favor_count.setText(favor_sum.toString());
    praise_count.setText(praise_sum.toString());
    share_count.setText(share_sum.toString());

    btnBack.setOnClickListener(this);
    //收藏
    Favorites.setOnClickListener(this);
    //点赞
    thumb_up.setOnClickListener(this);
    //分享
    share.setOnClickListener(this);
    //评论框
    news_content_reply.setOnClickListener(this);

    adapter = new CommentListAdapter(list);
    comment_list.setAdapter(adapter);
    //点击监听
    comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //获得用户名
        useName = list.get(position).getUsername();
        list_id = list.get(position).getId();
        account_id = list.get(position).getAccount_id();
        comment = list.get(position).getContent();
        sons = list.get(position).getSons();

        View view1 = getLayoutInflater().inflate(R.layout.view_dialog, null);
        view1.setAlpha(1);
        final AlertDialog alertDialog = new AlertDialog.Builder(CommentActivity.this).setCancelable(false).create();
        alertDialog.setView(view1, 0, 0, 0, 0);
        alertDialog.setCanceledOnTouchOutside(true);
        Button button1 = (Button) view1.findViewById(R.id.main_button1);
        Button button2 = (Button) view1.findViewById(R.id.main_button2);
        Button button3 = (Button) view1.findViewById(R.id.main_button3);

        if (!UserUtils.isLogin()) {
          button3.setVisibility(View.GONE);
        } else {
          if (account_id != null && account_id.equals(UserUtils.getToken())) {
            button3.setVisibility(View.VISIBLE);
          } else {
            button3.setVisibility(View.GONE);
          }
        }
        alertDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        final WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        alertDialog.getWindow().setAttributes(lp);

        button1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            alertDialog.dismiss();
            if (UserUtils.isLogin()) {
              showCustomDialog(null, true, position);
            } else {
              return;
//              startActivity(new Intent(CommentActivity.this, WechartLoginActivity.class));
            }
          }
        });
        button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ClipboardManager clip = (ClipboardManager) CommentActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(comment);
            UIHelper.toast(CommentActivity.this, "复制成功");

            alertDialog.dismiss();
          }
        });
        button3.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            if (UserUtils.isLogin()) {
              final Map<String, String> param = new HashMap<String, String>();
              param.put("token", UserUtils.getToken());
              param.put("id", list_id);
              GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {

                @Override
                public void onSuccess(String respornseInfo) {
                  UIHelper.toast(CommentActivity.this, "删除成功!");
                  alertDialog.dismiss();
                  list.remove(position);
                  adapter.notifyDataSetChanged();

                  int number = Integer.parseInt(reply_count.getText().toString());
                  if (sons != null && sons.size() == 0) {
                    number--;
                  } else if (sons != null) {
                    number = number - sons.size() - 1;
                  } else {
                    number--;
                  }
                  reply_count.setText(String.valueOf(number));
                }

                @Override
                public void onFailure(String msg) {
                  UIHelper.toast(CommentActivity.this, "删除失败!请检查网络设置后重试");
                }
              };
              new GGOKHTTP(param, GGOKHTTP.COMMENT_DELETE, ggHttpInterface).startGet();
            } else {
              startActivity(new Intent(CommentActivity.this, TypeLoginActivity.class));
            }
          }
        });
      }
    });

    //上拉加载
    comment_list.setPullBottomListener(new GGListView.PullBottomListener() {
      @Override
      public void Run() {
        if (page < 500) {
          page++;
          getComment(page, news_id, type);
        } else {
          comment_list.loadMoreComplate();
        }
      }
    });

    if (barUtil.isMiUIV6()) {
      setStatusColor(R.color.white);
    } else {
      setStatusColor(R.color.black);
    }
  }

  private void setStatusColor(int white) {
      barUtil.setColor(white);
  }

  @Subscriber(tag = "CommentActivity")
  private void refresh(String s) {
    String[] number=s.split(",");
    list.get(Integer.parseInt(number[1])).setPraise_sum(Integer.parseInt(number[0]));
    adapter.notifyDataSetChanged();
  }

  //加载评论数据
  private void getComment(final int page, String id, final String type) {

    final Map<String, String> param = new HashMap<String, String>();
    param.put("theme_id", id);
    param.put("type", type);
    param.put("get_father_son", "1");
    param.put("get_count", "0");
    param.put("page", page + "");
    param.put("rows", "10");
    GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
      @Override
      public void onSuccess(String respornseInfo) {
        if (respornseInfo != null) {
          CommentBean bean = JSONObject.parseObject(respornseInfo, CommentBean.class);
          if (bean.getCode().equals("0")) {
            comment_list.setVisibility(View.VISIBLE);
            stock_no_data.setVisibility(View.GONE);
            ArrayList<CommentData> info = bean.getData();
            list.addAll(info);
            adapter.notifyDataSetChanged();
          } else if (bean.getCode().equals("1001")) {
            if (page != 1) {
              UIHelper.toast(getApplicationContext(), R.string.nomoredata_hint);
            } else {
              comment_list.setVisibility(View.GONE);
              stock_no_data.setVisibility(View.VISIBLE);
            }
          }
        }
        load_animation.setVisibility(View.GONE);
        comment_list.loadMoreComplate();
      }

      @Override
      public void onFailure(String msg) {
        comment_list.loadMoreComplate();
        load_animation.setVisibility(View.GONE);
        UIHelper.toast(getApplicationContext(), R.string.net_erro_hint);
      }
    };
    new GGOKHTTP(param, GGOKHTTP.COMMENT_LIST, ggHttpInterface).startGet();
  }

  //显示评论框
  protected void showCustomDialog(String useName, final Boolean isTwoReply, final int position) {
    final Dialog dialog = new Dialog(CommentActivity.this);
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

    if (isTwoReply) {
      editText.setHint("回复:");
    } else {
      editText.setHint("请输入评论");
    }
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
          UIHelper.toast(CommentActivity.this, "回复内容不能为空");
        } else {
          if (isTwoReply) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("author_id", UserUtils.getUserId());
            params.put("theme_id", news_id);
            params.put("type", type);
            params.put("content", text);
            params.put("token", UserUtils.getToken());
            params.put("comment_id", list_id);
            GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
              @Override
              public void onSuccess(String responseInfo) {
                UIHelper.toast(CommentActivity.this, "回复成功!");

                CommentData one_data = list.get(position);

                SimpleDateFormat dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CommentData_Sons sons = new CommentData_Sons();
                sons.setAccount_id(UserUtils.getUserId());
                sons.setAuthor_id(null);
                sons.setAvoter(UserUtils.getUserAvatar());
                sons.setContent(text);
                sons.setDate(dateStr.format(new Date()));
                sons.setId("0");
                sons.setParent_id(one_data.getParent_id());
                sons.setPraise_sum(one_data.getPraise_sum());
                sons.setUsername(UserUtils.getUserName());

                ArrayList<CommentData_Sons> al_sons = one_data.getSons();
                if (al_sons == null) {
                  al_sons = new ArrayList<CommentData_Sons>();
                }
                al_sons.add(0, sons);
                list.get(position).setSons(al_sons);
                adapter.notifyDataSetChanged();

                int num = Integer.parseInt(reply_count.getText().toString());
                num++;
                reply_count.setText(String.valueOf(num));
                SPTools.clearItem("wenben");
                editText.setText(null);
              }

              @Override
              public void onFailure(String msg) {
                UIHelper.toast(CommentActivity.this, "回复失败!请检查网络设置后重试");
              }
            };
            new GGOKHTTP(params, GGOKHTTP.COMMENT_ADD, ggHttpInterface).startPost();
          } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("token", UserUtils.getToken());
            params.put("theme_id", news_id);
            params.put("type", type);
            params.put("content", text);
            GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
              @Override
              public void onSuccess(String responseInfo) {
                CommentAdd_bean bean = JSONObject.parseObject(responseInfo, CommentAdd_bean.class);
                if (bean.getCode().equals("0")) {
                  CommentAdd_data info = bean.getData();
                  SimpleDateFormat dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  CommentData addData = new CommentData();
                  addData.setAccount_id(UserUtils.getUserId());
                  addData.setAuthor_id(null);
                  addData.setAvoter(UserUtils.getUserAvatar());
                  addData.setContent(text);
                  String dd = dateStr.format(new Date());
                  addData.setDate(dd);
                  addData.setId(info.getId());
                  addData.setParent_id(0);
                  addData.setPraise_sum(0);
                  addData.setSons(null);

                  addData.setUsername(UserUtils.getUserName());
                  list.add(0, addData);

                  adapter.notifyDataSetChanged();
                  UIHelper.toast(CommentActivity.this, "评论成功!");
                  int num = Integer.parseInt(reply_count.getText().toString());
                  num++;
                  reply_count.setText(String.valueOf(num));
                  comment_list.setVisibility(View.VISIBLE);
                  stock_no_data.setVisibility(View.GONE);
                  SPTools.clearItem("wenben");
                  editText.setText(null);
                }
              }

              @Override
              public void onFailure(String msg) {
                Log.i("OIO","msg:" + msg);
                UIHelper.toast(CommentActivity.this, "评论失败!请检查网络设置后重试");
              }
            };
            new GGOKHTTP(params, GGOKHTTP.COMMENT_ADD, ggHttpInterface).startPost();
          }
        }

        dialog.dismiss();
      }
    });
    dialog.show();
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.width = AppDevice.getWidth(this);
    dialog.getWindow().setAttributes(lp);

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

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnBack:
        finish();
        break;
      case R.id.content_love_layout:
        if (UserUtils.isLogin()) {
          if (!AppDevice.isFastClick()) operateFavor();
        } else {
          startActivity(new Intent(getActivity(),TypeLoginActivity.class));
        }
        break;
      case R.id.thumb_up:
        cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(thumb_up, -1);
        addPraise();
        break;
      case R.id.content_share:
        cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(share, -1);
        shareAction();
        break;
      case R.id.news_content_reply:
        if (UserUtils.isLogin()) {
          showCustomDialog(null, false, -1001);
        } else {
          startActivity(new Intent(CommentActivity.this, TypeLoginActivity.class));
        }
        break;
    }
  }

  private void shareAction() {
//    UIHelper.showShareBox(this, R.id.comment_Linear, url, newstitle, AppConst.SHARE_CONTENT1, null);
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
//    params.put("type", type);
//    params.put("platform", "1");
//    params.put("target_id", news_id);
//    new GGOKHTTP(params, GGOKHTTP.SHARE_ADD, ggHttpInterface).startGet();
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
    params.put("target_id", news_id);
    params.put("type", type);
    new GGOKHTTP(params, GGOKHTTP.PRAISE_ADD, ggHttpInterface).startGet();
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

  //收藏
  private void operateFavor() {
    if (isFavor) deletFavor();
    else addFavor();
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
          if (favor_sum > 0) favor_sum--;
          favor_count.setText(favor_sum.toString());
          UIHelper.toast(CommentActivity.this, "删除收藏成功");
        }
      }

      @Override
      public void onFailure(String msg) {
        UIHelper.toast(CommentActivity.this, "删除收藏成功");
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
          cn.gogoal.im.common.AnimationUtils.getInstance().scaleBigAndSmall(loveBtn, R.drawable.news_content_favorites_already);
          UIHelper.toast(CommentActivity.this, "收藏成功");
          isFavor = true;
          favor_sum++;
          favor_count.setText(favor_sum.toString());
        } else {
          loveBtn.setBackgroundResource(R.drawable.content_love);
          UIHelper.toast(CommentActivity.this, "收藏失败");
        }
      }

      @Override
      public void onFailure(String msg) {
        loveBtn.setBackgroundResource(R.drawable.content_love);
        UIHelper.toast(CommentActivity.this, "收藏失败");
      }
    };
    new GGOKHTTP(getFavorParams(), GGOKHTTP.FAVOR_ADD, ggHttpInterface).startGet();
  }

  private Map<String, String> getFavorParams() {
    Map<String, String> params = new HashMap<String, String>();
    params.put("target_id", news_id);
    params.put("type", type);
    params.put("token", UserUtils.getToken());
    return params;
  }

  @Override
  protected void onResume() {
    super.onResume();
    isShowHeard();
    barUtil.initForGogoal(true);
  }

  private void isShowHeard() {
    if (!UserUtils.isLogin()) {
      user_head_iv.setVisibility(View.GONE);
    } else {
      user_head_iv.setVisibility(View.VISIBLE);
      ImageDisplay.loadCircleNetImage(getActivity(),UserUtils.getUserAvatar(), user_head_iv);
    }
  }
}