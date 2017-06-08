package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hply.qrcode_lib.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.Bean;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import cn.gogoal.im.common.permission.PermisstionCode;
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
public class MyQrCodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.layout_main)
    NestedScrollView scrollView;

    @BindView(R.id.iv_my_qrcode_avatar)
    ImageView ivMyQrcodeAvatar;

    @BindView(R.id.tv_my_qqrcode_name)
    TextView tvMyQqrcodeName;

    @BindView(R.id.tv_my_qqrcode_duty)
    TextView tvMyQqrcodeDuty;

    @BindView(R.id.appCompatImageView)
    AppCompatImageView ivQrCode;
    private Bitmap qrCodeBitmap;
    private Bitmap userAvatarBitmap;

    @Override
    public int bindLayout() {
        return R.layout.activity_my_qrcode;
    }

    @Override
    public void doBusiness(final Context mContext) {
        setMyTitle("我的二维码", true);

        UserUtils.getUserAvatar(new Impl<Bitmap>() {
            @Override
            public void response(boolean success, Bitmap data) {
                ivMyQrcodeAvatar.setImageBitmap(data);
            }
        });
        tvMyQqrcodeName.setText(UserUtils.getNickname());
        tvMyQqrcodeDuty.setText(UserUtils.getDuty());

        Bean bean = new Bean();
        bean.setAccount_id(UserUtils.getMyAccountId());
        bean.setQrType(Bean.QR_CODE_TYPE_PERSIONAL);

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {
                UserUtils.getUserAvatar(new Impl<Bitmap>() {
                    @Override
                    public void response(boolean success, Bitmap data) {
                        if (success) {
                            userAvatarBitmap = data;
                        } else {
                            userAvatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                        }
                    }
                });

                qrCodeBitmap = CodeUtils.createImage(
                        mContext,//上下文
                        GGQrCode.getMyQrcodeString(),//内容
                        400,//尺寸
                        userAvatarBitmap);//内嵌logo
            }

            @Override
            public void onPostExecute() {
                ivQrCode.setImageBitmap(qrCodeBitmap);
            }
        });
    }

    @OnClick({R.id.btn_my_qrcode_save_qrcode, R.id.btn_my_qrcode_share_qrcode})
    void click(final View view) {
        switch (view.getId()) {
            case R.id.btn_my_qrcode_save_qrcode:
                saveQrCode();
                break;
            case R.id.btn_my_qrcode_share_qrcode:
                share();
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
//        Intent intent = new Intent(MyQrCodeActivity.this, ShareMessageActivity.class);
//        Bundle bundle = new Bundle();
//        GGShareEntity entity = new GGShareEntity();
//        entity.setShareType(GGShareEntity.SHARE_TYPE_IMAGE);
//        entity.setArg(qrCodeBitmap);
//        bundle.putParcelable("share_web_data", entity);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    @AfterPermissionGranted(PermisstionCode.WRITE_EXTERNAL_STORAGE)
    public void saveQrCode() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ImageUtils.saveImage2DCIM(ImageUtils.screenshot(scrollView), "my_qr_code" + System.currentTimeMillis() + ".png", new Impl<String>() {
                @Override
                public void response(boolean success, String data) {
                    UIHelper.toast(getActivity(), success ? "二维码已保存到相册" : "保存二维码出错，请重试");
                }
            });
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求存储图片权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            UIHelper.toast(getActivity(), "需要请求存储图片权限,请到设置中开启");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
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

    /**
     * 二维码插入logo
     */
    public Bitmap getQrCodeInnerBitmap(Context context, Bitmap logoBitmap) {

        int radius = AppDevice.dp2px(context, 10);

        int border = AppDevice.dp2px(context, 20);

        if (logoBitmap == null) {
            return null;
        }

        int size = Math.min(logoBitmap.getWidth(), logoBitmap.getHeight());

        Bitmap mBitmap = ImageUtils.drawableToBitmap(
                ImageUtils.getRoundedRectangleDrawable(
                        context,
                        Bitmap.createScaledBitmap(logoBitmap, size, size, true), radius));
        ;

        Bitmap result = Bitmap.createBitmap(
                size + border * 2,
                size + border * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(255, 255, 255, 255);
        canvas.drawBitmap(mBitmap, border, border, null);

        return ImageUtils.drawableToBitmap(
                ImageUtils.getRoundedRectangleDrawable(context, result, radius));

    }
}
