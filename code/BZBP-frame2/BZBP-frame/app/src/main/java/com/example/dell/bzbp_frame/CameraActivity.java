package com.example.dell.bzbp_frame;

import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.content.Intent;

import com.example.dell.bzbp_frame.model.User;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;

public class CameraActivity extends TakePhotoActivity {

    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.bundle = this.getIntent().getExtras();

        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(imageUri);
    }

    @Override
    public void takeCancel() {
        Intent i = new Intent(CameraActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Intent i = new Intent(CameraActivity.this,ShareActivity.class);
        String s = result.getImage().getOriginalPath();
        //i.putExtra("images",s);
        this.bundle.putString("images",s);
        i.putExtras(this.bundle);
        startActivity(i);
    }

}
