package cn.gogoal.im.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.R;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.LitePalDBHelper;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.servise.AppBackServise;
import cn.gogoal.im.servise.MessageSaveService;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SplashActivity extends Activity {

    @BindView(R.id.img_splash)
    ImageView imgSplash;

    private ServiceConnection sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        startService(new Intent(SplashActivity.this, MessageSaveService.class));

        bineServise();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageDisplay.loadImage(this, R.mipmap.splash, imgSplash);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                go2Next();
            }
        }, 1000);

    }

    private void go2Next() {
        if (UserUtils.isLogin()) {
            NormalIntentUtils.go2MainActivity(SplashActivity.this);
            //建立数据库
            LitePalDBHelper.getInstance().createSQLite(UserUtils.getUserId());
        } else {
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

                sBinder.initData();
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
