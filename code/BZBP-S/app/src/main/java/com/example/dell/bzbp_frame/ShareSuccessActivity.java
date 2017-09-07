package com.example.dell.bzbp_frame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShareSuccessActivity extends BaseActivity {
    private TextView tv_sharesuccess_name;
    private TextView tv_sharesuccess_comment;
    private ImageView iv_sharesuccess_image;
    private Button button_sharesuccess_cancel;
    private Button button_sharesuccess_share;
    private Bundle bundle;
    private User user;
    private Bitmap bitmap;
    public static String ip;

    @Override
    protected void initData() {
        ip = this.getString(R.string.ipv4);
        bundle = this.getIntent().getExtras();
        //获取user
        user = (User)bundle.getSerializable("user");
        //获取照片在手机中的路径并生成bitmap
        //
        String image_path = (String) bundle.getString("share_image");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(image_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inSampleSize =2;
        bitmap = BitmapFactory.decodeStream(fis,null,opts);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_share_success);
        tv_sharesuccess_name = (TextView)findViewById(R.id.text_view_sharesuccess_name);
        tv_sharesuccess_comment = (TextView)findViewById(R.id.text_view_sharesuccess_comment);
        iv_sharesuccess_image = (ImageView)findViewById(R.id.imageview_sharesuccess_image);
        button_sharesuccess_share = (Button)findViewById(R.id.button_sharesuccess_share);
        button_sharesuccess_cancel = (Button)findViewById(R.id.button_sharesuccess_cancel);

    }

    @Override
    protected void initListener() {
        button_sharesuccess_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        button_sharesuccess_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bundle.getBoolean("is_in_route")){
                    Intent i = new Intent(ShareSuccessActivity.this,RouteActivity.class);
                    Bundle intent_bundle = new Bundle();
                    //user还是之前的user
                    intent_bundle.putSerializable("user",user);
                    //route对象的pid list里加进本次新的pid
                    intent_bundle.putInt("last_pid",bundle.getInt("last_pid"));

                    intent_bundle.putSerializable("route",(Route)bundle.getSerializable("route"));
                    i.putExtras(intent_bundle);
                    ShareSuccessActivity.this.finish();
                    startActivity(i);
                }else{
                    Intent i = new Intent(ShareSuccessActivity.this,MainActivity.class);
                    Bundle intent_bundle = new Bundle();
                    intent_bundle.putSerializable("user",((User)bundle.getSerializable("user")));
                    i.putExtras(intent_bundle);
                    ShareSuccessActivity.this.finish();
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void doBusiness(Bundle savedInstanceState) {
        tv_sharesuccess_name.setText("name:"+bundle.getString("share_name"));
        tv_sharesuccess_comment.setText("comment:"+bundle.getString("share_comment"));
        //显示图片
        iv_sharesuccess_image.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Warnning").setMessage("Are you sure left?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        ShareSuccessActivity.this.finish();
                    }
                }).show();
    }
}
