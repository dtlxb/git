package com.example.dell.bzbp_frame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

public abstract class BaseActivity extends AppCompatActivity{

    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = true;

    /** 当前Activity渲染的视图View **/
    private View mContentView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化数据
        initData();
        // 初始化ui
        initView();
        // 添加监听器
        initListener();

        doBusiness(savedInstanceState);
    }



    // 初始化数据
    protected abstract void initData();

    // 初始化ui
    protected abstract void initView();

    // 添加监听器
    protected abstract void initListener();

    protected abstract void doBusiness(Bundle savedInstanceState);

    public void msgbox(String msg)
    {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public void submit(MyThread myThread, String url, User user, int what){
        myThread.setGetUrl("http://"+url);
        myThread.setUser(user);

        myThread.setWhat(what);
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void submit(MyThread myThread, String url, Posto posto, int what){
        myThread.setGetUrl("http://"+url);
        myThread.setPosto(posto);
        myThread.setWhat(what);
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
