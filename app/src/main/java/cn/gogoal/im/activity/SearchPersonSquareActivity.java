package cn.gogoal.im.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.socks.library.KLog;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.IconFontUtils;
import cn.gogoal.im.fragment.SearchPersionFragment;
import cn.gogoal.im.fragment.SearchTeamFragment;
import cn.gogoal.im.ui.KeyboardLaunchLinearLayout;

/**
 * Created by huangxx on 2017/3/28.
 */

public class SearchPersonSquareActivity extends BaseActivity {

    @BindView(R.id.root)
    KeyboardLaunchLinearLayout keyboardLayout;

    @BindArray(R.array.searchTitle)
    String[] searchTitle;

    @BindView(R.id.tab_search_persion_team)
    TabLayout tabSearchPersionTeam;

    @BindView(R.id.layout_2search)
    AppCompatEditText layout2search;

    @BindView(R.id.vp_search_persion_team)
    ViewPager vpSearchPersionTeam;

    @Override
    public int bindLayout() {
        return R.layout.activity_search_person_square;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.square_collcet_add, true);

        IconFontUtils.setFont(mContext, layout2search, "iconfont/search.ttf");

        final int searchIndex = getIntent().getIntExtra("search_index", 0);

        SearchPersionFragment persionFragment = new SearchPersionFragment();
        SearchTeamFragment teamFragment = new SearchTeamFragment();

        final Fragment[] fragments = new Fragment[]{persionFragment, teamFragment};

        vpSearchPersionTeam.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return searchTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return searchTitle[position];
            }
        });

        tabSearchPersionTeam.setupWithViewPager(vpSearchPersionTeam);

        try {
            tabSearchPersionTeam.getTabAt(searchIndex).select();
        } catch (NullPointerException e) {
            KLog.e("Tab不存在");
        }

        layout2search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(layout2search.getText())) {
                        AppManager.getInstance().sendMessage("SEARCH_PERSION_TAG", layout2search.getText().toString());
                        AppDevice.hideSoftKeyboard(layout2search);
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
