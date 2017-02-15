package com.github.lzyzsd.jsbridge;

import android.webkit.ValueCallback;

public class DefaultHandler implements BridgeHandler{

    @Override
    public void handler(String data, ValueCallback<String> function) {
        if(function != null){
//            function.onReceiveValue("DefaultHandler response data");
            function.onReceiveValue(data);
        }
    }
}
