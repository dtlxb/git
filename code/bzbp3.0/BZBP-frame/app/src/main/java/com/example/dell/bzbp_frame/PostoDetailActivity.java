package com.example.dell.bzbp_frame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;

public class PostoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_detail);
        Bundle bundle = this.getIntent().getExtras();
        ((TextView)this.findViewById(R.id.postodetail_name)).setText(((Posto)bundle.getSerializable("posto")).getName());
    }
}
