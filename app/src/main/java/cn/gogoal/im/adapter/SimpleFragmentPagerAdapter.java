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
 * author wangjd on 2017/2/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 主界面Fragment适配器
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    private String[] titles;

    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        this.context=context;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public View getTabView(int position) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.tab_layout_item,new LinearLayout(context),false);
        TextView tv = (TextView) view.findViewById(R.id.tv_main_tab);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_main_tab);
        switch (position) {
            case 0:
//                    if (!TextUtils.isEmpty(url)){//底部按钮icon图片可以为后台提供(节假日纪念日活动)
//                    ImageDisplay.loadNetImage(getActivity(),url,imageView);
//                    }else {
                imageView.setImageResource(R.drawable.selector_icon_main_tab_message);
//                    }
                break;
            case 1:
                imageView.setImageResource(R.drawable.selector_icon_main_tab_my_stock);
                break;
            case 2:
                imageView.setImageResource(R.drawable.selector_icon_main_tab_function);
                break;
            case 3:
                imageView.setImageResource(R.drawable.selector_icon_main_tab_contact);
                break;
            case 4:
                imageView.setImageResource(R.drawable.selector_icon_main_tab_mine);
                break;
        }

        tv.setText(titles[position]);

        return view;
    }
}
