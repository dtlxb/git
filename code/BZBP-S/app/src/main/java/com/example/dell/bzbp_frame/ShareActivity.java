package com.example.dell.bzbp_frame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ShareActivity extends BaseActivity {

    private ImageView imageview_share_image;
    private EditText editText_share_name;
    private EditText editText_share_comment;
    private Button button_share_share;
    private Button button_share_cancel;
    private Spinner spinner_share;
    private String share_choice;
    public static String ip;
    private Bundle bundle;
    private User user;
    private Bitmap bitmap;

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);

        bundle = this.getIntent().getExtras();
        //获取user
        user = (User)bundle.getSerializable("user");
        //获取照片在手机中的路径并生成bitmap
        String image_path = (String) bundle.getString("images");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(image_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inSampleSize =2;
        bitmap = BitmapFactory.decodeStream(fis,null,opts);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_share);
        imageview_share_image = (ImageView)findViewById(R.id.imageview_share_image);
        editText_share_name = (EditText)findViewById(R.id.edittext_share_name);
        editText_share_comment = (EditText)findViewById(R.id.edittext_share_comment);
        button_share_share = (Button)findViewById(R.id.button_share_share);
        button_share_cancel = (Button)findViewById(R.id.button_share_cancel);
        spinner_share = (Spinner)findViewById(R.id.spinner_share);
    }

    @Override
    protected void initListener() {
        //用户选择照片公开度：public，friend，private
        spinner_share.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] share = getResources().getStringArray(R.array.share);
                share_choice = share[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                share_choice = "public";
            }
        });

        //cancel
        button_share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareActivity.this.finish();
            }
        });

        //提交posto
        button_share_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------
                Posto temp = new Posto();
                temp.setUsername(user.getUsername());
                temp.setName(editText_share_name.getText().toString());
                temp.setComment(editText_share_comment.getText().toString());


                //将图片转化为String
                ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] appicon =  baos.toByteArray();// 转为byte数组
                String url = Base64.encodeToString(appicon, Base64.NO_WRAP);
                //包装posto
                temp.setImage(url);
                temp.setLatitude((Double)bundle.getDouble("latitude"));
                temp.setLongitude((Double)bundle.getDouble("longitude"));
                //String test = share_choice;
                temp.setPath_local(share_choice);
                temp.setDate((Long) bundle.getSerializable("time"));
                temp.setBelong_rid(-1);
                //对于route过程中的posto，设置其belong_rid
                if (bundle.getBoolean("is_in_route"))temp.setBelong_rid(bundle.getInt("rid"));

                MyThread myThread1 = new MyThread();
                submit(myThread1,ip+"/rest/addPosto",temp,0);

                int last_pid = -1;
                if (myThread1.getRid()!=null){
                    last_pid = myThread1.getRid().intValue();//本次新posto的id
                }

                //------------------------------------------------
                //测试
                //Intent i = new Intent(ShareActivity.this,Test2Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //Bundle intent_bundle = new Bundle();
                //intent_bundle.putSerializable("temp",temp);
                //i.putExtras(intent_bundle);
                //
                //对于route中的posto,返回之前的route
                if (bundle.getBoolean("is_in_route")){
                    Intent i = new Intent(ShareActivity.this,RouteActivity.class);
                    Bundle intent_bundle = new Bundle();
                    //user还是之前的user
                    intent_bundle.putSerializable("user",user);
                    //route对象的pid list里加进本次新的pid
                    intent_bundle.putInt("last_pid",last_pid);
                    //intent_bundle.putSerializable("last_posto",temp);
                    //Route r = (Route) bundle.getSerializable("route");
                    //r.getPids().add(last_pid);
                    //intent_bundle.putSerializable("route",r);

                    //把route对象传回routeactivity，以恢复拍照前的状态
                    intent_bundle.putSerializable("route",(Route)bundle.getSerializable("route"));
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(ShareActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",((User)bundle.getSerializable("user")));
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        //显示图片
        imageview_share_image.setImageBitmap(bitmap);

    }
}