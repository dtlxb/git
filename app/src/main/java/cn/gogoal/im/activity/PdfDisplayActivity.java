package cn.gogoal.im.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSONObject;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.MD5Utils;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 */

public class PdfDisplayActivity extends BaseActivity {

    @BindView(R.id.pdf_view)
    PDFView pdfView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    /* 进度条对话框 */
    private ProgressDialog pdialog;

    private String pdfUrl;

//    http://rlrw.bnu.edu.cn/NewsImage/2012410100744.pdf
    @Override
    public void setOrientation() {
        // do nothing
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_pdf_display;
    }

    @Override
    public void doBusiness(Context mContext) {

        String jsonData = getIntent().getStringExtra("pdf_data");

        KLog.e(jsonData);

        if (!TextUtils.isEmpty(jsonData)) {
            try {
                pdfUrl = JSONObject.parseObject(jsonData).getString("pdfUrl");
                setMyTitle(JSONObject.parseObject(jsonData).getString("title"), true);

                showPdf();

            } catch (Exception e) {
                setMyTitle("", true);
                e.printStackTrace();
            }
        } else {
            setMyTitle("", true);
        }

    }

    private void showPdf() {
//        0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
        switch (AppDevice.getNetworkType(getActivity())) {
            case 0:
                xLayout.setStatus(XLayout.No_Network);
                break;
            case 1:
//                new MyLoadAsyncTask().execute(pdfUrl);
//                break;
            case 2:
            case 3:
                new AlertDialog.Builder(this, R.style.HoloDialogStyle).setTitle("提示")
                        .setMessage("当前处于非WI-FI环境，继续查看将消耗运营商流量，请确认是否继续")
                        .setPositiveButton("继续,有的是流量", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyLoadAsyncTask().execute(pdfUrl);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
                break;
        }
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id) {
//         实例化进度条对话框
        pdialog = new ProgressDialog(this);
        pdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (pdialog.getProgress() < 95) {
                    FileUtil.deleteDir(getExternalFilesDir("cachePdf"));
                    finish();
                }
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
//         进度条对话框属性设置
        pdialog.setMessage("加载中...");
//         进度值最大100
        pdialog.setMax(100);
//         水平风格进度条
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//         无限循环模式
        pdialog.setIndeterminate(false);
//         可取消
        pdialog.setCancelable(true);
//         显示对话框
        pdialog.show();
        return pdialog;
    }

    //     异步任务，后台处理与更新UI
    private class MyLoadAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
//             所下载文件的URL
            InputStream in = null;
            FileOutputStream out = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                 URL属性设置
                conn.setRequestMethod("GET");
//                 URL建立连接
                conn.connect();
//                 下载文件的大小
                int fileOfLength = conn.getContentLength();
//                 每次下载的大小与总下载的大小
                int totallength = 0;
                int length = 0;
//                 输入流
                in = conn.getInputStream();
//                 输出流
                out = new FileOutputStream(new File(getExternalFilesDir("cachePdf"),
                        MD5Utils.getMD5EncryptyString32(pdfUrl)));//命名

//                 缓存模式，下载文件
                byte[] buff = new byte[1024 * 1024];
                while ((length = in.read(buff)) > 0) {
                    totallength += length;
                    String str1 = "" + ((totallength * 100) / fileOfLength);
                    publishProgress(str1);
                    out.write(buff, 0, length);
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {//关闭流
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
            }
            return null;
        }

        //         预处理UI线程
        @Override
        protected void onPreExecute() {
            showDialog(0);
            super.onPreExecute();
        }

        //         结束时的UI线程
        @Override
        protected void onPostExecute(String result) {
            try {
                dismissDialog(0);
            } catch (Exception e) {

            }
            super.onPostExecute(result);

            pdfView.fromFile(new File(getExternalFilesDir("cachePdf").getPath(), MD5Utils.getMD5EncryptyString32(pdfUrl)))
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(PdfDisplayActivity.this))
                    .enableAnnotationRendering(true)
                    .load();

        }

        //         处理UI线程，会被多次调用,触发事件为publicProgress方法
        @Override
        protected void onProgressUpdate(String... values) {
            //进度显示
            pdialog.setProgress(Integer.parseInt(values[0]));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = getExternalFilesDir("cachePdf");
        FileUtil.deleteDir(file);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pdialog.isShowing()) {
                pdialog.dismiss();
                finish();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
