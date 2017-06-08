package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.EditInfoBean;
import cn.gogoal.im.bean.UserDetailInfo;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.dialog.AddressPicker;


/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :编辑个人资料
 */
public class EditMyInfoActivity extends BaseActivity {

    private List<UserDetailInfo> editInfos;
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
        iniListDatas();//初始化设置列表
        getMyInfo();//获取个人信息
    }

    private void getMyInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        new GGOKHTTP(map, GGOKHTTP.GET_MY_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    EditInfoBean.EditInfoData editInfoData = JSONObject.parseObject(responseInfo, EditInfoBean.class).getData();
                    UserUtils.updateLocalUserInfo("city", editInfoData.getCity());
                    updataUserInfo("");
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    private void iniListDatas() {
        String[] userInfoValue = {UserUtils.getNickname(), UserUtils.getUserName(), UserUtils.getPhoneNumber(),
                UserUtils.getorgName(), UserUtils.getDuty(), UserUtils.getOrganizationAddress(), UserUtils.getOrganizationAddress()};

        editInfos.add(new UserDetailInfo<>(UserDetailInfo.HEAD));

        for (int i = 0; i < edidInfoArray.length; i++) {
            editInfos.add(new UserDetailInfo(
                    UserDetailInfo.TEXT_ITEM_2,
                    !edidInfoArray[i].equals("Go-Goal账号"),
                    edidInfoArray[i],
                    userInfoValue[i]));
        }
        myInfoAdapter.notifyDataSetChanged();
    }

    private class MyInfoAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo, BaseViewHolder> {

        private MyInfoAdapter(List<UserDetailInfo> data) {
            super(data);
            addItemType(UserDetailInfo.HEAD, R.layout.header_rv_edit_my_info);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserDetailInfo data, final int position) {
            final RoundedImageView circleImageView = holder.getView(R.id.image_user_info_avatar);
            switch (holder.getItemViewType()) {
                case UserDetailInfo.HEAD:
                    UserUtils.getUserAvatar(new Impl<Bitmap>() {
                        @Override
                        public void response(boolean success, Bitmap data) {
                            circleImageView.setImageBitmap(data);
                        }
                    });

                    holder.getView(R.id.header_edit_my_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), ImageDetailActivity.class);
                            intent.putExtra("is_change_avatar", true);
                            startActivity(intent);

                        }
                    });
                    break;
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:

                    View itemView = holder.getView(R.id.item_rv_edit_my_info);

                    itemView.setClickable(!data.getItemKey().equals("Go-Goal账号"));
                    itemView.setEnabled(!data.getItemKey().equals("Go-Goal账号"));

                    holder.setText(R.id.tv_info_key, data.getItemKey());

                    holder.setText(R.id.tv_info_value, TextUtils.isEmpty(data.getItemValue()) ? "未设置" : data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.INVISIBLE);

                    final Intent intent = new Intent(getActivity(), SingleEditActivity.class);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            UIHelper.toast(v.getContext(), getString(R.string.str_coming_soon) + "::" + position);
                            switch (position) {
                                case 1://姓名、昵称
                                    intent.putExtra(SingleEditActivity.EDIT_MY_INFO_TYPE, SingleEditActivity.EDIT_MY_INFO_TYPE_NAME);
                                    startActivity(intent);
                                    break;
                                case 2://不支持修改
                                    break;
                                case 3://
                                    startActivity(new Intent(v.getContext(), ChangePhoneNumberActivity.class));
                                    break;
                                case 4://公司
                                    intent.putExtra(SingleEditActivity.EDIT_MY_INFO_TYPE, SingleEditActivity.EDIT_MY_INFO_TYPE_COMPANY);
                                    startActivity(intent);
                                    break;
                                case 5://职位
                                    intent.putExtra(SingleEditActivity.EDIT_MY_INFO_TYPE, SingleEditActivity.EDIT_MY_INFO_TYPE_DUTY);
                                    startActivity(intent);
                                    break;
                                case 6://工作地区
                                    new AddressPicker().show(getSupportFragmentManager());
//                                    startActivity(new Intent(v.getContext(),AddressActivity.class));
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Subscriber(tag = "updata_cache_avatar")
    void updataCacheAvatar(String newAvatarUrl) {
        editInfos.remove(0);
//        editInfos.add(0,new UserDetailInfo(UserDetailInfo.HEAD, UserUtils.getUserCacheAvatarFile()));
        editInfos.add(0, new UserDetailInfo<>(UserDetailInfo.HEAD, newAvatarUrl));
        myInfoAdapter.notifyItemChanged(0);
    }

    @Subscriber(tag = "updata_userinfo")
    void updataUserInfo(String msg) {
        editInfos.clear();
        iniListDatas();
    }
}
