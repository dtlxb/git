package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.bzbp_frame.model.User;

public class MenuActivity extends AppCompatActivity {

    private Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        User user = new User();
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        mLoginButton = (Button) findViewById(R.id.button_menu_login);
        mLoginButton.setText(user.getUsername());
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
