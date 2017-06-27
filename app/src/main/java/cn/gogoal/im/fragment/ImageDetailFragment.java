package cn.gogoal.im.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ImageDetailActivity;
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

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((ImageDetailActivity) getActivity()).addAction.performClick();
                return true;
            }
        });
    }

    private void loadImage(Context mContext) {
        RequestOptions options=new RequestOptions();
        options.fitCenter();

        Glide.with(mContext).load(imageUrl).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                xLayout.setStatus(XLayout.Error);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                xLayout.setStatus(XLayout.Success);
                return false;
            }
        }).into(image);


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
