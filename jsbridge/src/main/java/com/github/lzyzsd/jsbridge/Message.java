package com.github.lzyzsd.jsbridge;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * data of bridge
 * @author haoqing
 *
 */
class Message {

    private String callbackId; //callbackId
    private String responseId; //responseId
    private String responseData; //responseData
    private String data; //data of message
    private String handlerName; //name of handler

    private final static String CALLBACK_ID_STR = "callbackId";
    private final static String RESPONSE_ID_STR = "responseId";
    private final static String RESPONSE_DATA_STR = "responseData";
    private final static String DATA_STR = "data";
    private final static String HANDLER_NAME_STR = "handlerName";

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CALLBACK_ID_STR, getCallbackId());
            jsonObject.put(DATA_STR, getData());
            jsonObject.put(HANDLER_NAME_STR, getHandlerName());
            jsonObject.put(RESPONSE_DATA_STR, getResponseData());
            jsonObject.put(RESPONSE_ID_STR, getResponseId());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Message toObject(String jsonStr) {
        Message m =  new Message();
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            m.setHandlerName(jsonObject.containsKey(HANDLER_NAME_STR) ? jsonObject.getString(HANDLER_NAME_STR):null);
            m.setCallbackId(jsonObject.containsKey(CALLBACK_ID_STR) ? jsonObject.getString(CALLBACK_ID_STR):null);
            m.setResponseData(jsonObject.containsKey(RESPONSE_DATA_STR) ? jsonObject.getString(RESPONSE_DATA_STR):null);
            m.setResponseId(jsonObject.containsKey(RESPONSE_ID_STR) ? jsonObject.getString(RESPONSE_ID_STR):null);
            m.setData(jsonObject.containsKey(DATA_STR) ? jsonObject.getString(DATA_STR):null);
            return m;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }

    static List<Message> toArrayList(String jsonStr){
        List<Message> list = new ArrayList<>();
        if (BridgeUtil.isBlank(jsonStr)) return list;
        try {
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            for(int i = 0; i < jsonArray.size(); i++){
                Message m = new Message();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                m.setHandlerName(jsonObject.containsKey(HANDLER_NAME_STR) ? jsonObject.getString(HANDLER_NAME_STR):null);
                m.setCallbackId(jsonObject.containsKey(CALLBACK_ID_STR) ? jsonObject.getString(CALLBACK_ID_STR):null);
                m.setResponseData(jsonObject.containsKey(RESPONSE_DATA_STR) ? jsonObject.getString(RESPONSE_DATA_STR):null);
                m.setResponseId(jsonObject.containsKey(RESPONSE_ID_STR) ? jsonObject.getString(RESPONSE_ID_STR):null);
                m.setData(jsonObject.containsKey(DATA_STR) ? jsonObject.getString(DATA_STR):null);
                list.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
