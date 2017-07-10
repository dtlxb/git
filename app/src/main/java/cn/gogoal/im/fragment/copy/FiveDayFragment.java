package cn.gogoal.im.fragment.copy;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.MessageHandlerList;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.ui.stockviews.LandScapeChartView;
import cn.gogoal.im.ui.stockviews.TimesFivesBitmap;
import hply.com.niugu.stock.StockMinuteBean;

public class FiveDayFragment extends Fragment {
    @BindView(R.id.fiveday_bitmapchartview)
    LandScapeChartView mBitmapChartView;
    private TimesFivesBitmap fiveDayBitmap;
    private int width;
    private int height;
    private int totalHeight;
    private String stockCode;
    private int stockType;
    private StockMinuteBean bean;
    //定时刷新
    private Timer timer;
    private int refreshtime;
    private int stock_charge_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockCode = getActivity().getIntent().getStringExtra("stockCode");
        Bundle bundle = getArguments();
        width = bundle.getInt("width", 0);
        height = bundle.getInt("height", 0);
        totalHeight = bundle.getInt("totalHeight", 0);
        stockType = bundle.getInt("type");
        stock_charge_type = bundle.getInt("stock_charge_type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fivedaybitmap, container, false);
        ButterKnife.bind(this, view);
        refreshtime = SPTools.getInt("refreshtime", 15000);
        getFiveData(true);
        return view;
    }

    private void getFiveData(boolean needShowProgress) {
        if (needShowProgress) {
            ((StockDetailChartsActivity) getActivity()).showProgressbar(true);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("day", "5");
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            param.put("stock_code", stockCode);
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            param.put("fullcode", stockCode);
            param.put("avg_line_type", "150");
        }

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                bean = JSONObject.parseObject(responseInfo, StockMinuteBean.class);

                if (bean.getCode() == 0) {
                    new BitmapTask().execute();
                    //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, AppConst.DISS_PROGRESSBAR, 0);
                    AppManager.getInstance().sendMessage("Diss_Progressbar");
                    ((StockDetailChartsActivity) getActivity()).showProgressbar(false);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            new GGOKHTTP(param, GGOKHTTP.STOCK_MINUTE, ggHttpInterface).startGet();
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            new GGOKHTTP(param, GGOKHTTP.GET_HQ_MINUTE, ggHttpInterface).startGet();
        }
    }

    private void AutoRefresh(int time) {
        if (StockUtils.isTradeTime()) {
            setTime(time);
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    public void setTime(int num) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                AppManager.getInstance().sendMessage("FiveDayFragment", "fiveData");
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(timerTask, num, num);
    }

    @Subscriber(tag = "FiveDayFragment")
    private void refresh(String s) {
        if (StockUtils.isTradeTime()) {
            getFiveData(false);
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        AutoRefresh(refreshtime);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    class BitmapTask extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            fiveDayBitmap = new TimesFivesBitmap(width, height);
            fiveDayBitmap.setShowDetail(true);
            fiveDayBitmap.setLongitudeNum(4);
            fiveDayBitmap.setmSize(AppDevice.dp2px(getActivity(), 1));
            fiveDayBitmap.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
            //画交互点
            fiveDayBitmap.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
            if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
                fiveDayBitmap.setLeftMargin(AppDevice.dp2px(getActivity(), 40));
                fiveDayBitmap.setRightMargin(AppDevice.dp2px(getActivity(), 40));
            } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
                fiveDayBitmap.setLeftMargin(AppDevice.dp2px(getActivity(), 45));
                fiveDayBitmap.setRightMargin(AppDevice.dp2px(getActivity(), 45));
            }
            return fiveDayBitmap.setTimesList(bean, false, stock_charge_type);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mBitmapChartView.setBitmap(bitmap);
            mBitmapChartView.setData(fiveDayBitmap, stock_charge_type);
        }
    }
}
