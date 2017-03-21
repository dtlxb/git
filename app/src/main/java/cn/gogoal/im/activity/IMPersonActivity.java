package cn.gogoal.im.activity;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.NineGridImageViewAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.ui.view.NineGridImageView;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_imperson;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        initRecycleView(personlistRecycler, null);
        ContactBean contactBean = (ContactBean) getIntent().getSerializableExtra("seri");
        contactBeens.add(contactBean);

        contactBeens.add(addFunctionHead("", R.mipmap.person_add));

        KLog.e(contactBeens);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        mPersonInfoAdapter = new IMPersonSetAdapter(IMPersonActivity.this, R.layout.item_grid_foundfragment, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);

        mPersonInfoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == contactBeens.size() - 1) {

                } else {
                    Intent intent = new Intent(IMPersonActivity.this, IMPersonDetailActivity.class);
                    intent.putExtra("friend_id", contactBeens.get(position).getFriend_id());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private ContactBean<Integer> addFunctionHead(String name, @DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setRemark(name);
        bean.setContactType(ContactBean.ContactType.FUNCTION_ITEM);
        bean.setAvatar(iconId);
        return bean;
    }

    @OnClick({R.id.tv_do_search_conversation, R.id.getmessage_swith})
    void function(View view) {
        switch (view.getId()) {
            case R.id.tv_do_search_conversation:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.getmessage_swith:
                break;
        }
    }

}
