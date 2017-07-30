package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

public class RegisterActivity extends BaseActivity{

    private static User thisuser;
    private static String ip;
    private Button button_register_register;
    private Button button_register_cancel;
    private EditText editText_register_username;
    private EditText editText_register_password;

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        button_register_register = (Button) findViewById(R.id.button_register_register);
        button_register_cancel = (Button) findViewById(R.id.button_register_cancel);
        editText_register_username = (EditText) findViewById(R.id.edittext_register_username);
        editText_register_password = (EditText) findViewById(R.id.edittext_register_password);
    }

    @Override
    protected void initListener() {
        //取消注册
        button_register_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        //提交注册
        button_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //获取用户输入的用户名和密码
                String username = editText_register_username.getText().toString();
                String password = editText_register_password.getText().toString();
                //判断输入是否为空
                if(username.equals("")||password.equals(""))
                {
                    msgbox("不能为空");
                    return;
                }
                //将用户名和密码包装成USER提交
                thisuser=new User();
                thisuser.setUsername(username);
                thisuser.setPassword(password);

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
                //注册成功返回ID，否则返回-1
                String result = myThread1.getResult();
                Integer result_int=Integer.parseInt(result);
                if(result_int<=0){
                    msgbox("用户名重复");
                } else{
                    thisuser.setId(result_int);
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",thisuser);
                    i.putExtras(intent_bundle);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {

    }
}

