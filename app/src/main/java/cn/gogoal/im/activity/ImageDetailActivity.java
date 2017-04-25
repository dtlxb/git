package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

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
import cn.gogoal.im.common.DownloadCallBack;
import cn.gogoal.im.common.DownloadUtils;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.ImageDetailFragment;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.BottomSheetListDialog;
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

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_avatar;
    }

    @Override
    public void doBusiness(Context mContext) {
        final String accountId = getIntent().getStringExtra("account_Id");

        isEditMyAvatar = accountId.equalsIgnoreCase(UserUtils.getMyAccountId());
        XTitle title = setMyTitle(isEditMyAvatar ? "个人头像" : "", true);

        title.addAction(new XTitle.ImageAction(getResDrawable(R.drawable.ic_more_horiz_black_24dp)) {
            @Override
            public void actionClick(View view) {
                if (isEditMyAvatar) { //自己头像,查看修改
                    editDialogClick();
                } else { //其他类型
                    KLog.e("其他类型//TODO");
                    saveImageDialog();
                }
            }
        });

        if (isEditMyAvatar) {
            imageUrls.add(UserUtils.getUserAvatar());
            imageUrls.add("http://hackfile.ufile.ucloud.cn/gogoal/avatar/ucloud_266F015CFCC3D7AB.jpg");
            imageUrls.add("http://www.jcodecraeer.com/uploads/20170330/1490865016182928.jpeg");
            imageUrls.add("http://hackfile.ufile.ucloud.cn/gogoal/avatar/ucloud_9385F5CF7F318CEE.jpg@1000x1000");
        } else {
            imageUrls.clear();
            imageUrls.addAll(null /*如果是别的预览情况，就传入别的图片集合*/);
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
                        DownloadUtils.getInstance(DownloadUtils.DEFAULT_DOWNLOAD_PATH).downloadPicture(getActivity(),
                                imageUrls.get(vpImageDetail.getCurrentItem()),null, new DownloadCallBack() {
                                    @Override
                                    public void success() {
                                        UIHelper.toast(getActivity(), "图片已成功下载到相册");
                                    }

                                    @Override
                                    public void error(String errorMsg) {
                                        UIHelper.toast(getActivity(), "图片载出错::" + errorMsg);
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
                        UserUtils.updataLocalUserInfo("simple_avatar", onlineUri);

                        KLog.e(UserUtils.getUserAvatar());

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
