package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.activity.ToolsSettingActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.StockDetail2Text;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.stock.CompanyFinanceFragment;
import cn.gogoal.im.fragment.stock.CompanyInfoFragment;
import cn.gogoal.im.fragment.stock.StockMapsFragment;
import cn.gogoal.im.fragment.stock.StockNewsMinFragment;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.dialog.NormalAlertDialog;
import cn.gogoal.im.ui.dialog.StockDetailPopuDialog;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.stock.DialogRecyclerView;
import cn.gogoal.im.ui.view.XTitle;


/**
 * author wangjd on 2017/4/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :个股详情,重构版 ☆☆☆
 */
public class StockDetailActivity extends BaseActivity {

    protected static RotateAnimation rotateAnimation;//旋转动画

    //下拉刷新
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    //大头部
    @BindView(R.id.layout_stock_detail_head)
    View layoutHead;

    //股价
    @BindView(R.id.tv_stock_detail_price)
    TextView tvStockDetailPrice;

    //涨跌额
    @BindView(R.id.tv_stock_detail_change_value)
    TextView tvStockDetailChangeValue;

    //涨跌幅
    @BindView(R.id.tv_stock_detail_change_rate)
    TextView tvStockDetailChangeRate;

    //交易头部右侧列表
    @BindView(R.id.rv_stock_detail_head)
    RecyclerView rvStockDetailHead;
    private List<StockDetail2Text> listHead = new ArrayList<>();
    private HeadInfoAdapter headInfoAdapter;

    @BindView(R.id.iv_show_info_dialog)
    ImageView imageViewShoeDialog;

    @BindArray(R.array.stock_chart_array)
    String[] arrStockChart;

    //新闻Tab
    @BindView(R.id.tab_stock_detail_news)
    TabLayout tabStockDetailNews;

    @BindView(R.id.layout_news_content)
    FrameLayout layoutContent;

    @BindArray(R.array.stock_news_array)
    String[] arrStockNews;

    //"================================dialog"================
    //弹窗交易列表
    @BindView(R.id.rv_stock_detail_treat_popu)
    DialogRecyclerView rvStockTreatPopu;
    @BindArray(R.array.stock_detail_info)
    String[] treatDescArray;
    private List<StockDetail2Text> listTreatPopu;
    private TreatInfoAdapter treatPopuAdapter;

    @BindView(R.id.view_dialog_mask)
    View viewMask;//蒙版1

    @BindView(R.id.view_dialog_mask_bottom)
    View viewMaskBottom;//蒙版2

    private ImageView ivMessageTag;//消息

    String subTitleText = "%s\u3000%s";

    private String stockCode;
    private String stockName;
    private String change_value;
    private int stock_status_type = 1;
    private double closePrice;

    private XTitle xTitle;

    //加自选
    @BindView(R.id.tv_stockDetail_toggle_mystock)
    TextView stock_detail_choose;

    //加自选
    @BindView(R.id.iv_stock_detail_toggle_mystock)
    AppCompatImageView ivToggleMyStock;

    //诊断工具
    private ArrayList<ToolData.Tool> diagnoseStockTools
            = new ArrayList<>();

    //是否分享消息
    private int num;
    private WaitDialog waitDialog;

    //消息
    private BadgeView badge;
    private int unReadCount;

    private boolean isChoose = true;

    private int headHeight;
    private List<Fragment> newsFragments;

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");

        initDialog();//初始化截图

        initTitle();//初始化标题

        initHead();//初始化头部控件

        getStockHeadInfo(AppConst.REFRESH_TYPE_FIRST);//获取交易数据，并填充头部

        initNews();//个股新闻

        initChatMessage();//初始化消息

