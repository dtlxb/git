package cn.gogoal.im.activity;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.img_splash)
    ImageView imgSplash;

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void doBusiness(final Context mContext) {
        Glide.with(this).load(R.mipmap.splash).centerCrop().into(imgSplash);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserUtils.isLogin()){
                    NormalIntentUtils.go2MainActivity(mContext);
                }else {
                    NormalIntentUtils.go2LoginActivity(mContext);
                }

                finish();
            }
        },2000);
    }

}
