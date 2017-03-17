package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private PersonInfoAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_imperson;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("聊天详情", true);
        initRecycleView(personlistRecycler, null);
        ContactBean contactBean = (ContactBean) getIntent().getSerializableExtra("seri");
        contactBeens.add(contactBean);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mPersonInfoAdapter = new PersonInfoAdapter(IMPersonActivity.this, R.layout.item_contact_info, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
    }

    @OnClick({R.id.tv_jumpto_search, R.id.getmessage_swith})
    void function(View view) {
        switch (view.getId()) {
            case R.id.tv_jumpto_search:

                break;
            case R.id.getmessage_swith:
                break;
        }
    }

    private class PersonInfoAdapter extends CommonAdapter<ContactBean> {

        public PersonInfoAdapter(Context context, int layoutId, List<ContactBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ContactBean contactBean, int position) {
            holder.setText(R.id.contact_name, contactBean.getNickname());
            ImageDisplay.loadNetImage(IMPersonActivity.this, (String) contactBean.getAvatar(), (ImageView) holder.getView(R.id.contact_head));
        }
    }


}
