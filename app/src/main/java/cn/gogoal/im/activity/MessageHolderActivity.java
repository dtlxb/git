package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;

import org.simple.eventbus.Subscriber;

import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.ContactsFragment;
import cn.gogoal.im.fragment.main.MessageFragment;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;

/**
 * Created by huangxx on 2017/6/16.
 */

public class MessageHolderActivity extends BaseActivity {

    @BindView(R.id.tab_chat)
    TabLayout tabChat;

    @BindView(R.id.vp_chat_tab)
    UnSlidingViewPager vpChatTab;

    @BindArray(R.array.chat_tab)
    String[] chatTabsArray;

    private MessageFragment messageFragment;
    private ContactsFragment contactsFragment;
    private BadgeView badge;
    private int unReadCount;

    @Override
    public int bindLayout() {
        return R.layout.activity_message_holder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    @Override
    public void doBusiness(Context mContext) {
        messageFragment = new MessageFragment();
        contactsFragment = new ContactsFragment();
        MessageHolderTabAdapter messageHolderTabAdapter = new MessageHolderTabAdapter(getSupportFragmentManager());
        vpChatTab.setAdapter(messageHolderTabAdapter);
        tabChat.setupWithViewPager(vpChatTab);

        for (int i = 0; i < chatTabsArray.length; i++) {
            TabLayout.Tab tab = tabChat.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(messageHolderTabAdapter.getTabView(i));
            }
        }
        tabChat.getTabAt(0).select();
        badge = new BadgeView(MessageHolderActivity.this);
        initBadge(unReadCount, badge);
    }


    /**
     * 自选股、行情切换adapter
     */
    private class MessageHolderTabAdapter extends FragmentPagerAdapter {

        private MessageHolderTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? messageFragment : contactsFragment;
        }

        @Override
        public int getCount() {
            return chatTabsArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return chatTabsArray[position];
        }

        private View getTabView(int position) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate
                    (R.layout.tab_layout_item, new LinearLayout(getActivity()), false);

            TextView tv = (TextView) view.findViewById(R.id.tv_main_tab);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_main_tab);
            switch (position) {
                case 0:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_message);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.selector_icon_main_tab_mine);
                    break;
                default:
                    break;
            }
            tv.setText(chatTabsArray[position]);
            return view;
        }
    }

    private void initBadge(int num, BadgeView badge) {
        int moveX = AppDevice.getWidth(MessageHolderActivity.this) / 4 - AppDevice.dp2px(MessageHolderActivity.this, 32);
        badge.setGravityOffset(moveX, 0, false);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(12, true);
        badge.bindTarget(tabChat.getTabAt(0).getCustomView());
        badge.setBadgeNumber(num);
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

    /**
     * 消息删除
     */
    @Subscriber(tag = "Decrease_Message_Count")
    public void decreaseMessage(String count) {
        unReadCount -= Integer.parseInt(count);
        badge.setBadgeNumber(unReadCount);
    }
}
