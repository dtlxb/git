package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.LogUtil;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.sql.Timestamp;

public class CameraActivity extends TakePhotoActivity {
   //private static String intent_url= Environment.getExternalStorageDirectory()+ "/temp/"+System.currentTimeMillis() + ".jpg";
    //final private String lo = intent_url;
    private Bundle bundle;
    private boolean is_in_route = false;    //是否在route里
    private int rid;                        //如果在，则获得rid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得bundle
        this.bundle = this.getIntent().getExtras();

        //判断这次posto是否在route里，若在，则取出rid
        if (bundle.getBoolean("is_in_route")){
            rid = bundle.getInt("rid");
        }

        //为了命名方便，从之前的activity里获得time
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

        CameraActivity.this.finish();

        /*
        //在route中拍照的流程
        if (this.bundle.getBoolean("is_in_route")){
            CameraActivity.this.finish();
        }


        //默认拍照的流程

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
        */
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

        //route

        //设置拍摄的时间,这个时间替换掉之前的time，它将会被存进数据库。
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long time = timestamp.getTime();// 直接转换成long
        bundle.putLong("time",time);
        i.putExtras(this.bundle);
        startActivity(i);
        CameraActivity.this.finish();
    }

}
