package cn.gogoal.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hply.imagepicker.ITakePhoto;
import com.hply.imagepicker.ImagePicker;
import com.hply.imagepicker.bean.ImageItem;
import com.hply.imagepicker.ui.ImageGridActivity;
import com.hply.imagepicker.ui.ImagePreviewActivity;
import com.hply.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.ImageUtils.UILImageLoader;

/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 中间的辅助类Activity 配置
 */
public class TakePhotoActivity extends Activity{

    public static final int IMAGE_PICKER = 100;
    private ITakePhoto takePhotoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        takePhotoListener= ImageTakeUtils.getInstance().getListener();

        int limit = getIntent().getIntExtra("limit", 9);

        int canCropSize=getIntent().getIntExtra("canCropSize",200);

        boolean canCrop = getIntent().getBooleanExtra("canCrop", false);

        init(limit, canCrop,canCropSize);

        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
            if (data != null) {
                //是否发送原图
                boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                List<String> paths = new ArrayList<>();
                for (ImageItem item : images) {
                    paths.add(item.path);
                }

                if (takePhotoListener != null) {
                    takePhotoListener.success(paths, isOrig);
                }
            } else {
                takePhotoListener.error();
            }
        }
        finish();
    }

    /**
     * 初始化配置——config
     */

    private void init(int limit, boolean canCrop,int canCropSize) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());       //设置图片加载器
        imagePicker.setShowCamera(true);                        //显示拍照按钮
        imagePicker.setCrop(canCrop);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                     //是否按矩形区域保存
        imagePicker.setSelectLimit(limit);                      //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);   //裁剪框的形状
        imagePicker.setFocusWidth(canCropSize);                  //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(canCropSize);                 //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(canCropSize);                           //保存文件的宽度。单位像素
        imagePicker.setOutPutY(canCropSize);                           //保存文件的高度。单位像素
        imagePicker.setMultiMode(limit!=1);
    }

}
