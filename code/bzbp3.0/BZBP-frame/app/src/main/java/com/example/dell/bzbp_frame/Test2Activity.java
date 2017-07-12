package com.example.dell.bzbp_frame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;

import java.text.SimpleDateFormat;

public class Test2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        final Bundle bundle = this.getIntent().getExtras();
        Posto posto = (Posto) bundle.getSerializable("temp");

        TextView test2_name = (TextView) findViewById(R.id.test2_name);
        TextView test2_comment = (TextView) findViewById(R.id.test2_comment);
        TextView test2_date = (TextView) findViewById(R.id.test2_date);
        TextView test2_rid = (TextView) findViewById(R.id.test2_rid);

        test2_name.setText(posto.getName());
        test2_comment.setText(posto.getComment());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(posto.getDate());
        test2_date.setText(dateString);
        test2_rid.setText(posto.getBelong_rid()+"");

    }
}
