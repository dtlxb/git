package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private PersonInfoAdapter mPersonInfoAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_imperson;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("聊天详情", true);
        initRecycleView(personlistRecycler, R.drawable.shape_divider_1px);

        //初始化
        personlistRecycler.setLayoutManager(new LinearLayoutManager(
                IMPersonActivity.this, LinearLayoutManager.VERTICAL, false));


    }

    private class PersonInfoAdapter extends CommonAdapter<ContactBean> {

        public PersonInfoAdapter(Context context, int layoutId, List<ContactBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ContactBean data, int position) {

        }
    }
}
