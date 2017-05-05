package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;

/**
 * Created by huangxx on 2017/5/3.
 */

public class EmojiFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    private Context mContext;

    public EmojiFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        ImageView imageView = new ImageView(mContext);
        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.selector_emoji_choese);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.chat_add);
                break;
            default:
                break;
        }

        return imageView;
    }
}
