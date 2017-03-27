package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.OnItemClickLitener;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;

/**
 * Created by huangxx on 2017/3/16.
 */

public class IMPersonActivity extends BaseActivity {

    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private String conversationId;
    private List<String> idList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();

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
//        contactBeens.add(contactBean);

        parseContactDatas(SPTools.getString(AppConst.LEAN_CLOUD_TOKEN + "_contact_beans", ""));

        contactBeens.add(addFunctionHead("", R.mipmap.person_add));

        for (int i = 0; i < contactBeens.size() - 1; i++) {
            imageList.add(contactBeens.get(i).getAvatar().toString());
            KLog.e(contactBeens.get(i).getFriend_id());
            idList.add(String.valueOf(contactBeens.get(i).getFriend_id()));
        }
        idList.add(AppConst.LEAN_CLOUD_TOKEN);
        KLog.e(contactBeens);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        mPersonInfoAdapter = new IMPersonSetAdapter(IMPersonActivity.this, R.layout.item_grid_foundfragment, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);

        mPersonInfoAdapter.setOnItemClickListener(new OnItemClickLitener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                if (position == contactBeens.size() - 1) {
                    createChatGroup(idList);
                } else {
                    Intent intent = new Intent(IMPersonActivity.this, IMPersonDetailActivity.class);
                    intent.putExtra("friend_id", contactBeens.get(position).getFriend_id());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(RecyclerView.ViewHolder holder, View view, int position) {
                return false;
            }
        });

    }

    //群头像测试
    private void parseContactDatas(String responseInfo) {
        if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
            BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                    responseInfo,
                    new TypeReference<BaseBeanList<ContactBean<String>>>() {
                    });
            List<ContactBean<String>> list = beanList.getData();

            for (ContactBean<String> bean : list) {
                bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
            }

            contactBeens.addAll(list);
        }
    }

    //创建群组
    public void createChatGroup(List<String> idList) {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(idList));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(IMPersonActivity.this, "群组创建成功!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CREATE_GROUP_CHAT, ggHttpInterface).startGet();
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
                intent.putExtra("conversation_id", conversationId);
                startActivity(intent);
                startActivity(new Intent(getActivity(), SearchActivity.class));
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
            layoutParams.width = AppDevice.getWidth(getmContext()) / 5;
            layoutParams.height = AppDevice.getWidth(getmContext()) / 4;
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.absoluteWhite));
            view.setLayoutParams(layoutParams);

            AppCompatImageView imageIcon = holder.getView(R.id.iv);
            ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
            viewParams.width = AppDevice.getWidth(getmContext()) / 8;
            viewParams.height = AppDevice.getWidth(getmContext()) / 8;
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
