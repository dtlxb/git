package com.gogoal.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gogoal.app.common.image.GetImageConfig;
import com.hply.imagepicker.ImagePicker;
import com.hply.imagepicker.bean.ImageItem;
import com.hply.imagepicker.ui.ImagePreviewActivity;

import java.util.ArrayList;

/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 *
 * 中间的辅助类Activity
 */
public class TakePhotoActivity extends Activity {

    public static final int IMAGE_PICKER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int limit = getIntent().getIntExtra("limit", 9);
        boolean canCrop = getIntent().getBooleanExtra("canCrop", false);

        GetImageConfig.getInstance()
                .setLimit(limit)
                .setCanCrop(canCrop)
                .takePhoto(TakePhotoActivity.this);


//        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
            if (data != null) {
                //是否发送原图
                boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                Log.e("CSDN_LQR", isOrig ? "发原图" : "不发原图");//若不发原图的话，需要在自己在项目中做好压缩图片算法
                for (ImageItem imageItem : images) {
                    Log.e("CSDN_LQR", imageItem.path);
                }
            }
        }
    }
}
