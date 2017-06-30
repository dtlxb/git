package com.example.li.gd_test_00;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.util.ContentLengthInputStream;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PhotoActivity extends TakePhotoActivity {
    private static Context context;
    private static String str;
    private CustomHelper customHelper;
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=getApplicationContext();
        super.onCreate(savedInstanceState);
        View contentView= LayoutInflater.from(this).inflate(R.layout.common_layout,null);
        setContentView(contentView);
        customHelper=CustomHelper.of(contentView);
    }

    public void onClick(View view) {
        str=customHelper.onClick(view,getTakePhoto());


    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {

        File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/IMG_" + str + ".jpg");
        new SingleMediaScanner(this, file);

        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("DCIM/Camera/" )));


        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("images",images);
        startActivity(intent);
    }
}