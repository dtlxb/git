package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hply.qrcode_lib.activity.CaptureFragment;
import com.hply.qrcode_lib.activity.CodeUtils;
import com.socks.library.KLog;

import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.ggqrcode.GGQrCode;

import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_IMAGE;

/**
 * author wangjd on 2017/6/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * description :扫码
 */
public class ScanQRCodeActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_scan_qrcode;
    }

    @Override
    public void doBusiness(Context mContext) {
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    public boolean isOpen = false;

    @OnClick({R.id.iv_scan_flashlight, R.id.iv_scan_open_gallery})
    void click(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_flashlight:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
            case R.id.iv_scan_open_gallery:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CodeUtils.REQUEST_IMAGE && data != null) {
            Uri uri = data.getData();
            try {
                CodeUtils.analyzeBitmap(ImageUtils.getImageAbsolutePath(getActivity(), uri), new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        String codeBody = GGQrCode.getUserQrCodeBody(result);

                        KLog.e(codeBody);

                        try {
                            final JSONObject scanBody = JSONObject.parseObject(codeBody);

                            if (scanBody.getIntValue("qrType") == 0) {
                                //TODO 跳转个人详情
                                NormalIntentUtils.go2PersionDetail(getActivity(),
                                        scanBody.getIntValue("account_id"));
                            } else {
                                //TODO 跳转群名片
                                ChatGroupHelper.getGroupInfo(scanBody.getString("conv_id"), new Impl<String>() {
                                    @Override
                                    public void response(int code, String data) {
                                        KLog.e(data);
                                        GroupData groupData = JSONObject.parseObject(data, GroupData.class);
                                        switch (code) {
                                            case Impl.RESPON_DATA_SUCCESS:
                                                Intent in = new Intent(getActivity(), SquareCardActivity.class);
                                                in.putExtra("conversation_id", groupData.getConv_id());
                                                in.putExtra("square_name", groupData.getName());
                                                in.putExtra("square_creater", groupData.getC());
                                                in.putParcelableArrayListExtra("square_members", groupData.getM_info());
                                                getActivity().startActivity(in);
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
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(getActivity(), "未发现二维啊(onAnalyze)", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "未发现二维啊(e)", Toast.LENGTH_LONG).show();
            }
        }
        ScanQRCodeActivity.this.finish();
    }
}
