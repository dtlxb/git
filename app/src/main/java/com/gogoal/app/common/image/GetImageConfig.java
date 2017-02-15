package com.gogoal.app.common.image;

import android.content.Context;
import android.content.Intent;

import com.hply.imagepicker.ImagePicker;
import com.hply.imagepicker.ui.ImageGridActivity;
import com.hply.imagepicker.view.CropImageView;
import com.socks.library.KLog;

/**
 * author wangjd on 2017/2/14 0014.
 * Staff_id 1375
 * phone 18930640263
 */
public class GetImageConfig {

    private boolean canCrop;

    private int limit = 9;

    public GetImageConfig setCanCrop(boolean canCrop) {
        if (limit>1){
            KLog.e("多张图片不允许使用裁剪功能");
            limit=1;
        }else {
            this.canCrop = canCrop;
        }
        init();

        return this;
    }

    private GetImageConfig() {
    }

    /**
     * 静态内部类实现单利
     */
    private static class ConfigHolder {
        private static final GetImageConfig INSTANCE = new GetImageConfig();
    }

    public static final GetImageConfig getInstance() {
        return ConfigHolder.INSTANCE;
    }

    public GetImageConfig setLimit(int limit) {
        this.limit = limit;
        init();
        return this;
    }


    public void takePhoto(Context context) {
        context.startActivity(new Intent(context, ImageGridActivity.class));
    }

    private void init() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());       //设置图片加载器
        imagePicker.setShowCamera(true);                        //显示拍照按钮
        imagePicker.setCrop(canCrop);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                     //是否按矩形区域保存
        imagePicker.setSelectLimit(limit);                      //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);   //裁剪框的形状
        imagePicker.setFocusWidth(800);                         //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                           //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                           //保存文件的高度。单位像素
    }
}
