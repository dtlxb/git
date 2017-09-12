package com.example.dell.bzbp_frame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.bzbp_frame.tool.FileUtils;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;

import java.io.File;

public class AblumActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;

    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int TAKE_PHOTO_CODE = 8;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    public static final int ACTION_STICKERS_IMAGE = 10;
    private AblumActivity context;
    private ImageView imgView;
    private View openAblum;
    private View editImage;//
    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;//
    private String path;


    private View mTakenPhoto;//拍摄照片用于编辑
    private Uri photoURI = null;
    private void initView() {
        context = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        imgView = (ImageView) findViewById(R.id.img);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button button_main_camera =  (Button) findViewById(R.id.button_main_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editImageClick();
            }
        });
        button_main_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAblum();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 从相册选择编辑图片
     */
    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck();
        } else {
            openAblum();
        }//end if
    }

    private void openAblum() {
        AblumActivity.this.startActivityForResult(new Intent(
                        AblumActivity.this, SelectPictureActivity.class),
                SELECT_GALLERY_IMAGE_CODE);
    }

    private void openAblumWithPermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        openAblum();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE://
                    handleSelectFromAblum(data);
                    break;

                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }// end switch
        }
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit){
            Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
        }else{//未编辑  还是用原来的图片
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);;
        }
        //System.out.println("newFilePath---->" + newFilePath);
        //File file = new File(newFilePath);
        //System.out.println("newFilePath size ---->" + (file.length() / 1024)+"KB");
        Log.d("image is edit", isImageEdit + "");
        LoadImageTask loadTask = new LoadImageTask();
        loadTask.execute(newFilePath);
    }
    private void editImageClick() {
        File outputFile = FileUtils.genEditFile();
        EditImageActivity.start(this,path,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
    }
    private void handleSelectFromAblum(Intent data) {
        String filepath = data.getStringExtra("imgPath");
        path = filepath;
        // System.out.println("path---->"+path);
        startLoadTask();
    }

    private void startLoadTask() {
        LoadImageTask task = new LoadImageTask();
        task.execute(path);
    }
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth / 4, imageHeight / 4);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mainBitmap != null) {
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
            imgView.setImageBitmap(mainBitmap);
        }
    }// end inner class
}

