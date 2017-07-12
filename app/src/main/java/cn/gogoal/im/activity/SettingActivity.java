package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.GlideCacheUtil;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UserUtils;
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

        settingAdapter = new SettingAdapter(settingDatas);

        initRecycleView(rvSetting, null);
        rvSetting.setAdapter(settingAdapter);
        rvSetting.setBackgroundColor(getResColor(R.color.colorTitle));


        SelectorButton selectorButton = new SelectorButton(mContext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(AppDevice.dp2px(mContext, 15), AppDevice.dp2px(mContext, 15)
                , AppDevice.dp2px(mContext, 15), AppDevice.dp2px(mContext, 20));
        selectorButton.setLayoutParams(params);

        selectorButton.setText("退出登录");
        selectorButton.setNormalBackgroundColor(getResColor(R.color.stock_red));
        selectorButton.setPressedBackgroundColor(getResColor(R.color.colorMineHead));
        selectorButton.setRadius(AppDevice.dp2px(mContext, 5));
        selectorButton.setTextColor(Color.WHITE);
        selectorButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        selectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtils.logout(SettingActivity.this);
                finish();
            }
        });

        settingAdapter.addFooterView(selectorButton);

        iniSettingData();
    }

    private void clearMyAppCache() {
        String rubbish = GlideCacheUtil.getInstance().getCacheSize(SettingActivity.this);
        if (TextUtils.isEmpty(rubbish) || rubbish.equals("0.0Byte")) {
            DialogHelp.getMessageDialog(SettingActivity.this, "已经没有缓存了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else {
            DialogHelp.getConfirmDialog(SettingActivity.this, "总共有" + rubbish + "的缓存文件,确认要删除么？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GlideCacheUtil.getInstance().clearImageAllCache(SettingActivity.this);
                }
            }).show();
        }
    }

    private void iniSettingData() {
        settingDatas.add(new UserDetailInfo<String>(UserDetailInfo.SPACE));
        for (String s : strings) {
            settingDatas.add(new UserDetailInfo<String>(UserDetailInfo.TEXT_ITEM_2, s));
        }

        settingDatas.add(2, new UserDetailInfo<String>(UserDetailInfo.SPACE));
        settingDatas.add(6, new UserDetailInfo<String>(UserDetailInfo.SPACE));
        settingDatas.add(8, new UserDetailInfo<String>(UserDetailInfo.SPACE));
        settingAdapter.notifyDataSetChanged();
    }

    private class SettingAdapter extends BaseMultiItemQuickAdapter<UserDetailInfo<String>, BaseViewHolder> {

        private SettingAdapter(List<UserDetailInfo<String>> data) {
            super(data);
            addItemType(UserDetailInfo.SPACE, R.layout.layout_space_15dp);
            addItemType(UserDetailInfo.TEXT_ITEM_2, R.layout.item_rv_edit_my_info);
        }

        @Override
        protected void convert(BaseViewHolder holder, final UserDetailInfo<String> data, final int position) {

            switch (holder.getItemViewType()) {
                case UserDetailInfo.SPACE:
                    break;
                case UserDetailInfo.TEXT_ITEM_2:
                    holder.setText(R.id.tv_info_key, data.getItemValue());

                    holder.getView(R.id.tv_info_value).setVisibility(View.INVISIBLE);

                    holder.setVisible(R.id.view_divider, !(data.getItemValue().equalsIgnoreCase("账号与安全")
                            || data.getItemValue().equalsIgnoreCase("涨跌显示设置")
                            || data.getItemValue().equalsIgnoreCase("检查更新")
                            || data.getItemValue().equalsIgnoreCase("清除缓存")
                    ));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent;

                            switch (data.getItemValue()) {
                                case "账号与安全":
                                    startActivity(new Intent(view.getContext(), AccountSafeActivity.class));
                                    break;

                                case "行情刷新频率":
                                    //行情刷新频率
                                    intent = new Intent(view.getContext(), SetStockRefreshActivity.class);
                                    startActivity(intent);
                                    break;
                                case "K线设置":
                                    //K线设置
                                    intent = new Intent(view.getContext(), KlineSettingActivity.class);
                                    startActivity(intent);
                                    break;
                                case "涨跌显示设置":
                                    //涨跌幅显示
                                    intent = new Intent(view.getContext(), RedGreenSettingActivity.class);
                                    startActivity(intent);
                                    break;

                                case "清除缓存":
                                    //清除缓存
                                    clearMyAppCache();
                                    break;

                                case "关于":
                                    NormalIntentUtils.go2WebActivity(getActivity(),
                                            AppConst.GG_SETTING_ABOUT,
//                                            "http://192.168.52.156:9000/#/hello",
                                            data.getItemValue());
                                    break;

                                case "免责申明":
                                    NormalIntentUtils.go2WebActivity(
                                            getActivity(),
                                            AppConst.GG_DISCLAIMER,
                                            data.getItemValue());
                                    break;

                                case "服务协议":
                                    NormalIntentUtils.go2WebActivity(
                                            getActivity(),
                                            AppConst.GG_SERVICE_AGREEMENT, data.getItemValue());
                                    break;
                                case "检查更新":
                                    AppDevice.checkUpdata(getSupportFragmentManager(), true);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }
}
