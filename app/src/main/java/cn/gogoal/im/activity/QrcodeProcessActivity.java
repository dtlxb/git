package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.hply.qrcode_lib.activity.CodeUtils;
import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_CAMERA_PERM;
import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_CODE;
import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_LOCATION_PERM;

/**
 * author wangjd on 2017/6/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :二维码扫码和处理页面，中转
 */
public class QrcodeProcessActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @Override
    public int bindLayout() {
        return R.layout.layout_transparent;
    }

    @Override
    public void doBusiness(Context mContext) {
        cameraTask();
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            onClick();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    private void onClick() {
        Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERM:
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    new AppSettingsDialog.Builder(this, "当前App需要申请相机权限,需要打开设置页面么?")
                            .setTitle("权限申请")
                            .setPositiveButton("确认")
                            .setNegativeButton("取消", null /* click listener */)
                            .setRequestCode(REQUEST_CAMERA_PERM)
                            .build()
                            .show();
                }

                break;

            case REQUEST_LOCATION_PERM:
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    new AppSettingsDialog.Builder(this, "当前App需要申请定位权限,需要打开设置页面么?")
                            .setTitle("权限申请")
                            .setPositiveButton("确认")
                            .setNegativeButton("取消", null /* click listener */)
                            .setRequestCode(REQUEST_LOCATION_PERM)
                            .build()
                            .show();
                }
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        try {
            KLog.e("==========" + data.getExtras().getString(CodeUtils.RESULT_STRING) + "===========");
        } catch (Exception e) {
            e.getMessage();
        }

        KLog.e("requestCode==" + requestCode);

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    KLog.e(result);

                    String codeBody = GGQrCode.getUserQrCodeBody(result);

                    KLog.e(codeBody);

                    try {
                        final JSONObject scanBody = JSONObject.parseObject(codeBody);

//                        KLog.e(scanBody.getString("qrType"));
//                        KLog.e(scanBody.getString("account_id"));

                        if (scanBody.getIntValue("qrType") == 0) {
                            //TODO 跳转个人详情
                            NormalIntentUtils.go2PersionDetail(getActivity(),
                                    scanBody.getString("account_id"));
                            QrcodeProcessActivity.this.finish();
                        } else {
                            //TODO 跳转群名片
                            ChatGroupHelper.getGroupInfo(scanBody.getString("conv_id"), new Impl<String>() {
                                @Override
                                public void response(int code, String data) {
                                    switch (code) {
                                        case Impl.RESPON_DATA_SUCCESS:
                                            GroupData groupData = JSONObject.parseObject(data, GroupData.class);
                                            Intent in = new Intent(getActivity(), SquareCardActivity.class);
                                            in.putExtra("conversation_id", groupData.getConv_id());
                                            in.putExtra("square_name", groupData.getName());
                                            in.putExtra("square_creater", groupData.getC());
                                            in.putParcelableArrayListExtra("square_members", groupData.getM_info());
                                            QrcodeProcessActivity.this.startActivity(in);
                                            QrcodeProcessActivity.this.finish();
                                            break;

                                        default:
                                            KLog.e("解析::" + data);
                                            UIHelper.toast(getActivity(),"暂无相关信息");
                                            break;
                                    }
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.getMessage();
                        KLog.e("结果不是json，或者非GoGoal APP系二维码");
                        UIHelper.toast(getActivity(), "非GoGoal二维码");
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    UIHelper.toast(getActivity(), "解析出错");
                }
            }
        }
        QrcodeProcessActivity.this.finish();
    }
}
