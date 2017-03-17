package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;


/**
 * author wangjd on 2017/3/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :搜索.
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.recycler_result)
    RecyclerView recyclerResult;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("查找聊天", true);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
