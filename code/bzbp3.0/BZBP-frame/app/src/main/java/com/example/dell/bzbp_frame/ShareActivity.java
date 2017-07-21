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

public class ShareActivity extends Activity {
    private TextView textview_address;
    private ImageView imageview_image;
    private Button mCancelButton;
    private Button mShareButton;
    private String share_choice;
    //public static String ip="50.78.0.134:8080/BookStore";
     public static String ip="192.168.1.97:8080/BookStore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        final Bundle bundle = this.getIntent().getExtras();
        //获取user
        User user = (User)bundle.getSerializable("user");
        //获取照片在手机中的路径并生成bitmap
        String image_path = (String) bundle.getString("images");
       // String image_path = Environment.getExternalStorageDirectory()+"/temp/1499824307256.jpg";
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
        final Bitmap bitmap = BitmapFactory.decodeStream(fis,null,opts);



        //回收


        //获取命名&评论
        final EditText editText_name=(EditText)findViewById(R.id.edittext_share_name);
        final EditText editText_comment=(EditText)findViewById(R.id.edittext_share_comment);

        //test
        /*
        textview_address = (TextView) findViewById(R.id.text_view_address);
        textview_address.setText(user.getUsername());
        */
        //显示图片
        imageview_image = (ImageView) findViewById(R.id.imageview_share_image);
        imageview_image.setImageBitmap(bitmap);

        Spinner spinner = (Spinner) findViewById(R.id.share_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        this.findViewById(R.id.button_share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShareActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });


        this.findViewById(R.id.button_share_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------
                Posto temp = new Posto();
                temp.setUsername(((User)bundle.getSerializable("user")).getUsername());
                temp.setName(editText_name.getText().toString());
                temp.setComment(editText_comment.getText().toString());

                //对于route过程中的posto，设置其belong_rid
                if (bundle.getBoolean("is_in_route"))temp.setBelong_rid(bundle.getInt("rid"));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] appicon =  baos.toByteArray();// 转为byte数组
                String url = Base64.encodeToString(appicon, Base64.NO_WRAP);
                temp.setImage(url);



                temp.setLatitude((Double)bundle.getDouble("latitude"));
                temp.setLongitude((Double)bundle.getDouble("longitude"));
                String test = share_choice;
                temp.setPath_local(share_choice);
                temp.setDate((Long) bundle.getSerializable("time"));
                temp.setBelong_rid(-1);
                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/addPosto");
                myThread1.setWhat(0);
                myThread1.setPosto(temp);
                myThread1.start();

                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                    intent_bundle.putSerializable("user",((User)bundle.getSerializable("user")));
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
}
