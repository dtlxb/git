package cn.gogoal.im.common;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;

import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.activity.PdfDisplayActivity;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;
import cn.gogoal.im.bean.PdfData;

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

    /**
     * 跳网页Web Activiy
     *
     * @param context 上下文
     * @param url     跳转的url(拼好的)
     * @param title   web页面的原生标题头
     */
    public static void go2WebActivity(Context context, String url, String title) {
        go2WebActivity(context,url,title,true);
    }

    /**
     * 跳网页Web Activiy
     *
     * @param context   上下文
     * @param url       跳转的url(拼好的)
     * @param title     web页面的原生标题头
     * @param needShare 是否支持分享，(显示分享按钮判断)
     */
    public static void go2WebActivity(Context context, String url, String title, boolean needShare) {
        Intent intent = new Intent(context, FunctionActivity.class);
        intent.putExtra("function_url", url);
        intent.putExtra("title", StringUtils.isActuallyEmpty(title)?"":title);
        intent.putExtra("need_share", needShare);
        context.startActivity(intent);
    }

//    /**
//     * 跳好友选择 Activiy
//     *
//     * @param context   上下文
//     * @param url       跳转的url(拼好的)
//     * @param title     web页面的原生标题头
//     * @param needShare 是否支持分享，(显示分享按钮判断)
//     */
//    public static void go2ChooseContactsActivity(Context context,int ) {
//        Intent intent = new Intent(context, ChooseContactActivity.class);
//        intent.putExtra("function_url", url);
//        intent.putExtra("title", title);
//        intent.putExtra("need_share", needShare);
//        context.startActivity(intent);
//    }

    /**
     * 跳网页Web pdf预览
     *
     * @param context   上下文
     * @param url       pdf url
     */
    public static void go2PdfDisplayActivity(Context context, String url,String pdfTitle) {
        Intent intent = new Intent(context, PdfDisplayActivity.class);
        intent.putExtra("pdf_data", JSONObject.toJSONString(new PdfData(url,pdfTitle)));
        context.startActivity(intent);
    }

    /**
     * 跳网页Web pdf预览
     *
     * @param context   上下文
     */
    public static void go2MainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    /**
     * 跳网页Web pdf预览
     *
     * @param context   上下文
     */
    public static void go2LoginActivity(Context context) {
        Intent intent = new Intent(context, TypeLoginActivity.class);
        context.startActivity(intent);
    }
}
