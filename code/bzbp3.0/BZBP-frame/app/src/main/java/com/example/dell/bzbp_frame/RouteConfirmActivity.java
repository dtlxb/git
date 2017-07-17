package com.example.dell.bzbp_frame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RouteConfirmActivity extends Activity {
    private TextView textview_address;
    private ImageView imageview_image;
    private Button mCancelButton;
    private Button mShareButton;
    //public static String ip="50.78.0.134:8080/BookStore";
    public static String ip="192.168.1.97:8080/BookStore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_confirm);


        final Bundle bundle = this.getIntent().getExtras();
        //获取user
        User user = (User)bundle.getSerializable("user");
        final Route route = (Route)bundle.getSerializable("route");
        //获取命名&评论
        final EditText editText_route_name=(EditText)findViewById(R.id.edittext_confirm_name);
        final EditText editText_route_comment=(EditText)findViewById(R.id.edittext_confirm_comment);


        this.findViewById(R.id.button_confirm_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteConfirmActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });


        this.findViewById(R.id.button_confirm_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------
                Route temp = new Route();
                //包装需要上传的route
                temp = route;
                temp.setName(editText_route_name.getText().toString());
                temp.setComment(editText_route_comment.getText().toString());
                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/addRoute");
                myThread1.setWhat(3);
                myThread1.setRoute(temp);
                myThread1.start();

                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(RouteConfirmActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",((User)bundle.getSerializable("user")));
                i.putExtras(intent_bundle);
                startActivity(i);

            }
        });
    }
}
