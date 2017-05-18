package cn.gogoal.im.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

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
    public void doBusiness(final Context mContext) {
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
//        xLayout.setStatus(XLayout.Loading);

        loadImage(mContext);

        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadImage(mContext);
            }
        });
    }

    private void loadImage(Context mContext) {
        Glide.with(mContext).load(imageUrl)
                .fitCenter().into(new GlideDrawableImageViewTarget(image) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                xLayout.setStatus(XLayout.Success);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                xLayout.setStatus(XLayout.Loading);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                xLayout.setStatus(XLayout.Error);
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
