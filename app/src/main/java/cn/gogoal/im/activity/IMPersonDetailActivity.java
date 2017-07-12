package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.UserDetailInfo;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XLayout;


/**
 * Created by huangxx on 2017/3/17.
 */

public class IMPersonDetailActivity extends BaseActivity {

    private static final int ClassRequestCode = 0x10;

    @BindView(R.id.person_detail)
    RecyclerView personDetailRecycler;

    private String accountId;

    @BindView(R.id.add_friend_button)
    SelectorButton addFriendBtn;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @Override
    public int bindLayout() {
        return R.layout.activity_person_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_user_information, true);

        personDetailRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        personDetailRecycler.addItemDecoration(new NormalItemDecoration(mContext));

        accountId = getIntent().getStringExtra("account_id");

        if (accountId.equals(UserUtils.getMyAccountId())) {
            addFriendBtn.setVisibility(View.GONE);
        }

        addFriendBtn.setText(UserUtils.isMyFriend(accountId) ? "发送消息" : "添加好友");

        if (!TextUtils.isEmpty(accountId)) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        xLayout.setStatus(XLayout.Loading);

        UserUtils.getUserInfo(accountId, new Impl<String>() {
            @Override
            public void response(int code, String data) {
                switch (code) {
                    case Impl.RESPON_DATA_SUCCESS:
                        final ContactBean contactBean = JSONObject.parseObject(data, ContactBean.class);
                        List<UserDetailInfo> infos = new ArrayList<>();

                        infos.add(new UserDetailInfo<>(UserDetailInfo.HEAD,
                                (String) contactBean.getAvatar(), contactBean.getFull_name(), contactBean.getNickname()));

                        infos.add(new UserDetailInfo(UserDetailInfo.SPACE));
                        infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "地区", "--"));
                        infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "公司", contactBean.getOrg_name()));
                        infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "职位", contactBean.getDuty()));
                        infos.add(new UserDetailInfo(UserDetailInfo.SPACE));
                        infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "个人描述", contactBean.getDuty()));

                        personDetailRecycler.setAdapter(new UserInfoAdapter(infos));

                        xLayout.setStatus(XLayout.Success);

                        addFriendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                click(contactBean);
                            }
                        });
                        break;
                    case Impl.RESPON_DATA_EMPTY:
                        xLayout.setStatus(XLayout.Empty);
                        break;
                    case Impl.RESPON_DATA_ERROR:
                        UIHelper.toast(getActivity(), data);
                        xLayout.setStatus(XLayout.Error);
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode != 0) {
            if (requestCode == ClassRequestCode) {
                addFriendBtn.setClickable(false);
                addFriendBtn.setBackgroundResource(R.drawable.selector_button_enable_color);
                addFriendBtn.setText("等待验证");
            }
        }*/
    }


    public void click(ContactBean contactBean) {
        Intent intent;
        if (!TextUtils.isEmpty(accountId)) {
            if (UserUtils.isMyFriend(accountId)) {
                intent = new Intent(IMPersonDetailActivity.this, SingleChatRoomActivity.class);
                intent.putExtra("conversation_id", UserUtils.findAnyoneByFriendId(accountId) != null ?
                        UserUtils.findAnyoneByFriendId(accountId).getConv_id() : "");
                intent.putExtra("nickname", contactBean.getNickname());
                intent.putExtra("need_update", false);
                startActivity(intent);
                //点击发消息关闭前面页面
                AppManager.getInstance().finishActivity(IMPersonActivity.class);
                this.finish();
            } else {
                intent = new Intent(getActivity(), IMAddFriendActivity.class);
                intent.putExtra("user_id", accountId);
                //startActivityForResult(intent, ClassRequestCode);
                startActivity(intent);
            }
        } else {
            UIHelper.toast(getActivity(), "用户Id获取失败,请重试");
        }
    }

    private class UserInfoAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo, BaseViewHolder> {

        public UserInfoAdapter(List<UserDetailInfo> data) {
            super(data);
            addItemType(UserDetailInfo.HEAD, R.layout.header_rv_item_user_info);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_space_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_type_user_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, final UserDetailInfo data, int position) {

            switch (holder.getItemViewType()) {
                case UserDetailInfo.HEAD:
                    RoundedImageView imageAvatar = holder.getView(R.id.image_user_info_avatar);
                    ImageDisplay.loadRoundedRectangleImage(mContext, data.getAvatar(), imageAvatar);
                    imageAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<String> urls = new ArrayList<>();
                            urls.add((String) data.getAvatar());
                            Intent intent = new Intent(mContext, ImageDetailActivity.class);
                            intent.putStringArrayListExtra("image_urls", (ArrayList<String>) urls);
                            mContext.startActivity(intent);
                        }
                    });

                    holder.setText(R.id.person_name, data.getNickName());

                    /*设置备注，暂无接口*/
                    TextView tvRemark = holder.getView(R.id.person_mark);
                    tvRemark.setText(String.valueOf(accountId).equals(UserUtils.getMyAccountId()) ?
                            "账号：" + UserUtils.getGoGoalId() : "备注：");

                    Drawable drawable = String.valueOf(accountId).equals(UserUtils.getMyAccountId()) ?
                            null : ContextCompat.getDrawable(IMPersonDetailActivity.this, R.mipmap.img_edit_remark);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    }
                    tvRemark.setCompoundDrawables(null, null, drawable, null);

                    break;
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:
                    holder.setText(R.id.item_text_1, data.getItemKey());
                    holder.setText(R.id.item_text_2, data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.GONE);

                    UIHelper.setRippBg(holder.itemView);
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            UIHelper.toast(v.getContext(), getString(R.string.str_coming_soon));
//                        }
//                    });
                    break;
            }
        }
    }

}
