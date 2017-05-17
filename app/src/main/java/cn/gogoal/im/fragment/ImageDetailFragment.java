package cn.gogoal.im.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.ImageUtils.UFileImageHelper;
import cn.gogoal.im.ui.view.XLayout;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :图片预览的Fragment
 */
public class ImageDetailFragment extends BaseFragment {

    private static final String IMAGE_URL = "image";

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.image)
    PhotoView image;

    private String imageUrl;

    @Override
    public int bindLayout() {
        return R.layout.fragment_iamge_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
        xLayout.setStatus(XLayout.Loading);

        KLog.e(imageUrl);

        Glide.with(mContext).load(imageUrl).into(new GlideDrawableImageViewTarget(image) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
            }

            @Override
            public void onStop() {
                super.onStop();
                xLayout.setStatus(XLayout.Success);
            }
        });

        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
                KLog.e("image点击");
            }
        });
    }

    public static ImageDetailFragment getInstance(String imageUrl) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundl = new Bundle();
        bundl.putString(IMAGE_URL, UFileImageHelper.getUFileOriginalImage(imageUrl));
        fragment.setArguments(bundl);
        return fragment;
    }

    @Subscriber(tag = "updata_cache_avatar")
    void updataCacheAvatar(String newAvatarUrl) {
        Glide.with(getActivity()).load(newAvatarUrl).into(image);
    }
}
