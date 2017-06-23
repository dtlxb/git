package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ContactsActivity;
import cn.gogoal.im.activity.EditMyInfoActivity;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.activity.MyAdvisersActivity;
import cn.gogoal.im.activity.MyGroupsActivity;
import cn.gogoal.im.activity.PhoneContactsActivity;
import cn.gogoal.im.activity.QrCodeActivity;
import cn.gogoal.im.activity.SettingActivity;
import cn.gogoal.im.activity.SettingStockActivity;
import cn.gogoal.im.activity.ToolsSettingActivity;
import cn.gogoal.im.adapter.InvestmentResearchAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.MineItem;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.ImageUtils.GlideUrilUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.NoAlphaItemAnimator;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

//    @BindView(R.id.text_flipper)
//    AdapterViewFlipper flipper;

    @BindView(R.id.rv_mine)
    RecyclerView rvMine;

    @BindView(R.id.img_mine_avatar)
    RoundedImageView imageAvatar;

    @BindView(R.id.tv_mine_userName)
    TextView tvMineUserName;

    @BindView(R.id.tv_mine_introduction)
    TextView tvMineIntroduction;

    @BindView(R.id.scrollView_mine)
    NestedScrollView scrollView;

    /**
     * 用户信息头部
     */
    @BindView(R.id.layout_user_head)
    ViewGroup layoutHead;

    private ImageView ivMessageTag;

    private MineAdapter mineAdapter;
    private ToolData.Tool moreTools;

    public MineFragment() {
    }

    @BindArray(R.array.mine_arr)
    String[] mineTitle;

    //===
    @BindView(R.id.rv_mine_tools)
    RecyclerView rvMineTools;

    @BindView(R.id.tv_tools_setting)
    TextView tvToolsFlag;

    private ArrayList<ToolData.Tool> mGridData;
    private InvestmentResearchAdapter toolsAdapter;
    //消息
    private BadgeView badge;
    private int unReadCount;

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onResume() {
        super.onResume();
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
        getTouYan();
    }

    @Override
    public void doBusiness(Context mContext) {

        moreTools = getMoreTools();

        XTitle.ImageAction messageAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.message_dark)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(), MessageHolderActivity.class));
            }
        };

        initools();
        iniheadInfo(mContext);
        initRecycler(mContext);
        initDatas();
        rvMine.setAdapter(mineAdapter);

