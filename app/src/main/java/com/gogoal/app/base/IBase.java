package com.gogoal.app.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public interface IBase {

    /**
     * 绑定渲染视图的布局文件
     * @return 布局文件资源id
     */
    public int bindLayout();

    /**
     * 初始化控件
     */
    public void initView(final View view);

    /**
     * 业务处理操作（onCreate方法中调用）
     * @param mContext  当前Activity对象
     */
    public void doBusiness(Context mContext);

    /**
     * 暂停恢复刷新相关操作（onResume方法中调用）
     */
    public void resume();
}
