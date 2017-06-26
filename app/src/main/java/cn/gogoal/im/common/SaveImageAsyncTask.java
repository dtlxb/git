package cn.gogoal.im.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.socks.library.KLog;

import cn.gogoal.im.common.ImageUtils.ImageUtils;

/**
 * author wangjd on 2017/6/23 0023.
 * Staff_id 1375
 * phone 18930640263
 * description :Bitmap异步保存到.../DCIM/GoGoal文件夹
 */
public class SaveImageAsyncTask extends AsyncTask<Bitmap, Void, Integer> {

    private String flag;//保存的图片类型，
    private Context context;//上下文
    private String saveName;

    private int resCode;

    private SaveImageAsyncTask(Context context, String saveName) {
        this.context = context;
        this.saveName = saveName;
    }

    public SaveImageAsyncTask(Context context,String saveName,String flag) {
        this.context = context;
        this.saveName=saveName;
        this.flag = flag;
    }

    @Override
    protected void onPostExecute(Integer code) {
        if (StringUtils.isActuallyEmpty(flag)) {
            UIHelper.toast(context, code == 0 ? "保存成功" : "保存出错，请重试");
        } else {
            UIHelper.toast(context, code == 0 ? flag + "已成功保存" : "保存" + flag + "出错，请重试");
        }
    }

    @Override
    protected Integer doInBackground(Bitmap... params) {
        ImageUtils.saveImage2DCIM(
                context,
                params[0],
                StringUtils.isActuallyEmpty(saveName) ? String.valueOf(System.currentTimeMillis()) : saveName,
                new Impl<String>() {
                    @Override
                    public void response(int code, String data) {
                        KLog.e("code=" + code + ";data=" + data);
                        resCode = code;
                    }
                });
        return resCode;
    }

}
