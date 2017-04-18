package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.UserInfo;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;

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
        String[] userInfoValue = {UserUtils.getNickname(), UserUtils.getUserName(), UserUtils.getPhoneNumber(),
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

        private MyInfoAdapter(List<UserInfo> data) {
            super(data);
            addItemType(UserInfo.HEAD, R.layout.header_rv_edit_my_info);
            addItemType(UserInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserInfo data, final int position) {

            switch (holder.getItemViewType()) {
                case UserInfo.HEAD:
                    ImageDisplay.loadNetAvatarWithBorder(getActivity(),
                            data.getAvatar(),
                            (ImageView) holder.getView(R.id.image_user_info_avatar));

                    holder.getView(R.id.header_edit_my_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), ImageDetailActivity.class);
                            intent.putExtra("account_Id",UserUtils.getUserAccountId());
                            startActivity(intent);
                        }
                    });
                    break;
                case UserInfo.SPACE:
                    break;
                case UserInfo.TEXT_ITEM_2:

                    View itemView = holder.getView(R.id.item_rv_edit_my_info);

                    itemView.setClickable(!data.getItemKey().equals("Go-Goal账号"));
                    itemView.setEnabled(!data.getItemKey().equals("Go-Goal账号"));

                    holder.setText(R.id.tv_info_key, data.getItemKey());
                    holder.setText(R.id.tv_info_value, data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.GONE);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIHelper.toast(v.getContext(), getString(R.string.str_coming_soon) + "::" + position);
                            switch (position) {
                                case 1://姓名、昵称
                                    break;
                                case 2://不支持修改
                                    break;
                                case 3://手机号，暂不支持修改
                                    UIHelper.toast(v.getContext(), getString(R.string.str_coming_soon) + "::" + position);
                                    break;
                                case 4://公司
                                    break;
                                case 5://职位
                                    break;
                                case 6://工作地区
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }

}
