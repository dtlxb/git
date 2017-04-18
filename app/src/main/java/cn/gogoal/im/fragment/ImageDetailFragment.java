package cn.gogoal.im.fragment;

import android.content.Context;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import uk.co.senab.photoview.PhotoView;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :图片预览的Fragment
 */
public class ImageDetailFragment extends BaseFragment {

    private static final String IMAGE_URL = "image";

    @BindView(R.id.image)
    PhotoView image;

    private String imageUrl;

    @Override
    public int bindLayout() {
        return R.layout.fragment_iamge_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public void doBusiness(Context mContext) {
        Glide.with(getActivity()).load(imageUrl).into(image);
    }

    public static ImageDetailFragment getInstance(String imageUrl) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundl = new Bundle();
        bundl.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(bundl);
        return fragment;
    }
}
