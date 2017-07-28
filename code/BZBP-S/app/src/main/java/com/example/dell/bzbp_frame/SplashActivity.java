package com.example.dell.bzbp_frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent i = new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(i);
    }
}
