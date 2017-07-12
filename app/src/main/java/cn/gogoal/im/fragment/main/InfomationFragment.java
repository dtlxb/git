package cn.gogoal.im.fragment.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;

import org.simple.eventbus.Subscriber;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.adapter.InfoTabAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.infomation.InfomationTabFragment;
import cn.gogoal.im.ui.Badge.BadgeView;

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

    @BindView(R.id.iv_calendar)
    ImageView ivCalendar;

    @BindView(R.id.iv_message)
    ImageView ivMessageTag;

    private InfoTabAdapter infoTabAdapter;
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
    public void doBusiness(final Context mContext) {

        final int[] tabTypes = {
//                InfomationTabFragment.INFOMATION_TYPE_GET_ASK_NEWS,
                InfomationTabFragment.INFOMATION_TYPE_PRIVATE_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_SUN_BUSINESS,
                InfomationTabFragment.INFOMATION_TYPE_SKY_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_POLICY_DYNAMICS};

        infoTabAdapter = new InfoTabAdapter(getChildFragmentManager(),
                mContext,
                tabTypes,
                infoArrays);

        vpInfomation.setAdapter(infoTabAdapter);

        tabInfomation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ivCalendar.setVisibility(tab.getPosition() == 1 ? View.VISIBLE : View.GONE);
                TextView tabTextView = (TextView) tab.getCustomView();
                if (tabTextView!=null){
                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabTextView = (TextView) tab.getCustomView();
                if (tabTextView!=null){
                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                }
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tabInfomation.setupWithViewPager(vpInfomation);

        for (int i=0;i<infoArrays.length;i++){
            TabLayout.Tab tab = tabInfomation.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(infoTabAdapter.getTabView(i));
            }
        }

        vpInfomation.setOffscreenPageLimit(7);

        badge = new BadgeView(getActivity());
        initBadge(unReadCount, badge);
    }

    private void initBadge(int num, BadgeView badge) {
        badge.setGravityOffset(7, 7, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(num);
    }

    @OnClick({R.id.iv_message, R.id.iv_calendar, R.id.layout_title_infomation})
    void click(View view) {
        switch (view.getId()) {
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), MessageHolderActivity.class));
                break;
            case R.id.iv_calendar:
                showDateDialog();
                break;
            case R.id.layout_title_infomation:
                AppManager.getInstance().sendMessage("double_click_2_top",
                        "" + tabInfomation.getSelectedTabPosition());
                break;
        }
    }

    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                AppManager.getInstance().sendMessage("focus_news_in_date", year + "-" +
                        StringUtils.formatInteger(month + 1) + "-" +
                        StringUtils.formatInteger(dayOfMonth));
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        AVIMConversation conversation = (AVIMConversation) map.get("conversation");
        //获取免打扰
        List<String> muList = (List<String>) conversation.get("mu");
        boolean noBother = muList.contains(UserUtils.getMyAccountId());
        if (!noBother) {
            unReadCount++;
        }
        badge.setBadgeNumber(unReadCount);
    }

}
