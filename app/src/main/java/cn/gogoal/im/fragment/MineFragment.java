package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.EditMyInfoActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.MineItem;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.WeakReferenceHandler;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.rv_mine)
    RecyclerView rvMine;

    @BindView(R.id.img_mine_avatar)
    ImageView imageAvatar;

    @BindView(R.id.main_tv_toolbar_title)
    TextView mTitleText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_mine_userName)
    TextView tvMineUserName;

    @BindView(R.id.tv_mine_introduction)
    TextView tvMineIntroduction;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.layout_head)
    RelativeLayout relativeLayout;


    private MineAdapter mineAdapter;

    private List<MineItem> mineItems;

    private WeakReferenceHandler<MineFragment> handler = new WeakReferenceHandler<MineFragment>(Looper.getMainLooper(), this) {
        @Override
        protected void handleMessage(MineFragment fragment, Message message) {
        }
    };

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
        iniheadInfo(mContext);
        rvMine.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvMine.addItemDecoration(new NormalItemDecoration(mContext));
        initDatas();
        rvMine.setAdapter(mineAdapter);

    }

    private void iniheadInfo(Context mContext) {
        AppDevice.setViewWidth$Height(imageAvatar, 4 * AppDevice.getWidth(mContext) / 25, 3 * AppDevice.getWidth(mContext) / 13);
        AppDevice.setViewWidth$Height(toolbar,AppDevice.getWidth(mContext),
                AppDevice.getStatusBarHeight(mContext)+AppDevice.getDefaultActionBarSize(mContext));

        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
        params.setMargins(0,AppDevice.getStatusBarHeight(mContext)+AppDevice.getDefaultActionBarSize(mContext),0,
                AppDevice.dp2px(mContext,20));

        //设置头像
        ImageDisplay.loadNetAvatarWithBorder(mContext, UserUtils.getUserAvatar(), imageAvatar);
        toolbar.setPadding(0,AppDevice.getStatusBarHeight(mContext),0,0);
        tvMineUserName.setText(UserUtils.getUserName());
        tvMineIntroduction.setText(UserUtils.getDuty()+" "+UserUtils.getorgName());

//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                int halfScroll = appBarLayout.getTotalScrollRange() / 2;
//                int offSetAbs = Math.abs(verticalOffset);
//                float percentage;
//                if (offSetAbs < halfScroll) {
//                    mTitleText.setText("");
//                    percentage = 1 - (float) offSetAbs / (float) halfScroll;
//                } else {
//                    mTitleText.setText("个人中心");
//                    percentage = (float) (offSetAbs - halfScroll) / (float) halfScroll;
//                }
//                mTitleText.setAlpha(percentage);
//
//            }
//        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditMyInfoActivity.class));
            }
        });
    }

    private void initDatas() {
        mineItems = new ArrayList<>();
        mineItems.add(new MineItem(MineItem.HEAD));
        for (int i = 0; i < mineTitle.length; i++) {
            int iconId = getResources().getIdentifier("img_mine_item_" + i, "mipmap", getActivity().getPackageName());
            mineItems.add(new MineItem(MineItem.ICON_TEXT_ITEM, iconId, mineTitle[i]));
        }
        mineItems.add(1,new MineItem(MineItem.SPACE));
        mineItems.add(4,new MineItem(MineItem.SPACE));
        mineItems.add(8,new MineItem(MineItem.SPACE));
        mineAdapter = new MineAdapter(mineItems);
    }

    @OnClick(R.id.img_my_qrcode)
    void onClick(View view){
        UIHelper.toast(view.getContext(),"二维码");
    }

    @Subscriber(tag = "updata_cache_avatar")
    void updataCacheAvatar(String newAvatarUrl){
        ImageDisplay.loadNetAvatarWithBorder(getContext(), UserUtils.getUserAvatar(), imageAvatar);
    }

    private class MineAdapter extends BaseMultiItemQuickAdapter<MineItem, BaseViewHolder> {

        private MineAdapter(List<MineItem> data) {
            super(data);
            addItemType(MineItem.HEAD, R.layout.item_type_mine_middle);
            addItemType(MineItem.SPACE, R.layout.layout_sapce_15dp);
            addItemType(MineItem.ICON_TEXT_ITEM, R.layout.item_type_mine_icon_text);
        }

        @Override
        protected void convert(BaseViewHolder holder, MineItem data, int position) {
            switch (holder.getItemViewType()) {
                case MineItem.HEAD:
                    break;
                case MineItem.SPACE:
                    break;
                case MineItem.ICON_TEXT_ITEM:
                    holder.setText(R.id.item_text_normal, data.getItemText());
                    holder.setImageResource(R.id.item_img_normal, data.getIconRes());
                    break;
            }
        }
    }

}
