package cn.gogoal.im.activity;

import android.content.Context;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/**
 * author wangjd on 2017/6/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Face2FaceActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_face2face;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("面对面建群",true);
    }
}
