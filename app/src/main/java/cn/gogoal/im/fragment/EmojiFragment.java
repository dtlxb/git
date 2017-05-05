package cn.gogoal.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;

/**
 * Created by huangxx on 2017/5/3.
 */

public class EmojiFragment extends Fragment {

    //这个是有多少个 fragment页面
    private static final int NUM_ITEMS = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji, null);
        ViewPager viewPage = (ViewPager) view.findViewById(R.id.emoji_pager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ArrayEmojiFragment.newInstance(0));
        fragmentList.add(ArrayEmojiFragment.newInstance(1));
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        viewPage.setAdapter(adapter);
        return view;
    }

    /**
     * 有状态的 ，只会有前3个存在 其他销毁， 前1个， 中间， 下一个
     */
    private static class MyAdapter extends FragmentStatePagerAdapter {
        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        //得到每个item
        @Override
        public Fragment getItem(int position) {
            return ArrayEmojiFragment.newInstance(position);
        }


        // 初始化每个页卡选项
        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            return super.instantiateItem(arg0, arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }
}