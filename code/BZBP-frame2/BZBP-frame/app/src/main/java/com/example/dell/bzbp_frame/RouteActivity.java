package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.bzbp_frame.model.Route;

public class RouteActivity extends AppCompatActivity {

    private Button mCameraButton;
    private Button mMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.button_main_route_end).setClickable(true);

        final Route mRoute = new Route();
        Bundle bundle = new Bundle();
        bundle.putSerializable("route",mRoute);

        mCameraButton = (Button) findViewById(R.id.button_main_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteActivity.this,CameraActivity.class);
                startActivity(i);
            }
        });
    }

}