//        setViewFlipper();

        badge = new BadgeView(getActivity());
        initBadge(unReadCount, badge);
    }

    private void initools() {
        rvMineTools.setNestedScrollingEnabled(false);
        rvMineTools.setItemAnimator(new NoAlphaItemAnimator());
        rvMineTools.setLayoutManager(new StaggeredGridLayoutManager(
                AppDevice.isLowDpi() ? 3 : 4,
                StaggeredGridLayoutManager.VERTICAL));
        mGridData = new ArrayList<>();
        toolsAdapter = new InvestmentResearchAdapter(
                getActivity(),//上下文
                mGridData, //小工具集
                null, //是否来自弹窗中使用
                null);//股票
        rvMineTools.setAdapter(toolsAdapter);
        getTouYan();
    }

    public void getTouYan() {
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        map.put("isShow", "1");

        new GGOKHTTP(map, GGOKHTTP.GET_USERCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                int code = object.getIntValue("code");
                if (code == 0) {
                    mGridData.clear();
                    List<ToolData.Tool> tools = JSONObject.parseArray(
                            object.getJSONArray("data").toJSONString(), ToolData.Tool.class);
                    mGridData.addAll(tools);

                    mGridData.add(moreTools);
                    toolsAdapter.notifyDataSetChanged();

                } else if (code == 1001) {
                    mGridData.clear();
                    mGridData.add(moreTools);
                    toolsAdapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
    }

    private ToolData.Tool getMoreTools() {
        ToolData.Tool moreTools = new ToolData.Tool();
        moreTools.setSimulatedArg(false);
        moreTools.setIconUrl(GlideUrilUtils.res2Uri(getContext(), R.mipmap.img_tools_center).toString());
        moreTools.setIsClick(10086);
        moreTools.setDesc("更多");
        return moreTools;
    }

    private void initRecycler(Context mContext) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvMine.setLayoutManager(layoutManager);
        rvMine.setHasFixedSize(true);
        rvMine.setNestedScrollingEnabled(false);
    }

    private void iniheadInfo(Context mContext) {
        AppDevice.setViewWidth$Height(imageAvatar,
                4 * AppDevice.getWidth(mContext) / 25,
                4 * AppDevice.getWidth(mContext) / 25);

        UserUtils.getUserAvatar(new Impl<Bitmap>() {
            @Override
            public void response(int code, Bitmap data) {
                imageAvatar.setImageBitmap(data);
            }
        });
        tvMineUserName.setText(UserUtils.getNickname());
        tvMineIntroduction.setText(UserUtils.getorgName() + " " + UserUtils.getDuty());

    }

    private void initDatas() {
        List<MineItem> mineItems = new ArrayList<>();
        for (int i = 0; i < mineTitle.length; i++) {
            int iconId = getResources().getIdentifier("img_mine_item_" + i, "mipmap", getActivity().getPackageName());
            mineItems.add(new MineItem(MineItem.TYPE_ICON_TEXT_ITEM, iconId, mineTitle[i]));
        }
        mineItems.add(1, new MineItem(MineItem.TYPE_SPACE));
        mineItems.add(4, new MineItem(MineItem.TYPE_SPACE));
        mineAdapter = new MineAdapter(mineItems);
    }

    @OnClick({R.id.layout_user_head,
            R.id.tv_tools_setting, R.id.img_mine_avatar})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_head:
                startActivity(new Intent(view.getContext(), EditMyInfoActivity.class));
                break;
            case R.id.tv_tools_setting:
                Intent intent = new Intent(view.getContext(), ToolsSettingActivity.class);
                mGridData.remove(moreTools);
                intent.putParcelableArrayListExtra("selected_tools", mGridData);
                startActivity(intent);
                break;
            case R.id.img_mine_avatar:
                startActivity(new Intent(view.getContext(), EditMyInfoActivity.class));
                break;
        }
    }

    @Subscriber(tag = "updata_cache_avatar")
    void updataCacheAvatar(String newAvatarUrl) {
        KLog.e(newAvatarUrl);
        ImageDisplay.loadCircleImage(getContext(), UserUtils.getUserAvatar(), imageAvatar);

    }

    @Subscriber(tag = "updata_userinfo")
    void updataUserInfo(String msg) {
        iniheadInfo(getActivity());
    }

    private class MineAdapter extends BaseMultiItemQuickAdapter<MineItem, BaseViewHolder> {

        private MineAdapter(List<MineItem> data) {
            super(data);
//            addItemType(MineItem.TYPE_HEAD, R.layout.item_type_mine_middle);
            addItemType(MineItem.TYPE_SPACE, R.layout.layout_space_15dp);
            addItemType(MineItem.TYPE_ICON_TEXT_ITEM, R.layout.item_type_mine_icon_text);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MineItem data, final int position) {
            switch (holder.getItemViewType()) {
                case MineItem.TYPE_HEAD:

                    route(holder, R.id.btn_mine_my_friend, ContactsActivity.class);//我的好友

                    route(holder, R.id.btn_mine_my_group, MyGroupsActivity.class);//我的群组

                    route(holder, R.id.btn_mine_my_phone_contacts, PhoneContactsActivity.class);//手机通讯录

                    route(holder, R.id.btn_mine_inviting_friends, ContactsActivity.class);//邀请好友

                    break;
                case MineItem.TYPE_SPACE:
                    break;
                case MineItem.TYPE_ICON_TEXT_ITEM:
                    holder.setText(R.id.item_text_normal, data.getItemText());
                    holder.setImageResource(R.id.item_img_normal, data.getIconRes());

                    holder.setVisible(R.id.view_divider,
                            data.getItemText().equals("行情设置") ||
                                    data.getItemText().equals("专属顾问") ||
                                    data.getItemText().equals("我的二维码")
                    );

                    holder.getView(R.id.item_layout_simple_image_text).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KLog.e("pos=" + position + ";item=" + data.getItemText());
                            final Intent intent;
                            switch (data.getItemText()) {
                                case "我要直播"://我要直播
                                    UserUtils.checkLivePermission(getActivity());
                                    break;
                                case "我的二维码"://我的二维码
                                    intent = new Intent(v.getContext(), QrCodeActivity.class);
                                    intent.putExtra("qr_code_type", GGQrCode.QR_CODE_TYPE_PERSIONAL);
                                    intent.putExtra("qrcode_name", UserUtils.getNickname());
                                    intent.putExtra("qrcode_info", UserUtils.getorgName() + " " + UserUtils.getDuty());
                                    intent.putExtra("qrcode_content_id", UserUtils.getMyAccountId());
                                    startActivity(intent);
                                    break;
                                case "行情设置"://自选股设置
                                    intent = new Intent(getActivity(), SettingStockActivity.class);
                                    startActivity(intent);
                                    break;
                                case "专属顾问":
                                    intent = new Intent(getActivity(), MyAdvisersActivity.class);
                                    startActivity(intent);
                                    break;
                                case "设置":
                                    intent = new Intent(getActivity(), SettingActivity.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    UIHelper.toastInCenter(getActivity(), "该功能暂未开放使用");
                                    break;
                            }
                        }
                    });
                    break;
            }
        }

        private void route(BaseViewHolder holder, int id, final Class<?> cazz) {
            holder.getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), cazz));
                }
            });
        }
    }

    private void initBadge(int num, BadgeView badge) {
//        badge.setGravityOffset(10, 7, true);
//        badge.setShowShadow(false);
//        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//        badge.setBadgeTextSize(8, true);
//        badge.bindTarget(ivMessageTag);
//        badge.setBadgeNumber(num);
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        unReadCount++;
        badge.setBadgeNumber(unReadCount);
    }

}
