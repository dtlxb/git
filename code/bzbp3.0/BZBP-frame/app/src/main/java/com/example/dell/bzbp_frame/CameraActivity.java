package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.example.dell.bzbp_frame.tool.LogUtil;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;

public class CameraActivity extends TakePhotoActivity {
   //private static String intent_url= Environment.getExternalStorageDirectory()+ "/temp/"+System.currentTimeMillis() + ".jpg";
    //final private String lo = intent_url;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.bundle = this.getIntent().getExtras();
        Long time = (Long)bundle.getLong("time");
        String location= Environment.getExternalStorageDirectory()+ "/temp/"+time+ ".jpg";
        //String location= lo;

        File file=new File(location);
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(imageUri);
        LogUtil.d("1",location+"location");
    }

    @Override
    public void takeCancel() {
        Intent i = new Intent(CameraActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle intent_bundle = new Bundle();
        //test
        //User test = new User();
        //test.setUsername("test1");
        //test.setPassword("test2");
        //intent_bundle.putSerializable("user",test);
        intent_bundle.putSerializable("user",this.bundle.getSerializable("user"));
        i.putExtras(intent_bundle);
        startActivity(i);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Intent i = new Intent(CameraActivity.this,ShareActivity.class);
        String s = result.getImage().getOriginalPath();
        LogUtil.d("1",s +"th");
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //long time = timestamp.getTime();// 直接转换成long

        this.bundle.putString("images",s);
        //this.bundle.putSerializable("time",time);
        i.putExtras(this.bundle);
        startActivity(i);
    }

}
