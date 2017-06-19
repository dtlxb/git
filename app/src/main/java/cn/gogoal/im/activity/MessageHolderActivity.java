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

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
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

    private BadgeView badge;
    private MessageFragment messageFragment;
    private ContactsFragment contactsFragment;
    private String[] chatTabs = {"消息", "通讯录"};

    @Override
    public int bindLayout() {
        return R.layout.activity_message_holder;
    }

    @Override
    public void doBusiness(Context mContext) {
        messageFragment = new MessageFragment();
        contactsFragment = new ContactsFragment();
        MessageHolderTabAdapter messageHolderTabAdapter = new MessageHolderTabAdapter(getSupportFragmentManager());
        vpChatTab.setAdapter(messageHolderTabAdapter);
        tabChat.setupWithViewPager(vpChatTab);

        for (int i = 0; i < chatTabs.length; i++) {
            TabLayout.Tab tab = tabChat.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(messageHolderTabAdapter.getTabView(i));
            }
        }

        tabChat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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
            return chatTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return chatTabs[position];
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
            }
            tv.setText(chatTabs[position]);
            return view;
        }
    }

    @Subscriber(tag = "correct_allmessage_count")
    public void setBadgeViewNum(BaseMessage<Integer> message) {
        int index = message.getOthers().get("index");
        int num = message.getOthers().get("number");

        initBadge(index, num);

        if (index >= 0 && index < chatTabs.length) {
            if (num > 0) {
                badge.setBadgeNumber(num);
            } else {
                badge.hide(false);
            }
        }
    }

    private void initBadge(int index, int num) {
        if (badge != null) {
            if (num == 0) {
                badge.hide(false);
                //TODO
            } else {
                badge.bindTarget(tabChat.getTabAt(index).getCustomView().findViewById(R.id.layout_badge));
                String uriStr = "android.resource://" + this.getPackageName() + "/" + R.raw.ding;
//                VoiceManager.getInstance(MainActivity.this)
//                        .startPlay(Uri.parse(uriStr));
            }
        } else {
            badge = new BadgeView(getActivity());
            badge.setGravityOffset(0, 0, true);
            badge.setShowShadow(false);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            badge.setBadgeTextSize(12, true);
            badge.setBadgePadding(5, true);
            badge.bindTarget(tabChat.getTabAt(index).getCustomView().findViewById(R.id.layout_badge));
        }
    }
}
