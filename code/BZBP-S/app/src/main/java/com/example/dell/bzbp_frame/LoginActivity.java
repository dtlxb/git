package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

public class LoginActivity extends BaseActivity {

    public static String ip;
    private Button button_login_register;
    private Button button_login_login;
    private EditText editText_login_username;
    private EditText editText_login_password;

    @Override
    protected void initView() {
        // 用来出来初始化视图
        // 别忘了setContentView(R.layout.XXX);加载布局文件
        // findViewByid
        setContentView(R.layout.activity_login);

        button_login_register = (Button) findViewById(R.id.button_login_register);
        button_login_login = (Button) findViewById(R.id.button_login_login);
        editText_login_username = (EditText) findViewById(R.id.edittext_login_username);
        editText_login_password = (EditText) findViewById(R.id.edittext_login_password);
    }

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
        // 用来初始化数据以及处理数据
    }

    @Override
    protected void initListener() {
        // 用来给控件添加监听器，可以在里面写如btn.setonClickListener(){}; .....等监听器。
        button_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //改到了main。为了方便不登录就测试功能。
                //Intent i = new Intent(LoginActivity.this,MainActivity.class);
                //Bundle b = new Bundle();
                //b.putSerializable("user",new User(-1,"developer","d","d"));
                //i.putExtras(b);
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        this.findViewById(R.id.button_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //获取输入的用户名和密码
                String username=editText_login_username.getText().toString();
                String password=editText_login_password.getText().toString();
                //判断输入是否为空
                if(username.equals("")||password.equals("")) {
                    msgbox("不能为空");
                    return;
                }
                //将用户名和密码包装成USER提交
                User thisuser = new User();
                thisuser.setUsername(username);
                thisuser.setPassword(password);

                //MyThread myThread1 = new MyThread();
                //submit(myThread1,ip+"/rest/checkUser",thisuser,1);

                //登录成功返回用户数字ID，否则返回-1
                //String result = myThread1.getResult();
                String result = "1";
                Integer result_int=Integer.parseInt(result);

                if(result_int<=0){
                    msgbox("用户名或密码错误");
                }else{
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

    @Override
    protected void doBusiness(Bundle savedInstanceState) {

    }
}
