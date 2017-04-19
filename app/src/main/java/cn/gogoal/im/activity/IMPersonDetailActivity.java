package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseInfo;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.UserDetailInfo;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.SelectorButton;


/**
 * Created by huangxx on 2017/3/17.
 */

public class IMPersonDetailActivity extends BaseActivity {

    @BindView(R.id.person_detail)
    RecyclerView personDetailRecycler;

    private int accountId = -1;

    @BindView(R.id.add_friend_button)
    SelectorButton addFriendBtn;

    @Override
    public int bindLayout() {
        return R.layout.activity_person_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);

        personDetailRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        personDetailRecycler.addItemDecoration(new NormalItemDecoration(mContext));
        accountId = getIntent().getIntExtra("account_id", -1);

        if (String.valueOf(accountId).equals(UserUtils.getUserAccountId())) {
            addFriendBtn.setVisibility(View.GONE);
        }

        addFriendBtn.setText(UserUtils.isMyFriend(accountId) ? "删除好友" : "添加好友");

        if (accountId != -1) {
            getUsernfo();
        }
    }

    private void getUsernfo() {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("account_id", String.valueOf(accountId));
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {

                    ContactBean contactBean = JSONObject.parseObject(responseInfo, BaseInfo.class).getData();

                    List<UserDetailInfo> infos = new ArrayList<>();

                    infos.add(new UserDetailInfo(UserDetailInfo.HEAD,
                            (String) contactBean.getAvatar(), contactBean.getFull_name(), contactBean.getNickname()));

                    infos.add(new UserDetailInfo(UserDetailInfo.SPACE));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "地区", "--"));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "公司", contactBean.getOrg_name()));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "职位", contactBean.getDuty()));
                    infos.add(new UserDetailInfo(UserDetailInfo.SPACE));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, false, "个人描述", contactBean.getDuty()));
                    infos.add(new UserDetailInfo(UserDetailInfo.SPACE));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, true, "研网活动(5)", ""));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, true, "参加活动活动(4)", ""));
                    infos.add(new UserDetailInfo(UserDetailInfo.TEXT_ITEM_2, true, "加入群组(3)", ""));

                    personDetailRecycler.setAdapter(new UserInfoAdapter(infos));

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {

                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_ACCOUNT_DETAIL, ggHttpInterface).startGet();
    }

    @OnClick(R.id.add_friend_button)
    void toAddFraeng(View view) {

        if (accountId != -1) {
            if (UserUtils.isMyFriend(accountId)) {
                UIHelper.toast(view.getContext(), "没有删除接口");
            } else {
                Intent intent = new Intent(view.getContext(), IMAddFriendActivity.class);
                intent.putExtra("user_id", accountId);
                startActivity(intent);
            }
        } else {
            UIHelper.toast(view.getContext(), "用户Id获取失败,请重试");
        }
    }

    private class UserInfoAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo, BaseViewHolder> {

        public UserInfoAdapter(List<UserDetailInfo> data) {
            super(data);
            addItemType(UserDetailInfo.HEAD, R.layout.header_rv_item_user_info);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_type_user_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserDetailInfo data, int position) {

            switch (holder.getItemViewType()) {
                case UserDetailInfo.HEAD:
                    holder.setImageUrl(R.id.image_user_info_avatar, data.getAvatar());
                    holder.setText(R.id.person_name, data.getFullName());
                    holder.setText(R.id.person_mark, data.getNickName());
                    break;
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:
                    holder.setText(R.id.item_text_1, data.getItemKey());
                    holder.setText(R.id.item_text_2, data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.GONE);

                    UIHelper.setRippBg(holder.itemView);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
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
