package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.DownloadCallBack;
import cn.gogoal.im.common.DownloadUtils;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
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

        isEditMyAvatar = accountId.equalsIgnoreCase(UserUtils.getUserAccountId());
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

    /**其他类型图片预览 弹窗*/
    private void saveImageDialog() {
        DialogHelp.getBottomSheetListDialog(getActivity(), new ArrayList<>(Arrays.asList(
                new String[]{"保存图片", "举报"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        DownloadUtils.downloadPicture(getActivity(),
                                imageUrls.get(vpImageDetail.getCurrentItem()), new DownloadCallBack() {
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
                    case 1:
                        break;
                }
            }
        });
    }

    /**头像预览 弹窗*/
    private void editDialogClick() {
        DialogHelp.getBottomSheetListDialog(getActivity(), new ArrayList<>(Arrays.asList(
                new String[]{"更换头像","保存图片"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        ImageTakeUtils.getInstance().takePhoto(getActivity(), 1, true, new ITakePhoto() {
                            @Override
                            public void success(List<String> uriPaths, boolean isOriginalPic) {
                                KLog.e(uriPaths.get(0));

                            }

                            @Override
                            public void error() {

                            }
                        });
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

}