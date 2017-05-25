package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.UserDetailInfo;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * author wangjd on 2017/5/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :账号与安全
 */
public class AccountSafeActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<UserDetailInfo> accountsafeInfos;
    private AccountSafeAdapter accountSafeAdapter;

    private String[] safeArray = {"Go-Goal账号", "手机号", "修改密码"};

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_account_safe, true);

        accountsafeInfos = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        accountSafeAdapter = new AccountSafeAdapter(accountsafeInfos);
        recyclerView.setAdapter(accountSafeAdapter);

        iniListDatas();
    }

    private void iniListDatas() {
        String[] userInfoValue = {UserUtils.getGoGoalId(), UserUtils.getPhoneNumber(), ""};

        accountsafeInfos.add(new UserDetailInfo<>(UserDetailInfo.SPACE));

        for (int i = 0; i < safeArray.length; i++) {
            accountsafeInfos.add(new UserDetailInfo(
                    UserDetailInfo.TEXT_ITEM_2,
                    !safeArray[i].equals("Go-Goal账号"),
                    safeArray[i],
                    userInfoValue[i]));
        }
        accountsafeInfos.add(2, new UserDetailInfo(UserDetailInfo.SPACE));

        accountSafeAdapter.notifyDataSetChanged();
    }

    private class AccountSafeAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo, BaseViewHolder> {

        private AccountSafeAdapter(List<UserDetailInfo> data) {
            super(data);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserDetailInfo data, final int position) {
            switch (holder.getItemViewType()) {
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:

                    View itemView = holder.getView(R.id.item_rv_edit_my_info);

                    itemView.setClickable(!data.getItemKey().equals("Go-Goal账号"));
                    itemView.setEnabled(!data.getItemKey().equals("Go-Goal账号"));

                    holder.setText(R.id.tv_info_key, data.getItemKey());

                    holder.setText(R.id.tv_info_value, data.getItemValue());

                    holder.getView(R.id.flag_img_more).setVisibility(data.isHaveMore() ? View.VISIBLE : View.GONE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (position) {
                                case 3:
                                    startActivity(new Intent(v.getContext(), ChangePhoneNumberActivity.class));
                                    break;
                                case 4:
                                    startActivity(new Intent(v.getContext(), ChangePasswordActivity.class));
                                    break;
                            }
                        }
                    });

                    break;
            }
        }
    }
}
