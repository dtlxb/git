package cn.gogoal.im.activity;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
import cn.gogoal.im.common.AppDevice;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private PersonInfoAdapter mPersonInfoAdapter;

    private List<ContactBean> contactBeens = new ArrayList<>();
    private String conversationId;

    @Override
    public int bindLayout() {
        return R.layout.activity_imperson;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        initRecycleView(personlistRecycler, null);
        ContactBean contactBean = (ContactBean) getIntent().getSerializableExtra("seri");
        conversationId = getIntent().getStringExtra("conversation_id");
        contactBeens.add(contactBean);
        contactBeens.add(addFunctionHead("", R.mipmap.person_add));
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        mPersonInfoAdapter = new PersonInfoAdapter(IMPersonActivity.this, R.layout.item_grid_foundfragment, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
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
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("conversation_id",conversationId);
                startActivity(intent);
                break;
            case R.id.getmessage_swith:
                break;
        }
    }

    private class PersonInfoAdapter extends CommonAdapter<ContactBean> {

        private PersonInfoAdapter(Context context, int layoutId, List<ContactBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ContactBean contactBean, int position) {

            final View view = holder.getView(R.id.layout_grid);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = AppDevice.getWidth(getContext()) / 5;
            layoutParams.height = AppDevice.getWidth(getContext()) / 4;
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.absoluteWhite));
            view.setLayoutParams(layoutParams);

            ImageView imageIcon = holder.getView(R.id.iv);
            ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
            viewParams.width = AppDevice.getWidth(getContext()) / 8;
            viewParams.height = AppDevice.getWidth(getContext()) / 8;
            imageIcon.setLayoutParams(viewParams);

            Object avatar = contactBean.getAvatar();
            holder.setText(R.id.tv, contactBean.getNickname());

            if (avatar instanceof String) {
                holder.setImageUrl(R.id.iv, avatar.toString());
            } else if (avatar instanceof Integer) {
                holder.setImageResource(R.id.iv, (Integer) avatar);
            }
        }
    }


}
