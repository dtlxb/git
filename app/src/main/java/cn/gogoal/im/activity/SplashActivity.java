package cn.gogoal.im.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.servise.AppBackServise;
import cn.gogoal.im.ui.view.SkipProgressView;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SplashActivity extends Activity {

    @BindView(R.id.img_splash)
    ImageView imgSplash;

    @BindView(R.id.skip_view)
    SkipProgressView skipProgressView;

    private ServiceConnection sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        bineServise();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Glide.with(this).load(R.mipmap.splash).centerCrop().into(imgSplash);

        skipProgressView.setTimeMillis(5000);
        skipProgressView.start();
        skipProgressView.setProgressListener(new SkipProgressView.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress==0){
                    go2Next();
                }
            }
        });
        skipProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Next();
            }
        });


    }

    private void go2Next() {
        if (UserUtils.isLogin()){
            NormalIntentUtils.go2MainActivity(SplashActivity.this);
        }else {
            NormalIntentUtils.go2LoginActivity(SplashActivity.this);
        }

        finish();
    }

    /**
     * 绑定后台服务
     */
    private void bineServise() {

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final AppBackServise.AppBackBinder sBinder = (AppBackServise.AppBackBinder) service;
                sBinder.ggAutoLogin(SplashActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        bindService(new Intent(SplashActivity.this, AppBackServise.class), sc, Context.BIND_AUTO_CREATE);
    }

    /***/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }
}
