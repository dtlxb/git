package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hply.qrcode_lib.activity.CaptureFragment;
import com.hply.qrcode_lib.activity.CodeUtils;

import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

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

    @OnClick({R.id.iv_scan_flashlight})
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
//            setResult(RESULT_OK, resultIntent);
//            finish();
            Toast.makeText(ScanQRCodeActivity.this, "解析结果=" + result, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);

            setResult(RESULT_OK, resultIntent);
//            finish();

            Toast.makeText(ScanQRCodeActivity.this, "解析出错", Toast.LENGTH_SHORT).show();
        }
    };

}
