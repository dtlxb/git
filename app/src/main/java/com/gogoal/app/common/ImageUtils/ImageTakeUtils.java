package com.gogoal.app.common.ImageUtils;

import android.content.Context;
import android.content.Intent;

import com.gogoal.app.activity.TakePhotoActivity;
import com.hply.imagepicker.ITakePhoto;

/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */
public class ImageTakeUtils {

    private ITakePhoto listener;

    private ImageTakeUtils() {
    }

    private static ImageTakeUtils instance = null;

    public static ImageTakeUtils getInstance() {
        if (instance == null) {
            synchronized (ImageTakeUtils.class) {
                if (instance == null) {
                    instance = new ImageTakeUtils();
                }
            }
        }
        return instance;
    }

    public void takePhoto(Context context,int limit,boolean canCrop,ITakePhoto takePhotoListener){
        this.listener=takePhotoListener;
        Intent intent=new Intent(context, TakePhotoActivity.class);
        intent.putExtra("limit",limit);
        intent.putExtra("canCrop",canCrop);
        context.startActivity(intent);
    }

    public ITakePhoto getListener() {
        return listener;
    }
}
