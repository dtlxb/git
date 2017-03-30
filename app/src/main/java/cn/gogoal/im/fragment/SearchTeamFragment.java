package cn.gogoal.im.fragment;


import android.content.Context;
import android.os.Bundle;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

public class SearchTeamFragment extends BaseFragment {

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_team;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    public static SearchTeamFragment getInstance(String keyword){
        SearchTeamFragment fragment=new SearchTeamFragment();
        Bundle bundle=new Bundle();
        bundle.putString("keyword",keyword);
        fragment.setArguments(bundle);
        return fragment;
    }
}
