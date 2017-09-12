package com.example.dell.bzbp_frame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;

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
    private String title;
    private String comment;

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
                shareBigImg();
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
        tv_sharesuccess_name.setText(bundle.getString("share_name"));
        tv_sharesuccess_comment.setText(bundle.getString("share_comment"));
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

    public void showShareDialog() {

        ShareEntity testBean = new ShareEntity(bundle.getString("share_name"), bundle.getString("share_comment"));

        testBean.setImgUrl(bundle.getString("share_image"));
        ShareUtil.showShareDialog(ShareSuccessActivity.this, testBean, ShareConstant.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 分享回调处理
         */
        if (requestCode == ShareConstant.REQUEST_CODE) {
            if (data != null) {
                int channel = data.getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1);
                int status = data.getIntExtra(ShareConstant.EXTRA_SHARE_STATUS, -1);
                onShareCallback(channel, status);
            }
        }
    }
    private void onShareCallback(int channel, int status) {
        new ShareCallBack().onShareCallback(channel, status);
    }
    public void shareBigImg() {
        ShareEntity testBean = new ShareEntity("", "");
        testBean.setShareBigImg(true);
        // testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png"); // 网络地址


        /** 如果你要分享的图片是Bitmap，你可以如下使用 **/

        String filePath = ShareUtil.saveBitmapToSDCard(this, getWxShareBitmap(bitmap));
        testBean.setImgUrl(filePath);

        int channel = ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_QQ;
        ShareUtil.showShareDialog(this, channel, testBean, ShareConstant.REQUEST_CODE);
    }
    public void startShare() {
        ShareEntity testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
        testBean.setUrl("https://www.baidu.com"); //分享链接
        testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png");
        ShareUtil.startShare(ShareSuccessActivity.this, ShareConstant.SHARE_CHANNEL_QQ, testBean, ShareConstant.REQUEST_CODE);
    }
    protected Bitmap getWxShareBitmap(Bitmap targetBitmap) {
        float scale = Math.min((float) 150 / targetBitmap.getWidth(), (float) 150 / targetBitmap.getHeight());
        Bitmap fixedBmp = Bitmap.createScaledBitmap(targetBitmap, (int) (scale * targetBitmap.getWidth()), (int) (scale * targetBitmap.getHeight()), false);
        return fixedBmp;
    }
}
