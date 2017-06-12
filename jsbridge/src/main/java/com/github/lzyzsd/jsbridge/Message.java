package com.github.lzyzsd.jsbridge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * data of bridge
 *
 * @author haoqing
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
        Message m = new Message();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            m.setHandlerName(containsKey(HANDLER_NAME_STR, jsonObject) ? jsonObject.getString(HANDLER_NAME_STR) : null);
            m.setCallbackId(containsKey(CALLBACK_ID_STR, jsonObject) ? jsonObject.getString(CALLBACK_ID_STR) : null);
            m.setResponseData(containsKey(RESPONSE_DATA_STR, jsonObject) ? jsonObject.getString(RESPONSE_DATA_STR) : null);
            m.setResponseId(containsKey(RESPONSE_ID_STR, jsonObject) ? jsonObject.getString(RESPONSE_ID_STR) : null);
            m.setData(containsKey(DATA_STR, jsonObject) ? jsonObject.getString(DATA_STR) : null);

            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    private static boolean containsKey(String key, JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            if (key.equalsIgnoreCase(keys.next())) {
                return true;
            }
        }
        return false;
    }

    static List<Message> toArrayList(String jsonStr) {
        List<Message> list = new ArrayList<>();
        if (BridgeUtil.isBlank(jsonStr)) return list;
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                Message m = new Message();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                m.setHandlerName(containsKey(HANDLER_NAME_STR, jsonObject) ? jsonObject.getString(HANDLER_NAME_STR) : null);
                m.setCallbackId(containsKey(CALLBACK_ID_STR, jsonObject) ? jsonObject.getString(CALLBACK_ID_STR) : null);
                m.setResponseData(containsKey(RESPONSE_DATA_STR, jsonObject) ? jsonObject.getString(RESPONSE_DATA_STR) : null);
                m.setResponseId(containsKey(RESPONSE_ID_STR, jsonObject) ? jsonObject.getString(RESPONSE_ID_STR) : null);
                m.setData(containsKey(DATA_STR, jsonObject) ? jsonObject.getString(DATA_STR) : null);
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
