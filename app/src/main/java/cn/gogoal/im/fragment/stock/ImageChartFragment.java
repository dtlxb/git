package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/4/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ImageChartFragment extends BaseFragment {

    @BindView(R.id.image_fragment)
    AppCompatImageView imageFragment;

    @BindView(R.id.xLayout)
    XLayout xLayout;

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
        xLayout.setEmptyText("暂无图表数据信息");
        xLayout.setErrorText("该股票状态异常,没有图表信息");
        xLayout.getErrorView().findViewById(R.id.error_reload_btn).setVisibility(View.GONE);

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(getActivity()).load(imageUrl).into(new GlideDrawableImageViewTarget(imageFragment){
                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    xLayout.setStatus(XLayout.Error);
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    xLayout.setStatus(XLayout.Success);
                }
            });
        }else {
            xLayout.setStatus(XLayout.Empty);
        }
    }
}
