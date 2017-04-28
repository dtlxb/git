package cn.gogoal.im.common;

import android.content.Context;
import android.content.Intent;

import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;

/**
 * author wangjd on 2017/4/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :需要跳转频繁的activity的封装
 */
public class NormalIntentUtils {

    //跳个股详情
    public static void go2StockDetail(Context context, String stockCode, String stockName) {
        Intent intent = new Intent(context, CopyStockDetailActivity.class);
        intent.putExtra("stock_code", stockCode);
        intent.putExtra("stock_name", stockName);
        context.startActivity(intent);
    }

    //跳网页Web Activiy
    public static void go2WebActivity(Context context,String url,String title){
        Intent intent = new Intent(context, FunctionActivity.class);
        intent.putExtra("function_url", url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }
}
