package com.example.dell.bzbp_frame;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Handler hh=new Handler();
    public static User thisuser;


    private List<User> users=new ArrayList<User>();

    public static String ip="192.168.1.97:8080/BookStore";

    public void msgbox(String msg)
    {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        this.findViewById(R.id.button_register_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        this.findViewById(R.id.button_register_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String temp_username=((EditText)findViewById(R.id.edittext_register_username)).getText().toString();
                String temp_password=((EditText)findViewById(R.id.edittext_register_password)).getText().toString();

                if(temp_username.equals("")||temp_password.equals(""))
                {
                    msgbox("不能为空");
                    return;
                }

                thisuser=new User();
                thisuser.setUsername(temp_username);
                thisuser.setPassword(temp_password);

                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/addUser");
                myThread1.setUser(thisuser);
                myThread1.setWhat(1);
                myThread1.start();
                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                msgbox( myThread1.getResult());

                String result = myThread1.getResult();
                //msgbox(result);

                if(result.equals("fail")){
                    msgbox("用户名重复");
                } else{
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",thisuser);
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
            }
        });
    }
}