        iniRefresh(swiperefreshlayout);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockHeadInfo(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                AppManager.getInstance().sendMessage("refresh_stock_news");//发个消息刷新新闻
                swiperefreshlayout.setRefreshing(false);
            }
        });

        layoutHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headHeight = layoutHead.getHeight();
                layoutHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initChatMessage() {
        badge = new BadgeView(getActivity());
        badge.setGravityOffset(10, 7, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(unReadCount);
    }

    private void initDialog() {
        num = getIntent().getIntExtra("num", 0);
        waitDialog = WaitDialog.getInstance("加载中", R.mipmap.login_loading, true);

        if (num == AppConst.TYPE_GET_STOCK) {
            waitDialog.show(getSupportFragmentManager());
            waitDialog.setCancelable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long refreshtime = SPTools.getLong("interval_time", 15000);
        getMyTools();
        if (StockUtils.isMyStock(stockCode)) {
            toggleIsMyStock(true, false);
        } else {
            toggleIsMyStock(false, false);
        }
        //消息未读数
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    private void stopRefreshAnimation(ImageView btnRefresh, @DrawableRes int defauleImage) {
        if (rotateAnimation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, btnRefresh, defauleImage);
        } else {
            btnRefresh.clearAnimation();
            btnRefresh.setImageResource(defauleImage);
        }
    }

    private void startRefreshAnimation(ImageView btnRefresh) {
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(btnRefresh, R.mipmap.loading_white);
        rotateAnimation.startNow();
    }


    //获取我的自选股
    private void getMyTools() {
        diagnoseStockTools.clear();

        take(new TakeToolsListener() {
            @Override
            public void onTakeTools(boolean sueecss, ArrayList<ToolData.Tool> tools) {
                if (sueecss) {
                    for (ToolData.Tool tool : tools) {
                        if (tool.getIsShow() == 1) {
                            diagnoseStockTools.add(tool);
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取诊断工具
     */
    public void take(final TakeToolsListener listener) {
        UserUtils.getAllMyTools(new Impl<String>() {
            @Override
            public void response(int code, String data) {
                switch (code) {
                    case Impl.RESPON_DATA_SUCCESS:
                        List<ToolData> toolDatas = JSONArray.parseArray(data, ToolData.class);
                        for (ToolData tool : toolDatas) {
                            if (tool.getTitle_id() == 3) {
                                if (listener != null) {
                                    listener.onTakeTools(true, tool.getDatas());
                                }
                                break;
                            }
                        }
                        break;
                    default:
                        if (listener != null)
                            listener.onTakeTools(false, null);
                        break;
                }
            }
        });
    }

    /**
     * 诊断工具回调
     */
    private interface TakeToolsListener {
        void onTakeTools(boolean sueecss, ArrayList<ToolData.Tool> tools);
    }

    //自选股切换视图状态
    public void toggleIsMyStock(boolean addMyStock, boolean needToast) {

        ivToggleMyStock.setImageResource(addMyStock ?
                R.mipmap.not_choose_stock : R.mipmap.choose_stock);

        stock_detail_choose.setText(addMyStock ? "已自选" : "加自选");

        if (needToast) {
            UIHelper.toast(getActivity(), addMyStock ? "添加自选成功" : "删除自选成功");
        }

        isChoose = addMyStock;
    }


    /**
     * 初始化头部控件
     */
    private void initHead() {
        //grid treat
        rvStockDetailHead.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        headInfoAdapter = new HeadInfoAdapter(listHead);
        rvStockDetailHead.setAdapter(headInfoAdapter);

        //popu
        rvStockTreatPopu.setLayoutManager(new
                GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
        listTreatPopu = new ArrayList<>();
        treatPopuAdapter = new TreatInfoAdapter(listTreatPopu);
        rvStockTreatPopu.setAdapter(treatPopuAdapter);
        layoutHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewShoeDialog.performClick();
            }
        });
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        setStockState();

       /* xTitle.addAction(new XTitle.ImageAction(getResDrawable(R.mipmap.refresh_white)) {
            @Override
            public void actionClick(View view) {
                //TODO 刷新
            }
        });*/

        XTitle.ImageAction refreshAction = new XTitle.ImageAction(getResDrawable(R.mipmap.refresh_white)) {
            @Override
            public void actionClick(View view) {
                //TODO 刷新
            }
        };

        XTitle.ImageAction messageAction = new XTitle.ImageAction(getResDrawable(R.mipmap.message_white)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(StockDetailActivity.this, MessageHolderActivity.class));
            }
        };

        xTitle.addAction(refreshAction, 0);
        xTitle.addAction(messageAction, 1);

        ivMessageTag = (ImageView) xTitle.getViewByAction(messageAction);

    }

    @OnClick({R.id.iv_show_info_dialog,
            R.id.tv_stockDetail_tools, R.id.tv_stockDetail_stockCircle,
            R.id.tv_stockDetail_interactiveinvestor, R.id.layout_stockDetail_toggle_mystock})
    public void allBtnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //交易popu
            case R.id.iv_show_info_dialog:
                if (isMaskViewVisiable()) {
                    dismissMarket();
                } else {
                    showStockInfoDialog();
                }
                break;

            //工具箱
            case R.id.tv_stockDetail_tools:

                if (!ArrayUtils.isEmpty(diagnoseStockTools)) {
                    StockDetailPopuDialog.newInstance(
                            diagnoseStockTools,
                            new Stock(stockCode, stockName))
                            .show(getSupportFragmentManager());
                } else {
                    NormalAlertDialog.newInstance("暂无诊股工具\n现在去我的工具中添加?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), ToolsSettingActivity.class));
                        }
                    }).show(getSupportFragmentManager());
                }

                break;

            //tog 加减自选股
            case R.id.layout_stockDetail_toggle_mystock:
                startRefreshAnimation(ivToggleMyStock);
                addOptionalShare();
                break;
            //股票圈，进入后台群
            case R.id.tv_stockDetail_stockCircle:
                getGroupChartConvsation();
                break;

            //投资者互动
            case R.id.tv_stockDetail_interactiveinvestor:
                intent.setClass(v.getContext(), InteractiveInvestorActivity.class);
                intent.putExtra("stock_info", getStockBean());
                startActivity(intent);
                break;
        }
    }

    private Stock getStockBean() {
        Stock stock = new Stock(stockCode, stockName);
        stock.setChangeValue(StringUtils.save2Significand(change_value));
        stock.setStock_type(stock_status_type);
        stock.setClosePrice(""+closePrice);
        return stock;
    }

    //添加自选股
    private void addOptionalShare() {
        if (isChoose) {
            final Map<String, String> param = new HashMap<String, String>();
            param.put("token", UserUtils.getToken());
            param.put("stock_code", stockCode);

            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    JSONObject result = JSONObject.parseObject(responseInfo);
                    int data = (int) result.get("code");
                    if (data == 0) {
                        toggleIsMyStock(false, true);
                        StockUtils.removeStock(stockCode);
                    }
                    stopRefreshAnimation(ivToggleMyStock, R.mipmap.choose_stock);
                }

                @Override
                public void onFailure(String msg) {
                    UIHelper.toastError(getActivity(), msg);
                    stopRefreshAnimation(ivToggleMyStock, R.mipmap.not_choose_stock);
                }
            };
            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
        } else {
            final Map<String, String> param = new HashMap<String, String>();
            param.put("token", UserUtils.getToken());
            param.put("group_id", "0");
            param.put("stock_code", stockCode);
            param.put("stock_class", "0");
            param.put("source", "9");
            param.put("group_class", "1");

            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    JSONObject result = JSONObject.parseObject(responseInfo);

                    int data = (int) result.get("code");
                    if (data == 0) {
                        toggleIsMyStock(true, true);
                        StockUtils.addStock2MyStock(stockCode);
                    }
                    stopRefreshAnimation(ivToggleMyStock, R.mipmap.not_choose_stock);
                }

                @Override
                public void onFailure(String msg) {
                    UIHelper.toastError(getActivity(), msg);
                    stopRefreshAnimation(ivToggleMyStock, R.mipmap.choose_stock);
                }
            };
            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, httpInterface).startGet();
        }
    }

    //获取股票群
    private void getGroupChartConvsation() {
        final WaitDialog waitDialog = WaitDialog.getInstance("请稍后...", R.mipmap.login_loading, true);
        waitDialog.show(getSupportFragmentManager());

        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("stock_code", stockCode);
        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_GROUP_ID, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    try {
                        final String conversationId = JSONObject.parseObject(responseInfo).
                                getJSONObject("data").getString("conv_id");

                        List<Integer> addIdList = new ArrayList<>();
                        addIdList.add(Integer.parseInt(UserUtils.getMyAccountId()));
                        //添加群成员
                        ChatGroupHelper.addAnyone(addIdList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                            @Override
                            public void groupActionSuccess(JSONObject object) {
                                Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                                intent.putExtra("conversation_id", conversationId);
                                intent.putExtra("need_update", false);
                                intent.putExtra("square_action", AppConst.CREATE_SQUARE_ROOM_BY_STOCK);
                                startActivity(intent);
                            }

                            @Override
                            public void groupActionFail(String error) {
                                UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                                Log.e("TAG", error);
                            }
                        });

                    } catch (Exception e) {
                        //返回数据异常
                        UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                        Log.e("TAG", e.getMessage());
                    }
                } else {//没有获取到ConvsationId
                    UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                }
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {//获取ConvsationId出错
                waitDialog.dismiss();
                UIHelper.toast(getActivity(), "请求异常");
            }
        }).startGet();
    }

    /**
     * 获取交易数据，并填充头部
     */
    private void getStockHeadInfo(final int refreshType) {
        Map<String, String> map = new HashMap<>();
        map.put("stock_code", stockCode);
        new GGOKHTTP(map, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    listHead.clear();
                    listTreatPopu.clear();

                    TreatData treatData = JSONObject.parseObject(responseInfo, StockDetail.class).getData();

                    change_value = treatData.getChange_value();
                    stock_status_type = treatData.getStock_type();
                    closePrice = StringUtils.parseStringDouble(treatData.getClose_price());

                    setChart(treatData.getStock_type(), treatData.getClose_price(), treatData.getPrice(),
                            treatData.getVolume(), treatData.getUpdate_time());//设置表格

                    setStockHeadColor(treatData.getChange_rate());//设置颜色
                    //设置标题、副标题
                    xTitle.setTitle(stockName + "(" + stockCode + ")\n" + (
                            treatData.getStock_type() == 1 ?
                                    String.format(subTitleText, StockUtils.getTreatState(), CalendarUtils.getCurrentTime("MM-dd HH:mm")) :
                                    String.format(subTitleText, StringUtils.saveSignificand(StringUtils.parseStringDouble(treatData.getOpen_price()), 2),
                                            StockUtils.getStockStatus(treatData.getStock_type()))
                    ));

                    tvStockDetailPrice.setText(StringUtils.saveSignificand(treatData.getPrice(), 2));
                    tvStockDetailChangeValue.setText(
                            StockUtils.plusMinus(StringUtils.saveSignificand(
                                    StringUtils.parseStringDouble(treatData.getChange_value()), 2), false));

                    tvStockDetailChangeRate.setText(
                            StockUtils.plusMinus(treatData.getChange_rate(), true));

                    /*=================数据重构======================================*/
                    //================ head ======================
                    listHead.add(new StockDetail2Text("今开", StringUtils.saveSignificand(treatData.getOpen_price(), 2)));
                    listHead.add(new StockDetail2Text("昨收", StringUtils.saveSignificand(treatData.getClose_price(), 2)));
                    listHead.add(new StockDetail2Text("成交量",
                            formatVolume(treatData.getVolume(), true)));//TODO "volume": 14793700,14.79万手
                    listHead.add(new StockDetail2Text("换手率",
                            StringUtils.saveSignificand(
                                    StringUtils.parseStringDouble(treatData.getTurnover_rate()) * 100, 2) + "%"));
                    headInfoAdapter.notifyDataSetChanged();

                    //=============== treat info popu===================\
                    setDialogInfoData(treatData);

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH ||
                            refreshType == AppConst.REFRESH_TYPE_PARENT_BUTTON) {
                        UIHelper.toast(getActivity(), "刷新成功");
                    }

                    scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            //
                            if (scrollY >= headHeight) {
                                setStockState();
                            }
                        }
                    });


                } else if (code == 1001) {
                    setStockHeadColor("-1");//设置颜色
                    //值数据从缓存中取
                } else {
                    setStockHeadColor("-1");//设置颜色
                }
            }

            @Override
            public void onFailure(String msg) {
                setStockHeadColor("-1");//设置颜色
                UIHelper.toastError(getActivity(), msg);
            }
        }).startGet();

    }

    //股票状态
    private void setStockState() {
        xTitle = setMyTitle(stockName + "(" + stockCode + ")" + "\n" + String.format(subTitleText,
                StockUtils.getTreatState(), CalendarUtils.getCurrentTime("MM-dd HH:mm")), true)
                .setSubTitleSize(TypedValue.COMPLEX_UNIT_SP, 10)
                .setSubTitleColor(Color.argb(204, 255, 255, 255))
                .setLeftImageResource(R.mipmap.image_title_back_255)
                .setLeftText(getString(R.string.str_title_back))
                .setTitleColor(Color.WHITE)
                .setLeftTextColor(Color.WHITE);
    }

    /**
     * 个股新闻
     */
    private void initNews() {

        newsFragments = new ArrayList<>();
        /*
         * stockCode 股票代码
         * stockName 股票代码
         * position 第几个tab
         * */
        newsFragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 0));
        newsFragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 1));
        newsFragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 2));
        newsFragments.add(CompanyInfoFragment.newInstance(stockCode));
        newsFragments.add(CompanyFinanceFragment.getInstance(stockCode, stockName));

        for (String newsTabTitle : arrStockNews) {
            tabStockDetailNews.addTab(tabStockDetailNews.newTab().setText(newsTabTitle));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_news_content, newsFragments.get(0));
        transaction.commit();

        AppDevice.setTabLayoutWidth(tabStockDetailNews, 15);


        tabStockDetailNews.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private int currentPostion = 0;

    private void showFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment showFragment = newsFragments.get(position);

        if (!showFragment.isAdded()) {
            transaction.add(R.id.layout_news_content, showFragment);
        }
        transaction.show(showFragment);
        transaction.hide(newsFragments.get(currentPostion));
        transaction.commit();
        currentPostion = position;
    }


    //设置头部颜色
    private void setStockHeadColor(String change_rate) {
        if (TextUtils.isEmpty(change_rate)) {
            setStatusColor(getResColor(R.color.stock_gray));
            xTitle.setBackgroundColor(getResColor(R.color.stock_gray));
            layoutHead.setBackgroundColor(getResColor(R.color.stock_gray));
        } else {
            setStatusColorId(StockUtils.getStockRateColor(change_rate));
            xTitle.setBackgroundResource(StockUtils.getStockRateColor(change_rate));
            layoutHead.setBackgroundResource(StockUtils.getStockRateColor(change_rate));
        }
    }

    /**
     * 设置表格
     */
    private void setChart(int stockType, String closePrice, String price, String volume, String updateTime) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_chart_content, StockMapsFragment.newInstance(
                stockType, stockName, stockCode, closePrice, price, volume, updateTime));
        transaction.commit();

        //加载消失,截屏开始
        if (num == AppConst.TYPE_GET_STOCK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    waitDialog.dismiss();
                    int screenWidth = AppDevice.getWidth(StockDetailActivity.this);
                    final Bitmap bitmapMessage = ImageUtils.screenshot(scrollView, 0, 0, screenWidth, screenWidth);
                    ImageUtils.saveBitmapToSD(getActivity(), getExternalCacheDir().getAbsolutePath() +
                            File.separator +
                            String.valueOf(System.currentTimeMillis()) + ".png", bitmapMessage, new Impl<String>() {
                        @Override
                        public void response(int code, String url) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("stock_bitmap", bitmapMessage);
                            map.put("stock_bitmapUrl", url);
                            map.put("stock_name", stockName);
                            map.put("stock_code", stockCode);
                            BaseMessage baseMessage = new BaseMessage("stockInfo", map);
                            AppManager.getInstance().sendMessage("oneStock", baseMessage);
                            finish();
                        }
                    });
                }
            }, 1500);

        }
    }

    //========================================数据处理区 START======================================
    private String formatVolume(String volume, boolean typeHead) {
        if (StringUtils.isActuallyEmpty(volume)) {
            return "--";
        }
        if (Integer.parseInt(volume) == 0 || Integer.parseInt(volume) == Double.NaN) {
            return "0手";
        }

        if (volume.length() > 6) {
            return StringUtils.saveSignificand(Double.parseDouble(String.valueOf(volume)) / 1000000d, 2)
                    + (typeHead ? "万手" : "万");
        } else {
            return StringUtils.saveSignificand(Double.parseDouble(String.valueOf(volume)) / 100d, 0) + "手";
        }
    }

    private String formatCapitalization(double volume, int unit) {
        if (volume == 0) {
            return "0";
        }
        if (Double.valueOf(volume).toString().length() < 7) {
            return StringUtils.saveSignificand(volume, 2) + "万";
        } else {
            return StringUtils.saveSignificand(volume / 10000d, unit) + "亿";
        }
    }
    //========================================数据处理区 END======================================

    /**
     * 设置-修改个股详情为弹窗
     */
    private void setDialogInfoData(TreatData info) {
        listTreatPopu.clear();
        //最高价
        listTreatPopu.add(new StockDetail2Text(treatDescArray[0],
                StringUtils.parseStringDouble(info.getHigh_price(), 2)));

        //最低价
        listTreatPopu.add(new StockDetail2Text(treatDescArray[1],
                StringUtils.parseStringDouble(info.getLow_price(), 2)));

        //涨停
        listTreatPopu.add(new StockDetail2Text(treatDescArray[2],
                "" + StringUtils.save2Significand(StringUtils.parseStringDouble(info.getClose_price()) * (
                        (stockName.startsWith("*") ||
                                stockName.contains("ST") ||
                                stockName.startsWith("N")) ? 1.05 : 1.10))));

        //跌停
        listTreatPopu.add(new StockDetail2Text(treatDescArray[3],
                "" + StringUtils.save2Significand(StringUtils.parseStringDouble(info.getClose_price()) * (
                        (stockName.startsWith("*") ||
                                stockName.contains("ST") ||
                                stockName.startsWith("N")) ? 0.95 : 0.9))));

        //内盘
        listTreatPopu.add(new StockDetail2Text(treatDescArray[4],
                (StringUtils.isActuallyEmpty(info.getVolume_inner()) || info.getVolume_inner().length() < 3) ? "0手" : (
                        info.getVolume_inner().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_inner()) / 1000000)) + "万手" :
                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_inner()) / 100))) + "手")));

        //外盘
        listTreatPopu.add(new StockDetail2Text(treatDescArray[5],
                (StringUtils.isActuallyEmpty(info.getVolume_outer()) || info.getVolume_outer().length() < 3) ? "0手" : (
                        info.getVolume_outer().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_outer()) / 1000000)) + "万手" :
                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_outer()) / 100))) + "手")));

        //成交额
        String turnover = StringUtils.parseStringDouble(info.getTurnover(), 2);
        listTreatPopu.add(new StockDetail2Text(treatDescArray[6],
                StringUtils.parseStringDouble(info.getTurnover()) == 0 ? "0" :
                        turnover.length() <= 7 ? StringUtils.parseStringDouble(info.getTurnover(), 2) + "万" :
                                StringUtils.save2Significand(StringUtils.parseStringDouble(info.getTurnover()) / 10000) + "亿"));
        //振幅
        listTreatPopu.add(new StockDetail2Text(treatDescArray[7],
                StringUtils.parseStringDouble(info.getAmplitude(), 2) + "%"));

        //委比
        listTreatPopu.add(new StockDetail2Text(treatDescArray[8],
                StringUtils.parseStringDouble(info.getCommission_rate(), 2) + "%"));

        //量比
        listTreatPopu.add(new StockDetail2Text(treatDescArray[9],
                StringUtils.parseStringDouble(info.getQuantity_ratio(), 2)));

        //流通市值
        String mCapString = StringUtils.parseStringDouble(info.getMcap(), 2);
        listTreatPopu.add(new StockDetail2Text(treatDescArray[10],
                mCapString.length() > 11 ?
                        StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getMcap()) / 100000000d, 2) + "万亿" :
                        mCapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getMcap()) / 10000d, 2) + "亿" :
                                mCapString + "万"));

        //总市值
        String mTcapString = StringUtils.parseStringDouble(info.getTcap(), 2);
        listTreatPopu.add(new StockDetail2Text(treatDescArray[11],
                mTcapString.length() > 11 ?
                        StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getTcap()) / 100000000d, 2) + "万亿" :
                        mTcapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getTcap()) / 10000d, 2) + "亿" :
                                mTcapString + "万"));

        //市盈率
        listTreatPopu.add(new StockDetail2Text(treatDescArray[12],
                StringUtils.parseStringDouble(info.getPe_y1(), 2)));

        //市净率
        listTreatPopu.add(new StockDetail2Text(treatDescArray[13],
                StringUtils.parseStringDouble(info.getPb_y1(), 2)));

        //每股收益
        listTreatPopu.add(new StockDetail2Text(treatDescArray[14],
                StringUtils.parseStringDouble(info.getEps_y1(), 2)));

        //总股本
        String capital = StringUtils.parseStringDouble(info.getCapital(), 2);
        listTreatPopu.add(new StockDetail2Text(treatDescArray[15],
                StringUtils.parseStringDouble(info.getCapital()) == 0 ? "0.00" :
                        (capital.length() > 7 ? StringUtils.save2Significand(StringUtils.parseStringDouble(info.getCapital()) / 10000) + "亿" :
                                StringUtils.parseStringDouble(info.getCapital(), 2) + "万")));

        //流通股
        listTreatPopu.add(new StockDetail2Text(treatDescArray[16],
                StringUtils.parseStringDouble(info.getNegotiable_capital()) == 0 ? "0.00" :
                        (StringUtils.parseStringDouble(info.getNegotiable_capital(), 2).length() > 7 ? StringUtils.save2Significand(StringUtils.parseStringDouble(info.getNegotiable_capital()) / 10000) + "亿" :
                                StringUtils.parseStringDouble(info.getNegotiable_capital(), 2) + "万")));

        treatPopuAdapter.notifyDataSetChanged();
    }

    //显示弹窗
    private void showStockInfoDialog() {
        rvStockTreatPopu.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top));
        rvStockTreatPopu.setVisibility(View.VISIBLE);

        functionViewMask(viewMask, true);
        functionViewMask(viewMaskBottom, true);

        //禁止滑动
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //禁止下拉刷新
        swiperefreshlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //禁止点击
        imageViewShoeDialog.setImageResource(R.mipmap.img_drop_up);
    }

    /**
     * 销毁指数[弹窗]
     */
    public void dismissMarket() {
        if (isMaskViewVisiable()) {
            rvStockTreatPopu.setVisibility(View.GONE);
            rvStockTreatPopu.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_from_top));

            functionViewMask(viewMask, false);
            functionViewMask(viewMaskBottom, false);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            swiperefreshlayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            //恢复可点击
            imageViewShoeDialog.setImageResource(R.mipmap.img_drop_down);
        }
    }

    private void functionViewMask(final View viewMask, boolean show) {
//        viewMaskBottom
        viewMask.setEnabled(show);
        viewMask.setClickable(show);
        viewMask.setVisibility(show ? View.VISIBLE : View.GONE);
        viewMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getActivity(),
                        show ? R.anim.alpha_in : R.anim.alpha_out));

        //点击蒙版消失
        viewMask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismissMarket();
                    viewMask.setEnabled(false);
                    viewMask.setClickable(false);
                }
                return true;
            }
        });

    }

    //弹窗是否可见
    public boolean isMaskViewVisiable() {
        return rvStockTreatPopu.getVisibility() == View.VISIBLE;
    }

    /**
     * 头部适配器
     */
    private class HeadInfoAdapter extends CommonAdapter<StockDetail2Text, BaseViewHolder> {

        private HeadInfoAdapter(List<StockDetail2Text> data) {
            super(R.layout.item_rv_stock_detail_head, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, StockDetail2Text item, int position) {
            holder.setText(R.id.tv_stock_detail_head_desc, item.getKey());
            holder.setText(R.id.tv_stock_detail_head_value, item.getValue());
        }
    }

    /**
     * 交易适配器
     */
    private class TreatInfoAdapter extends CommonAdapter<StockDetail2Text, BaseViewHolder> {

        private String closePrice;

        /**
         * @serialField StockUtils.getStockStatus();
         */
        private int stockType;

        public void setStockType(int stockType) {
            this.stockType = stockType;
        }

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        private TreatInfoAdapter(List<StockDetail2Text> data) {
            super(R.layout.item_stock_detail_dialog_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, StockDetail2Text data, int position) {
            holder.setBackgroundColor(R.id.item_stock_detail_dialog,
                    (position / 4) % 2 == 0 ? 0xffffffff : 0xfff0f4fa);

            holder.setText(R.id.tv_item_stock_detail_info_key, data.getKey());

            holder.setText(R.id.tv_item_stock_detail_info_value, (position == 0 || position == 1) ? (
                    realDouble(data.getValue()) == 0 ? "--" : data.getValue()) : data.getValue());

            switch (position) {
                case 0:
                case 1:
                    if (stockType == 1) {
                        holder.setTextColor(R.id.tv_item_stock_detail_info_value,
                                realDouble(data.getValue()) == 0 ? Color.BLACK : getResColor(
                                        StockUtils.getStockRateColor(realDouble(data.getValue()) -
                                                StringUtils.parseStringDouble(closePrice))));
                    } else {
                        holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
                    }
                    break;
                case 2:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_red));
                    break;
                case 3:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_green));
                    break;
                case 4:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_green));
                    break;
                case 5:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_red));
                    break;
                case 8:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value,
                            getResColor(StockUtils.getStockRateColor(realDouble(data.getValue()))));
                    break;
                default:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
                    break;
            }

        }

        private double realDouble(String value) {
            return StringUtils.parseStringDouble(value.replaceAll("[%手亿万]", ""));
        }
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        unReadCount++;
        badge.setBadgeNumber(unReadCount);
    }
}
