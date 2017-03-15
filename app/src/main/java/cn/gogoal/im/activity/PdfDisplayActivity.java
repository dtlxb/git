package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 */

public class PdfDisplayActivity extends BaseActivity {

//    @BindView(R.id.pdf_view)
//    PDFView pdfView;

    /* 进度条对话框 */
    private ProgressDialog pdialog;

    private String pdfUrl;

    @Override
    public int bindLayout() {
        return R.layout.activity_pdf_display;
    }

    @Override
    public void doBusiness(Context mContext) {
//        if (StatusBarUtil.with(this).isOperableDevice()){
//            StatusBarUtil.with(this).setStatusBarFontDark(true);
//        }else {
//            StatusBarUtil.with(this).setColor(Color.BLACK);
//        }
//        String jsonData = getIntent().getStringExtra("pdf_data");
//        if (!TextUtils.isEmpty(jsonData)) {
//            try {
//                pdfUrl = JSONObject.parseObject(jsonData).getString("pdfUrl");
//                setMyTitle(JSONObject.parseObject(jsonData).getString("title"), true);
//                new MyLoadAsyncTask().execute(pdfUrl);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

  /*  @Nullable
    @Override
    protected Dialog onCreateDialog(int id) {
        *//* 实例化进度条对话框 *//*
        pdialog = new ProgressDialog(this);
        *//* 进度条对话框属性设置 *//*
        pdialog.setMessage("加载中...");
        *//* 进度值最大100 *//*
        pdialog.setMax(100);
        *//* 水平风格进度条 *//*
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        *//* 无限循环模式 *//*
        pdialog.setIndeterminate(false);
        *//* 可取消 *//*
        pdialog.setCancelable(true);
        *//* 显示对话框 *//*
        pdialog.show();
        return pdialog;
    }

    *//* 异步任务，后台处理与更新UI *//*
    class MyLoadAsyncTask extends AsyncTask<String, String, String> {

        *//* 后台线程 *//*
        @Override
        protected String doInBackground(String... params) {
            *//* 所下载文件的URL *//*
            InputStream in = null;
            FileOutputStream out = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                *//* URL属性设置 *//*
                conn.setRequestMethod("GET");
                *//* URL建立连接 *//*
                conn.connect();
                *//* 下载文件的大小 *//*
                int fileOfLength = conn.getContentLength();
                *//* 每次下载的大小与总下载的大小 *//*
                int totallength = 0;
                int length = 0;
                *//* 输入流 *//*
                in = conn.getInputStream();
                *//* 输出流 *//*
                out = new FileOutputStream(new File(getExternalFilesDir("cachePdf"),
                        MD5Utils.getMD5EncryptyString(pdfUrl)));//命名

                *//* 缓存模式，下载文件 *//*
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

        *//* 预处理UI线程 *//*
        @Override
        protected void onPreExecute() {
            showDialog(0);
            super.onPreExecute();
        }

        *//* 结束时的UI线程 *//*
        @Override
        protected void onPostExecute(String result) {
            dismissDialog(0);
            super.onPostExecute(result);

            pdfView.fromFile(new File(getExternalFilesDir("cachePdf").getPath(), MD5Utils.getMD5EncryptyString(pdfUrl)))
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(PdfDisplayActivity.this))
                    .enableAnnotationRendering(true)
                    .load();

        }

        *//* 处理UI线程，会被多次调用,触发事件为publicProgress方法 *//*
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
    }*/
}
