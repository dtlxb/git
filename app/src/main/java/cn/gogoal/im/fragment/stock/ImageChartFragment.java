package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * author wangjd on 2017/4/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ImageChartFragment extends BaseFragment {

    @BindView(R.id.image_fragment)
    AppCompatImageView imageFragment;

    public static ImageChartFragment getInstance(String imageUrl) {
        ImageChartFragment fragment = new ImageChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image_url", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_chart_image;
    }

    @Override
    public void doBusiness(Context mContext) {
        String imageUrl = getArguments().getString("image_url");
        if (!TextUtils.isEmpty(imageUrl)) {
            ImageDisplay.loadChartImage(mContext,imageUrl,imageFragment);
        }
    }
}
