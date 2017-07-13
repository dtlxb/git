package cn.gogoal.im.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.gogoal.im.fragment.infomation.FocusNewsFragment;
import cn.gogoal.im.fragment.infomation.InfomationTabFragment;
import cn.gogoal.im.fragment.infomation.SevenBy24Fragment;

/**
 * author wangjd on 2017/7/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :$资讯tab适配器
 */
public class InfoTabAdapter extends FragmentPagerAdapter {

    private int[] tabTypes;
    private String[] infoArrays;

    public InfoTabAdapter(FragmentManager fm, int[] tabTypes, String[] infoArrays) {
        super(fm);
        this.tabTypes = tabTypes;
        this.infoArrays = infoArrays;
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0 ? new SevenBy24Fragment() : (
                position == 1 ? new FocusNewsFragment() :
                        InfomationTabFragment.newInstance(tabTypes[position - 2])));
    }

    @Override
    public int getCount() {
        return infoArrays.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return infoArrays[position];
    }

//    public View getTabView(int pos) {
//        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_tab_infomation, new LinearLayout(context), false);
//        textView.setText(infoArrays[pos]);
//        if (textView.isSelected()) {
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        } else {
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//        }
//        return textView;
//    }
}
