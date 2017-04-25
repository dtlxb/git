package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.UserDetailInfo;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.SelectorButton;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :设置页面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView rvSetting;

    @BindArray(R.array.settingArray)
    String[] strings;

    private SettingAdapter settingAdapter;
    private List<UserDetailInfo<String>> settingDatas = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("设置", true);

        settingAdapter=new SettingAdapter(settingDatas);

        initRecycleView(rvSetting,0);
        rvSetting.setAdapter(settingAdapter);

        SelectorButton selectorButton=new SelectorButton(mContext);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(AppDevice.dp2px(mContext,15),AppDevice.dp2px(mContext,55)
                ,AppDevice.dp2px(mContext,15),AppDevice.dp2px(mContext,20));
        selectorButton.setLayoutParams(params);

        selectorButton.setText("退出登录");
        selectorButton.setNormalBackgroundColor(getResColor(R.color.stock_red));
        selectorButton.setPressedBackgroundColor(getResColor(R.color.colorMineHead));
        selectorButton.setRadius(AppDevice.dp2px(mContext,5));
        selectorButton.setTextColor(Color.WHITE);
        selectorButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        selectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),TypeLoginActivity.class));
                finish();
            }
        });

        settingAdapter.addFooterView(selectorButton);

        iniSettingData();
    }

    private void iniSettingData() {
        for (String s:strings){
            settingDatas.add(new UserDetailInfo<>(UserDetailInfo.TEXT_ITEM_2,s));
        }
        settingDatas.add(2,new UserDetailInfo<String>(UserDetailInfo.SPACE));
        settingDatas.add(7,new UserDetailInfo<String>(UserDetailInfo.SPACE));
        settingAdapter.notifyDataSetChanged();
    }

    private class SettingAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo<String>, BaseViewHolder> {

        private SettingAdapter(List<UserDetailInfo<String>> data) {
            super(data);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_sapce_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, UserDetailInfo<String> data, final int position) {

            switch (holder.getItemViewType()) {
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:
                    holder.getView(R.id.tv_info_value).setVisibility(View.INVISIBLE);
                    holder.setText(R.id.tv_info_key, data.getAvatar());
                    break;
            }
        }
    }
}
