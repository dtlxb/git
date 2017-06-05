package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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
public class MyQrCodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.iv_my_qrcode_avatar)
    ImageView ivMyQrcodeAvatar;

    @BindView(R.id.tv_my_qqrcode_name)
    TextView tvMyQqrcodeName;

    @BindView(R.id.tv_my_qqrcode_duty)
    TextView tvMyQqrcodeDuty;

    @BindView(R.id.appCompatImageView)
    AppCompatImageView ivQrCode;
    private Bitmap qrCodeBitmap;

    @Override
    public int bindLayout() {
        return R.layout.activity_my_qrcode;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("我的二维码", true);

        UserUtils.getUserAvatar(new Impl<Bitmap>() {
            @Override
            public void response(boolean success, Bitmap data) {
                ivMyQrcodeAvatar.setImageBitmap(data);
            }
        });
        tvMyQqrcodeName.setText(UserUtils.getNickname());
        tvMyQqrcodeDuty.setText(UserUtils.getDuty());

        Bean bean=new Bean();
        bean.setAccount_id(UserUtils.getMyAccountId());
        bean.setQrType(Bean.QR_CODE_TYPE_PERSIONAL);

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {
                qrCodeBitmap = CodeUtils.createImage(
                        GGQrCode.getMyQrcodeString(),
                        400,
                        400,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
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

    /**分享*/
    private void share() {

    }

    @AfterPermissionGranted(PermisstionCode.WRITE_EXTERNAL_STORAGE)
    public void saveQrCode(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)) {
            ImageUtils.saveImage2DCIM(qrCodeBitmap, "my_qr_code.png", new Impl<String>() {
                @Override
                public void response(boolean success, String data) {
                    UIHelper.toast(getActivity(),success?"二维码已保存到相册":"保存二维码出错，请重试");
                }
            });
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求存储图片权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
}
