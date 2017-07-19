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

public class LoginActivity extends AppCompatActivity {

    private Handler hh=new Handler();
    public static User thisuser;

    public static String message;
  //  public static String ip="192.168.1.105:8080/BookStore";
    public static String ip="192.168.1.97:8080/BookStore";
    private List<User> users=new ArrayList<User>();

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

        setContentView(R.layout.activity_login);




        this.findViewById(R.id.button_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //改到了main。为了方便不登录就测试功能。
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                //Intent i = new Intent(LoginActivity.this,MainActivity.class);
                //Bundle b = new Bundle();
                //b.putSerializable("user",new User(-1,"developer","d","d"));
                //i.putExtras(b);
                startActivity(i);
            }
        });

        this.findViewById(R.id.button_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String temp_username=((EditText)findViewById(R.id.edittext_login_username)).getText().toString();
                String temp_password=((EditText)findViewById(R.id.edittext_login_password)).getText().toString();

                if(temp_username.equals("")||temp_password.equals(""))
                {
                    msgbox("不能为空");
                    return;
                }

                thisuser=new User();
                thisuser.setUsername(temp_username);
                thisuser.setPassword(temp_password);

                MyThread myThread1 = new MyThread();
                myThread1.setGetUrl("http://"+ip+"/rest/checkUser");
                myThread1.setUser(thisuser);
                myThread1.setWhat(1);
                myThread1.start();
                try {
                    myThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String result = myThread1.getResult();
                Integer result_int=Integer.parseInt(result);
                if(result_int<=0){
                    msgbox("用户名或密码错误");
                } else{
                    thisuser.setId(result_int);
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",thisuser);
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }

            }
        });
    }
}
