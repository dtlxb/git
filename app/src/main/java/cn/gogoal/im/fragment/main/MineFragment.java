package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ContactsActivity;
import cn.gogoal.im.activity.EditMyInfoActivity;
import cn.gogoal.im.activity.MyAdvisersActivity;
import cn.gogoal.im.activity.MyGroupsActivity;
import cn.gogoal.im.activity.PhoneContactsActivity;
import cn.gogoal.im.activity.SettingActivity;
import cn.gogoal.im.activity.SettingStockActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.MineItem;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

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

    private MineAdapter mineAdapter;

    public MineFragment() {
    }

    @BindArray(R.array.mine_arr)
    String[] mineTitle;

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("我");

        iniheadInfo(mContext);
        initRecycler(mContext);
        initDatas();
        rvMine.setAdapter(mineAdapter);

    }

    private void initRecycler(Context mContext) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvMine.setLayoutManager(layoutManager);
        rvMine.setHasFixedSize(true);
        rvMine.setNestedScrollingEnabled(false);
        rvMine.addItemDecoration(new NormalItemDecoration(mContext));
    }

//    @Override
//    protected void immersionInit() {
//        super.immersionInit();
//
//        StatusBarUtil.with(getActivity())
//                .statusBarDarkFont(false)
//                .barColor(R.color.colorMineHead)
//                .init();
//    }

    private void iniheadInfo(Context mContext) {
        AppDevice.setViewWidth$Height(imageAvatar,
                4 * AppDevice.getWidth(mContext) / 25,
                4 * AppDevice.getWidth(mContext) / 25);

        UserUtils.getUserAvatar(new AvatarTakeListener() {
            @Override
            public void success(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageAvatar.setImageBitmap(bitmap);
                    }
                });

            }

            @Override
            public void failed(Exception e) {

            }
        });
        tvMineUserName.setText(UserUtils.getNickname());
        tvMineIntroduction.setText(UserUtils.getDuty());

    }

    private void initDatas() {
        List<MineItem> mineItems = new ArrayList<>();
        mineItems.add(new MineItem(MineItem.TYPE_HEAD));
        mineItems.add(new MineItem(MineItem.TYPE_SPACE));
        for (int i = 0; i < mineTitle.length; i++) {
            int iconId = getResources().getIdentifier("img_mine_item_" + i, "mipmap", getActivity().getPackageName());
            mineItems.add(new MineItem(MineItem.TYPE_ICON_TEXT_ITEM, iconId, mineTitle[i]));
        }
        mineItems.add(3, new MineItem(MineItem.TYPE_SPACE));
        mineAdapter = new MineAdapter(mineItems);
    }

    @OnClick({R.id.layout_user_head})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_head:
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
            addItemType(MineItem.TYPE_HEAD, R.layout.item_type_mine_middle);
            addItemType(MineItem.TYPE_SPACE, R.layout.layout_sapce_15dp);
            addItemType(MineItem.TYPE_ICON_TEXT_ITEM, R.layout.item_type_mine_icon_text);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MineItem data, final int position) {
            switch (holder.getItemViewType()) {
                case MineItem.TYPE_HEAD:

                    route(holder,R.id.btn_mine_my_friend, ContactsActivity.class);//我的好友

                    route(holder,R.id.btn_mine_my_group, MyGroupsActivity.class);//我的群组

                    route(holder,R.id.btn_mine_my_phone_contacts, PhoneContactsActivity.class);//手机通讯录

                    route(holder,R.id.btn_mine_inviting_friends, ContactsActivity.class);//邀请好友

                    break;
                case MineItem.TYPE_SPACE:
                    break;
                case MineItem.TYPE_ICON_TEXT_ITEM:
                    holder.setText(R.id.item_text_normal, data.getItemText());
                    holder.setImageResource(R.id.item_img_normal, data.getIconRes());

                    holder.getView(R.id.item_layout_simple_image_text).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KLog.e("pos=" + position + ";item=" + data.getItemText());
                            Intent intent;
                            switch (data.getItemText()) {
                                case "我要直播"://我要直播
//                                    ((MainActivity)getActivity()).socialContactFragment.getUserValid();
                                    break;
                                case "自选股设置"://自选股设置
                                    intent=new Intent(getActivity(),SettingStockActivity.class);
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
                                    UIHelper.toastInCenter(getActivity(),"该功能暂未开放使用");
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
                    startActivity(new Intent(v.getContext(),cazz));
                }
            });
        }
    }

}
