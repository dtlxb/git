package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private String conversationId;
    private String nickname;

    @Override
    public int bindLayout() {
        return R.layout.activity_imperson;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        initRecycleView(personlistRecycler, null);
        final ContactBean contactBean = (ContactBean) getIntent().getSerializableExtra("seri");
        conversationId = getIntent().getStringExtra("conversation_id");
        nickname = getIntent().getStringExtra("nickname");
        contactBeens.add(contactBean);
        contactBeens.add(addFunctionHead("", R.mipmap.person_add));

        KLog.e(contactBeens);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        //单聊的传群创建者为空字符
        mPersonInfoAdapter = new IMPersonSetAdapter(1001, IMPersonActivity.this, R.layout.item_square_chat_set, "", contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);

        mPersonInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent;
                if (position == contactBeens.size() - 1) {
                    intent = new Intent(IMPersonActivity.this, ChooseContactActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("square_action", AppConst.CREATE_SQUARE_ROOM_BY_ONE);
                    mBundle.putSerializable("seri", contactBean);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else {
                    intent = new Intent(IMPersonActivity.this, IMPersonDetailActivity.class);
                    intent.putExtra("account_id", contactBeens.get(position).getUserId());
                    startActivity(intent);
                }
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

    @OnClick({R.id.tv_do_search_conversation})
    void function(View view) {
        switch (view.getId()) {
            case R.id.tv_do_search_conversation:
                Intent intent = new Intent(getActivity(), SearchMessagesActivity.class);
                intent.putExtra("conversation_id", conversationId);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                break;
            /*case R.id.getmessage_swith:
                break;*/
            default:
                break;
        }
    }

}
