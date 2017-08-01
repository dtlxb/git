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

public class RouteConfirmActivity extends BaseActivity {
    private Bundle bundle;
    private User user;
    private Route route;

    private TextView edittext_routeconfirm_name;
    private TextView edittext_routeconfirm_comment;
    private Button button_routeconfirm_cancel;
    private Button button_routeconfirm_confirm;
    public static String ip;

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
        bundle = this.getIntent().getExtras();
        //获取user
        user = (User)bundle.getSerializable("user");
        route = (Route)bundle.getSerializable("route");
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_route_confirm);
        edittext_routeconfirm_name = (EditText)findViewById(R.id.edittext_routeconfirm_name);
        edittext_routeconfirm_comment = (EditText)findViewById(R.id.edittext_routeconfirm_comment);
        button_routeconfirm_cancel = (Button)findViewById(R.id.button_routeconfirm_cancel);
        button_routeconfirm_confirm = (Button)findViewById(R.id.button_routeconfirm_confirm);
    }

    @Override
    protected void initListener() {
        button_routeconfirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteConfirmActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(intent_bundle);
                startActivity(i);
            }
        });


        button_routeconfirm_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------
                Route temp = new Route();
                //包装需要上传的route
                temp = route;
                temp.setName(edittext_routeconfirm_name.getText().toString());
                temp.setComment(edittext_routeconfirm_comment.getText().toString());
                MyThread myThread1 = new MyThread();
                submit(myThread1,ip+"/rest/addRoute",temp,3);

                Intent i = new Intent(RouteConfirmActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",user);
                i.putExtras(intent_bundle);
                startActivity(i);

            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {

    }
}
