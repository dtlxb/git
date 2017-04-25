package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/4/21.
 */

public class IMGroupContactsActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private String squareCreater;
    private List<ContactBean> contactBeens = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_group_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_square_contacts, true);

        contactBeens = (List<ContactBean>) getIntent().getSerializableExtra("chat_group_contacts");
        squareCreater = getIntent().getStringExtra("square_creater");
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        mPersonInfoAdapter = new IMPersonSetAdapter(1002, IMGroupContactsActivity.this, R.layout.item_square_chat_set, squareCreater, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);

        mPersonInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent = new Intent(IMGroupContactsActivity.this, IMPersonDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("account_id", contactBeens.get(position).getFriend_id());
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
    }
}
