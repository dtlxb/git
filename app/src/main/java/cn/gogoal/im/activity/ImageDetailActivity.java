package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hply.imagepicker.ITakePhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.MD5Utils;
import cn.gogoal.im.common.SaveBitmapAsyncTask;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.ImageDetailFragment;
import cn.gogoal.im.ui.dialog.BottomSheetListDialog;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.PhotoViewPager;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :头像预览、修改
 */
public class ImageDetailActivity extends BaseActivity {

    @BindView(R.id.vp_image_detail)
    PhotoViewPager vpImageDetail;

    private List<String> imageUrls = new ArrayList<>();

    private boolean isEditMyAvatar;

    public View addAction;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_avatar;
    }

    @Override
    public void doBusiness(Context mContext) {
        isEditMyAvatar = getIntent().getBooleanExtra("is_change_avatar", false);

        XTitle title = setMyTitle(isEditMyAvatar ? "个人头像" : "", true);

        int firstIndex = getIntent().getIntExtra("index", 0);

        title.setVisibility(isEditMyAvatar ? View.VISIBLE : View.GONE);

        //自己头像,查看修改
        //其他类型
        addAction = title.addAction(new XTitle.ImageAction(getResDrawable(R.drawable.ic_more_horiz_black_24dp)) {

            @Override
            public void actionClick(View view) {
                if (isEditMyAvatar) { //自己头像,查看修改
                    editDialogClick();
                } else { //其他类型
                    saveImageDialog();
                }
            }
        });

        if (isEditMyAvatar) {
            imageUrls.add(UserUtils.getUserAvatar());
        } else {
            imageUrls.addAll(getIntent().getStringArrayListExtra("image_urls"));
        }

        vpImageDetail.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return ImageDetailFragment.getInstance(imageUrls.get(position));
            }

            @Override
            public int getCount() {
                return imageUrls == null ? 0 : imageUrls.size();
            }
        });

        vpImageDetail.setOffscreenPageLimit(imageUrls.size());

        vpImageDetail.setCurrentItem(firstIndex);
    }

    /**
     * 其他类型图片预览 弹窗
     */
    private void saveImageDialog() {
        DialogHelp.getBottomSheetListDialog(getActivity(), new ArrayList<>(Arrays.asList(
                new String[]{"保存图片", "举报"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        ImageUtils.getUrlBitmap(getActivity(),
                                imageUrls.get(vpImageDetail.getCurrentItem()),
                                new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        new SaveBitmapAsyncTask(
                                                getActivity(),
                                                MD5Utils.getMD5EncryptyString16(imageUrls.get(vpImageDetail.getCurrentItem())),
                                                "图片")
                                                .execute(resource);
                                    }
                                });
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    /**
     * 头像预览 弹窗
     */
    private void editDialogClick() {
        DialogHelp.getBottomSheetListDialog(getActivity(), new ArrayList<>(Arrays.asList(
                new String[]{"更换头像", "保存图片"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0://更换头像
                        //打开相机、相册
                        openCamera();
                        break;
                    case 1://保存图片
                        ImageUtils.getUrlBitmap(getActivity(), imageUrls.get(vpImageDetail.getCurrentItem()), new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                new SaveBitmapAsyncTask(getActivity(),
                                        MD5Utils.getMD5EncryptyString16(imageUrls.get(vpImageDetail.getCurrentItem())),
                                        "头像")
                                        .execute(resource);
                            }
                        });
                        break;
                }
            }
        });
    }

    private void openCamera() {
        ImageTakeUtils.getInstance().takePhoto(getActivity(), 1, true, new ITakePhoto() {
            @Override
            public void success(List<String> uriPaths, boolean isOriginalPic) {
                if (uriPaths != null && (!uriPaths.isEmpty())) {
                    File chooseAvatar = new File(uriPaths.get(0));
                    uploadAvatar(chooseAvatar);
                }
            }

            @Override
            public void error() {

            }
        });
    }

    //上传头像到ufile
    private void uploadAvatar(File chooseAvatar) {
        //弹个窗
        final ProgressDialog waitDialog =
                DialogHelp.getWaitDialog(getActivity(), "上传中...");
        waitDialog.setCancelable(false);
        waitDialog.show();

        //上传
        UFileUpload.getInstance().upload(chooseAvatar,
                UFileUpload.Type.IMAGE,
                new UFileUpload.UploadListener() {
                    @Override
                    public void onUploading(int progress) {

                    }

                    @Override
                    public void onSuccess(String onlineUri) {
                        waitDialog.dismiss();
                        UIHelper.toast(getActivity(), "修改成功");
                        AppManager.getInstance().sendMessage("updata_cache_avatar",
                                onlineUri);
                        UserUtils.updateLocalUserInfo("simple_avatar", onlineUri);


                        Map<String, String> map = new HashMap<>();
                        map.put("avatar", StringUtils.decodeUrl(onlineUri));
                        new UserUtils().updataNetUserInfo(map, null);
                    }

                    @Override
                    public void onFailed() {
                        waitDialog.dismiss();
                        UIHelper.toast(getActivity(), "修改出错！");
                    }
                });
    }

}
