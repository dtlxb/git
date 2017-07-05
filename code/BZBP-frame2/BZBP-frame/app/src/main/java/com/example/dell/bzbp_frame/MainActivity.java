package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.bzbp_frame.model.User;

public class MainActivity extends AppCompatActivity {

    private Button mMenuButton;
    private Button mCameraButton;
    private Button mStartRouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final User user = new User();
        user.setUsername("wjb");
        user.setPassword("123");

        mMenuButton = (Button) findViewById(R.id.button_main_menu);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MenuActivity.class);
                Bundle bundle = new Bundle();
                User temp = new User();
                temp = user;
                bundle.putSerializable("user",temp);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        mCameraButton = (Button) findViewById(R.id.button_main_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CameraActivity.class);
                Bundle bundle = new Bundle();
                User temp = new User();
                temp = user;
                bundle.putSerializable("user",temp);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        mStartRouteButton = (Button) findViewById(R.id.button_main_route_start);
        mStartRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,RouteActivity.class);
                startActivity(i);
            }
        });
    }


}
