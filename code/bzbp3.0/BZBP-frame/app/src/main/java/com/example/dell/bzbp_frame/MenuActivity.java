package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Bundle temp_bundle = this.getIntent().getExtras();
        //final Bundle new_bundle = new Bundle();
        //User user = new User();
        //new_bundle.putSerializable("user",((User)temp_bundle.getSerializable("user")));

        final Bundle bundle = this.getIntent().getExtras();
/*
        this.findViewById(R.id.button_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MenuActivity.this,TestActivity.class);
                Bundle intent_bundle = new Bundle();
                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
*/

        this.findViewById(R.id.button_menu_search_posto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MenuActivity.this,SearchPostoActivity.class);
                Bundle intent_bundle = new Bundle();

                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                intent_bundle.putDouble("latitude",bundle.getDouble("latitude"));
                intent_bundle.putDouble("longitude",bundle.getDouble("longitude"));
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        this.findViewById(R.id.button_menu_search_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MenuActivity.this,SearchRouteActivity.class);
                Bundle intent_bundle = new Bundle();

                intent_bundle.putSerializable("user",bundle.getSerializable("user"));
                //intent_bundle.putDouble("latitude",bundle.getDouble("latitude"));
                //intent_bundle.putDouble("longitude",bundle.getDouble("longitude"));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}
