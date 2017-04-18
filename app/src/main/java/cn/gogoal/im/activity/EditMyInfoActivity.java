package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.UserInfo;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.widget.BottomSheetListDialog;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :编辑个人资料
 */
public class EditMyInfoActivity extends BaseActivity {

    private List<UserInfo> editInfos;
    private MyInfoAdapter myInfoAdapter;

    @BindView(R.id.rv_my_info)
    RecyclerView rvMyInfo;

    @BindArray(R.array.edit_info_key)
    String[] edidInfoArray;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_edit_my_infomation, true);
        editInfos = new ArrayList<>();
        myInfoAdapter = new MyInfoAdapter(editInfos);

        rvMyInfo.addItemDecoration(new NormalItemDecoration(mContext));
        rvMyInfo.setLayoutManager(new LinearLayoutManager(mContext));
        rvMyInfo.setAdapter(myInfoAdapter);
        iniListDatas();
    }

    private void iniListDatas() {
        String[] userInfoValue = {UserUtils.getUserName(), UserUtils.getNickname(), UserUtils.getPhoneNumber(),
                UserUtils.getorgName(), UserUtils.getDuty(), UserUtils.getOrganizationAddress()};
        editInfos.add(new UserInfo(UserInfo.HEAD, UserUtils.getUserAvatar()));
        for (int i = 0; i < edidInfoArray.length; i++) {
            editInfos.add(new UserInfo(
                    UserInfo.TEXT_ITEM_2,
                    !edidInfoArray[i].equals("Go-Goal账号"),
                    edidInfoArray[i],
                    userInfoValue[i]));
        }
        myInfoAdapter.notifyDataSetChanged();
    }

    private class MyInfoAdapter extends BaseMultiItemQuickAdapter<UserInfo, BaseViewHolder> {

        ArrayList<String> selectedPhoto = new ArrayList<>(Arrays.asList(
                new String[]{"拍照", "从手机相册选择", "保存图片"}));

        private MyInfoAdapter(List<UserInfo> data) {
            super(data);
            addItemType(UserInfo.HEAD, R.layout.header_rv_edit_my_info);
            addItemType(UserInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserInfo data, int position) {

            switch (holder.getItemViewType()) {
                case UserInfo.HEAD:
                    ImageDisplay.loadNetAvatarWithBorder(getActivity(),
                            data.getAvatar(),
                            (ImageView) holder.getView(R.id.image_user_info_avatar));

                    holder.getView(R.id.header_edit_my_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogHelp.getBottomSheetListDialog(getActivity(), selectedPhoto, new BottomSheetListDialog.DialogItemClick() {
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
                                        case 2:
                                            break;
                                        case 3:
                                            break;
                                    }
                                }
                            });
                        }
                    });
                    break;
                case UserInfo.SPACE:
                    break;
                case UserInfo.TEXT_ITEM_2:
                    holder.setText(R.id.tv_info_key, data.getItemKey());
                    holder.setText(R.id.tv_info_value, data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.GONE);

                    holder.getView(R.id.item_rv_edit_my_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIHelper.toast(v.getContext(), getString(R.string.str_coming_soon));
                        }
                    });
                    break;
            }
        }
    }

}
