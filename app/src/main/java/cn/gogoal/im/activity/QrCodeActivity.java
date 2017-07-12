package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hply.qrcode_lib.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.MD5Utils;
import cn.gogoal.im.common.SaveBitmapAsyncTask;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import cn.gogoal.im.common.permission.PermisstionCode;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_CAMERA_PERM;

/**
 * author wangjd on 2017/6/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :我的二维码
 */
public class QrCodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.layout_main)
    NestedScrollView scrollView;

    @BindView(R.id.iv_my_qrcode_avatar)
    ImageView ivQrcodeAvatar;

    @BindView(R.id.tv_my_qqrcode_name)
    TextView tvMyQqrcodeName;

    @BindView(R.id.tv_my_qqrcode_duty)
    TextView tvMyQqrcodeDuty;

    @BindView(R.id.appCompatImageView)
    AppCompatImageView ivQrCode;

    @BindView(R.id.btn_my_qrcode_save_qrcode)
    SelectorButton btnMyQrcodeSaveQrcode;

    @BindView(R.id.btn_my_qrcode_share_qrcode)
    SelectorButton btnMyQrcodeShareQrcode;


    private Bitmap qrCodeBitmap;

    private int codeType;//二维码类型，群的，还是个人的
    private String accountId;

    private int updataCode;

    @Override
    public int bindLayout() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void doBusiness(final Context mContext) {

        /*
         * @see GGQrCode.QR_CODE_TYPE_GROUP,GGQrCode.QR_CODE_TYPE_GROUP
         * */
        codeType = getIntent().getIntExtra("qr_code_type", 0);

        setMyTitle(codeType == 0 ? "我的二维码" : "群二维码", true);

        String name = getIntent().getStringExtra("qrcode_name");
        String userInfo = getIntent().getStringExtra("qrcode_info");

        accountId = getIntent().getStringExtra("qrcode_content_id");

        tvMyQqrcodeName.setText(StringUtils.isActuallyEmpty(name) ? "--" : name);
        tvMyQqrcodeDuty.setText(StringUtils.isActuallyEmpty(userInfo) ? "--" : userInfo);

        //异步处理二维码 bitmap
        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                switch (codeType) {
                    case GGQrCode.QR_CODE_TYPE_PERSIONAL://个人二维码
                        if (accountId.equalsIgnoreCase(UserUtils.getMyAccountId())) {//我自己的二维码
                            UserUtils.getUserAvatar(new Impl<Bitmap>() {
                                @Override
                                public void response(int code, Bitmap data) {
                                    if (code == 0)
                                        configQrCodeBitmap(data);
                                    else
                                        configQrCodeBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
                                }
                            });
                        } else {
                            UserUtils.getUserInfo(accountId, new Impl<String>() {//别人的二维码
                                @Override
                                public void response(int code, String data) {
                                    //拿别人头像
                                    if (code == 0) {
                                        ImageUtils.getUrlBitmap(getActivity(),
                                                JSONObject.parseObject(data).getString("avatar"), new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                        configQrCodeBitmap(resource);
                                                    }
                                                });
                                    } else {
                                        configQrCodeBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
                                    }
                                }
                            });
                        }
                        break;
                    case GGQrCode.QR_CODE_TYPE_GROUP://群二维码
                        ChatGroupHelper.setGroupAvatar(accountId, new AvatarTakeListener() {
                            @Override
                            public void success(Bitmap bitmap) {
                                configQrCodeBitmap(bitmap);
                            }

                            @Override
                            public void failed(Exception e) {
                                configQrCodeBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
                            }
                        });
                        break;
                }

            }

            @Override
            public void onPostExecute() {
                ivQrCode.setImageBitmap(qrCodeBitmap);
            }
        });
    }

    public void configQrCodeBitmap(final Bitmap avatar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (avatar != null)
                    ivQrcodeAvatar.setImageBitmap(avatar);
            }
        });

        qrCodeBitmap = CodeUtils.createImage(
                getActivity(),//上下文
                GGQrCode.getQrcodeString(codeType, accountId),//内容
                400,//尺寸
                avatar == null ? BitmapFactory.decodeResource(getResources(), R.mipmap.logo) :
                        avatar);//内嵌logo
    }

    @OnClick({R.id.btn_my_qrcode_save_qrcode, R.id.btn_my_qrcode_share_qrcode})
    void click(final View view) {
        switch (view.getId()) {
            case R.id.btn_my_qrcode_save_qrcode:
                saveQrCode();
                break;
            case R.id.btn_my_qrcode_share_qrcode:
                btnMyQrcodeShareQrcode.setClickable(false);
                share();
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        final WaitDialog dialog = WaitDialog.getInstance("请稍后...", R.mipmap.login_loading, true);
        dialog.show(getSupportFragmentManager());

        new AsyncTask<Bitmap, Void, Integer>() {
            @Override
            protected Integer doInBackground(Bitmap... params) {
                /*1.*/
                ImageUtils.cacheImage(getActivity(),
                        params[0],
                        MD5Utils.getMD5EncryptyString32(String.valueOf(System.currentTimeMillis())) + ".png",
                        new Impl<String>() {
                            @Override
                            public void response(int code, String data) {
                                updataCode = code;
                                if (code == 0) {

                                    GGShareEntity entity = new GGShareEntity();
                                    entity.setShareType(GGShareEntity.SHARE_TYPE_IMAGE);
                                    entity.setImage(data);

                                    Intent intent = new Intent(QrCodeActivity.this, ShareMessageActivity.class);
                                    intent.putExtra("share_web_data", entity);
                                    startActivity(intent);

                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    UIHelper.toast(getActivity(), "分享出错！");
                                }
                                btnMyQrcodeShareQrcode.setClickable(true);
                            }
                        });
                return updataCode;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }
        }.execute(ImageUtils.screenshot(scrollView));


    }

    @AfterPermissionGranted(PermisstionCode.WRITE_EXTERNAL_STORAGE)
    public void saveQrCode() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new SaveBitmapAsyncTask(
                    getActivity(),
                    "qr_code_" + MD5Utils.getMD5EncryptyString16(StringUtils.isActuallyEmpty(accountId) ?
                            String.valueOf(System.currentTimeMillis()) : accountId) + ".png",
                    "二维码")
                    .execute(ImageUtils.screenshot(scrollView));
        } else {
            EasyPermissions.requestPermissions(this, "需要请求存储图片权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            UIHelper.toast(getActivity(), "需要请求存储图片权限,请到设置中开启");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请需要请求存储图片权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }
}
