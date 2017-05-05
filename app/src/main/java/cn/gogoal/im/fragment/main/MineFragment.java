package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.EditMyInfoActivity;
import cn.gogoal.im.activity.SettingActivity;
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

    @BindView(R.id.tv_mine_title)
    TextView mTitleText;

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
        AppDevice.setViewWidth$Height(imageAvatar, 4 * AppDevice.getWidth(mContext) / 25, 3 * AppDevice.getWidth(mContext) / 13);
        iniheadInfo(mContext);
        initRecycler(mContext);
        initDatas();
        rvMine.setAdapter(mineAdapter);

        //滚动监听
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY < AppDevice.dp2px(getContext(), 150)) {
                    mTitleText.setAlpha(scrollY * 1.0f / AppDevice.dp2px(getContext(), 150));
                    layoutHead.setAlpha(1 - scrollY * 1.0f / AppDevice.dp2px(getContext(), 150));
                }
            }
        });
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

    private void iniheadInfo(Context mContext) {
        UserUtils.cacheUserAvatar();//缓存用户头像大图
        if (null != UserUtils.getUserCacheAvatarFile() &&
                (!(UserUtils.getUserCacheAvatarFile().getAbsolutePath()).equalsIgnoreCase(UserUtils.getMyAvatarCacheName()))) {
            ImageDisplay.loadCircleFileImageWithBoard(mContext, UserUtils.getUserCacheAvatarFile(), imageAvatar);
            KLog.e("用的缓存");
        } else {
            ImageDisplay.loadNetAvatarWithBorder(mContext, UserUtils.getUserAvatar(),imageAvatar);
            UserUtils.cacheUserAvatar();//缓存用户头像大图
            KLog.e("用的线上");
        }
        tvMineUserName.setText(UserUtils.getUserName());
        tvMineIntroduction.setText(UserUtils.getDuty());

    }

    private void initDatas() {
        mineItems = new ArrayList<>();
        mineItems.add(new MineItem(MineItem.TYPE_HEAD));
        for (int i = 0; i < mineTitle.length; i++) {
            int iconId = getResources().getIdentifier("img_mine_item_" + i, "mipmap", getActivity().getPackageName());
            mineItems.add(new MineItem(MineItem.TYPE_ICON_TEXT_ITEM, iconId, mineTitle[i]));
        }
        mineItems.add(1, new MineItem(MineItem.TYPE_SPACE));
        mineItems.add(4, new MineItem(MineItem.TYPE_SPACE));
        mineItems.add(8, new MineItem(MineItem.TYPE_SPACE));
        mineAdapter = new MineAdapter(mineItems);
    }

    @OnClick({R.id.img_my_qrcode, R.id.layout_user_head})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_my_qrcode:
                UIHelper.toast(view.getContext(), "二维码");
                break;
            case R.id.layout_user_head:
                startActivity(new Intent(view.getContext(), EditMyInfoActivity.class));
                break;
        }
    }

    @Subscriber(tag = "updata_cache_avatar")
    void updataCacheAvatar(String newAvatarUrl) {
        KLog.e(newAvatarUrl);
        ImageDisplay.loadNetAvatarWithBorder(getContext(), UserUtils.getUserAvatar(), imageAvatar);

    }

    @Subscriber(tag = "updata_userinfo")
    void updataUserInfo(String msg){
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
        protected void convert(BaseViewHolder holder, MineItem data, final int position) {
            switch (holder.getItemViewType()) {
                case MineItem.TYPE_HEAD:
                    break;
                case MineItem.TYPE_SPACE:
                    break;
                case MineItem.TYPE_ICON_TEXT_ITEM:
                    holder.setText(R.id.item_text_normal, data.getItemText());
                    holder.setImageResource(R.id.item_img_normal, data.getIconRes());

                    holder.getView(R.id.item_layout_simple_image_text).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (position) {
                                case 10:
                                    startActivity(new Intent(v.getContext(), SettingActivity.class));
                                    break;
                                default:
                                    UIHelper.toast(v.getContext(), "pos=" + position);
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }

}