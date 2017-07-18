package com.example.dell.bzbp_frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Posto;

public class PostoDetailActivity extends AppCompatActivity {
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_detail);
        //获取bundle
        bundle = this.getIntent().getExtras();
        //获取posto
        Posto posto = (Posto) bundle.getSerializable("posto");

        //获取并显示图片
        Bitmap bit;
        String picture=posto.getImage();
        byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView) findViewById(R.id.postodetail_image)).setImageBitmap(bit);
        //显示poto信息

        ((TextView)this.findViewById(R.id.postodetail_name)).setText("name："+posto.getName());
        ((TextView)this.findViewById(R.id.postodetail_comment)).setText("comment："+posto.getComment());
        ((TextView)this.findViewById(R.id.postodetail_username)).setText("username："+posto.getUsername());
        ((TextView)this.findViewById(R.id.postodetail_date)).setText("date："+posto.getDate());
    }
}
