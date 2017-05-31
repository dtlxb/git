package cn.gogoal.im.ui.dialog;

import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.BoxScreenData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.ui.dialog.base.BaseDialog;

/**
 * author wangjd on 2017/5/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class LiveMenuDialog extends BaseDialog {

    @Override
    public int getDialogStyle() {
        return R.style.RightDialog;
    }

    @Override
    public int gravity() {
        return Gravity.RIGHT|Gravity.BOTTOM;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_live_menu;
    }

    @Override
    public int getWidth() {
        return 3*AppDevice.getWidth(getActivity())/4;
    }

    @Override
    public int getHeight() {
        return AppDevice.getHeight(getActivity())-AppDevice.dp2px(getContext(),50);
    }

    @Override
    public void bindView(View v) {


    }

}
