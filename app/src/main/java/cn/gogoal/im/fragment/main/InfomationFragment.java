package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import org.simple.eventbus.Subscriber;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.fragment.infomation.InfoMyStockTabFragment;
import cn.gogoal.im.fragment.infomation.InfomationTabFragment;
import cn.gogoal.im.fragment.infomation.SevenBy24Fragment;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯tab
 */
public class InfomationFragment extends BaseFragment {

    @BindView(R.id.tab_infomation)
    TabLayout tabInfomation;

    @BindView(R.id.vp_infomation)
    ViewPager vpInfomation;

    @BindArray(R.array.info_arrar)
    String[] infoArrays;

    private ImageView ivMessageTag;
    //消息
    private BadgeView badge;
    private int unReadCount;

    @Override
    public int bindLayout() {
        return R.layout.fragment_infomation;
    }

    @Override
    public void onResume() {
        super.onResume();
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    @Override
    public void doBusiness(Context mContext) {

        XTitle xTitle = setFragmentTitle(R.string.title_information);

        XTitle.ImageAction messageAction = new XTitle.ImageAction(ContextCompat.getDrawable(mContext, R.mipmap.message_dark)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(), MessageHolderActivity.class));
            }
        };

        xTitle.addAction(messageAction);
        ivMessageTag = (ImageView) xTitle.getViewByAction(messageAction);

        final int[] tabTypes = {InfomationTabFragment.INFOMATION_TYPE_GET_ASK_NEWS,
                InfomationTabFragment.INFOMATION_TYPE_SUN_BUSINESS,
                InfomationTabFragment.INFOMATION_TYPE_PRIVATE_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_SKY_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_POLICY_DYNAMICS};

        vpInfomation.setOffscreenPageLimit(7);

        vpInfomation.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? new InfoMyStockTabFragment() : (position == 1 ? new SevenBy24Fragment() :
                        InfomationTabFragment.newInstance(tabTypes[position - 2]));
            }

            @Override
            public int getCount() {
                return infoArrays.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return infoArrays[position];
            }
        });
        tabInfomation.setupWithViewPager(vpInfomation);

        badge = new BadgeView(getActivity());
        initBadge(unReadCount, badge);
    }

    private void initBadge(int num, BadgeView badge) {
        badge.setGravityOffset(10, 7, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(num);
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
