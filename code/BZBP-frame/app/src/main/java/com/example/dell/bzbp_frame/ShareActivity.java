package com.example.dell.bzbp_frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bzbp_frame.model.User;

public class ShareActivity extends Activity {
    private TextView textview_address;
    private ImageView imageview_image;
    private Button mCancelButton;
    private Button mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();

        User user = new User();
        user = (User) bundle.getSerializable("user");

        String image_path = (String) bundle.getString("images");
        String image_address = "address:xxxxxx";

        Bitmap bitmap = BitmapFactory.decodeFile(image_path);

        textview_address = (TextView) findViewById(R.id.text_view_address) ;
        textview_address.setText(user.getUsername());
        imageview_image = (ImageView) findViewById(R.id.imageview_share_image);
        imageview_image.setImageBitmap(bitmap);

        final EditText editText=(EditText)findViewById(R.id.edittext_share_edit);

        mCancelButton = (Button) findViewById(R.id.button_share_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShareActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        mShareButton =(Button)findViewById(R.id.button_share_share);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShareActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
