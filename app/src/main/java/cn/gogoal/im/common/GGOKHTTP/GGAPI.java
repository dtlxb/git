package cn.gogoal.im.common.GGOKHTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 公司数据API
 */
public class GGAPI {

    //开发环境
//    private static final String Native_API = "http://ggservice.sandbox.gofund.com.cn";
//    private static final String APP_KEY = "HBTORVzBaGtqmGE";
//    private static final String APP_SECRET = "cGYIsTyTWXGeP4frqOqmdWxRO5xmh2Pi";

    //春哥环境
//    private static final String Native_API = "http://192.168.52.150:9000";
//    private static final String APP_KEY = "HBTORVzBaGtqmGE";
//    private static final String APP_SECRET = "cGYIsTyTWXGeP4frqOqmdWxRO5xmh2Pi";

    //预正式环境
//    private static final String Native_API = "http://ggservice-pre.go-goal.cn";
//    private static final String APP_KEY = "VzNQumNMxCmPcbD";
//    private static final String APP_SECRET = "SYRyCEmkmZsMm8xnN5VrLQLXfc7C9GB1";

    //正式环境
    private static final String Native_API = "https://ggservice.go-goal.cn";
    private static final String APP_KEY = "aQbUnbaRdkkHnBG";
    private static final String APP_SECRET = "bgX2vMuKMkkVX2izTrQv2iGXavfExIEf";

    public static String get(String api, Map<String, String> params) throws Exception {
        long timeStamp = System.currentTimeMillis() / 1000L;
        String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "GET");
        String incomingParams = "";
        String url;
        if (params != null) {
            try {
                for (Iterator e = params.keySet().iterator(); e.hasNext();
                     incomingParams = incomingParams + url + "=" + URLEncoder.encode(params.get(url), "UTF-8") + "&") {
                    url = (String) e.next();
                }
            } catch (UnsupportedEncodingException var9) {
                var9.printStackTrace();
            }

            incomingParams = incomingParams.substring(0, incomingParams.length() - 1);
        }

        url = "";

        try {
            url = "/" + api + "?" + "app_key=" + APP_KEY + "&time_stamp=" + timeStamp + "&sign=" + URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        }

        if (!"".equals(incomingParams.trim())) {
            url = url + "&" + incomingParams;
        }

        url = Native_API + url;
        return url;
    }

    public static String getReal(String api, Map<String, String> params) throws Exception {
        long timeStamp = System.currentTimeMillis() / 1000L;
        String sign = getSign("GnbhWYzxfcbrMOd", "I7WFKwulOrcYPHu8ZeQcMFEsiwQ45ruS", timeStamp, params, api, "GET");
        String incomingParams = "";
        String url;
        if (params != null) {
            try {
                for (Iterator e = params.keySet().iterator(); e.hasNext();
                     incomingParams = incomingParams + url + "=" + URLEncoder.encode(params.get(url), "UTF-8") + "&") {
                    url = (String) e.next();
                }
            } catch (UnsupportedEncodingException var9) {
                var9.printStackTrace();
            }

            incomingParams = incomingParams.substring(0, incomingParams.length() - 1);
        }

        url = "";

        try {
            url = "/" + api + "?" + "app_key=" + "GnbhWYzxfcbrMOd" + "&time_stamp=" + timeStamp + "&sign=" + URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        }

        if (!"".equals(incomingParams.trim())) {
            url = url + "&" + incomingParams;
        }

        url = "https://ggservice.go-goal.cn" + url;
        return url;
    }

    protected static Map<String, Object> post(String api, Map<String, String> params) throws Exception {
        long timeStamp = System.currentTimeMillis() / 1000L;
        String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "POST");
        params.put("app_key", APP_KEY);
        params.put("time_stamp", String.valueOf(timeStamp));
        params.put("sign", sign);
        Map<String, Object> result = new HashMap<>();
        result.put("url", Native_API + "/" + api);
        result.put("params", params);
        return result;
    }

    private static String getSign(String appKey, String appSecret, Long timeStamp, Map<String, String> params, String url, String requestMethod) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (params != null) {
            paramsMap.putAll(params);
        }

        paramsMap.put("app_key", appKey);
        paramsMap.put("time_stamp", timeStamp.toString());
        paramsMap.remove("sign");
        SignHelper.codePayValue(paramsMap);

        try {
            return SignHelper.makeSign(requestMethod, url, paramsMap, appSecret);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
}
