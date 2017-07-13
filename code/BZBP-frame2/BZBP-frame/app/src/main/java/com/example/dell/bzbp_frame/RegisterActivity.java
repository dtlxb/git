package com.example.dell.bzbp_frame;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.dell.bzbp_frame.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Handler hh=new Handler();
    public static User thisuser;


    private List<User> users=new ArrayList<User>();

    public static String ip="192.168.1.195:8080/BookStore";
    public void sendHttpPost(String getUrl, User user) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(user);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();

            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }

    }

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


        this.findViewById(R.id.button_register_register).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                String title=((EditText)findViewById(R.id.edittext_register_username)).getText().toString();
                String author=((EditText)findViewById(R.id.edittext_register_password)).getText().toString();

                if(title.equals("")||author.equals(""))
                {
                    msgbox("不能为空");
                    return;
                }
                //thisbook=new Book();
                //thisbook.setTitle(title);
                //thisbook.setAuthor(author);
                thisuser=new User();
                thisuser.setUsername(title);
                thisuser.setPassword(author);

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        sendHttpPost("http://"+ip+"/rest/addUser",thisuser);
                    }}).start();
                msgbox("seccuss");
            }
        });

    }
}