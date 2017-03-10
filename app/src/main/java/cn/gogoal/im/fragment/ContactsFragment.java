package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.adapter.ContactAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.index.SuspendedDecoration;

/**
 * 联系人
 */
public class ContactsFragment extends BaseFragment {

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;
    private ContactAdapter contactAdapter;

    public ContactsFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_contacts);

        ViewGroup.LayoutParams tvParams = tvConstactsFlag.getLayoutParams();
        tvParams.width = AppDevice.getWidth(getContext()) / 4;
        tvParams.height = AppDevice.getWidth(getContext()) / 4;
        tvConstactsFlag.setLayoutParams(tvParams);

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        final List<ContactBean> contactBeanList = new ArrayList<>();

        getData(contactBeanList);//列表数据

        contactAdapter = new ContactAdapter(getContext(), contactBeanList);

        contactAdapter.notifyDataSetChanged();

        contactAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //单聊处理
                Intent intent = new Intent(getContext(), SingleChatRoomActivity.class);
                intent.putExtra("friend_id", contactBeanList.get(position).getFriend_id() + "");
                intent.putExtra("conversation_id", contactBeanList.get(position).getConv_id());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void getData(List<ContactBean> contactBeanList) {
        contactBeanList.add(addFunctionHead("新的朋友", R.mipmap.cache_img_contacts_0));
        contactBeanList.add(addFunctionHead("群聊", R.mipmap.cache_img_contacts_1));
        contactBeanList.add(addFunctionHead("标签", R.mipmap.cache_img_contacts_2));
        contactBeanList.add(addFunctionHead("公众号", R.mipmap.cache_img_contacts_3));
        //添加模拟好友数据
//        contactBeanList.addAll(getConstactsData());
        getFriendList(contactBeanList);
    }

    private void getFriendList(final List<ContactBean> contactBeanList) {

        Map<String, String> param = new HashMap<>();

        param.put("token", AppConst.LEAN_CLOUD_TOKEN);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                SPTools.saveString(AppConst.LEAN_CLOUD_TOKEN + "_Contacts", responseInfo);

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<ContactBean<String>>>() {
                            });
                    List<ContactBean<String>> list = beanList.getData();

                    for (ContactBean<String> bean : list) {
                        bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
                    }

                    contactBeanList.addAll(list);

                    SuspendedDecoration mDecoration = new SuspendedDecoration(getContext(), contactBeanList);

                    indexBar.setmSourceDatas(contactBeanList)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(contactBeanList);

                    rvContacts.addItemDecoration(mDecoration);

                    //如果add两个，那么按照先后顺序，依次渲染。
                    DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
                    itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_divider_recyclerview_1px));
                    rvContacts.addItemDecoration(itemDecoration);

                    rvContacts.setAdapter(contactAdapter);

                }else {
                    UIHelper.toastErro(getContext(),GGOKHTTP.getMessage(responseInfo));
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                UIHelper.toastErro(getContext(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FRIEND_LIST, ggHttpInterface).startGet();
    }

    private ContactBean<Integer> addFunctionHead(String name, @DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setRemark(name);
        bean.setBaseIndexTag("↑");
        bean.setContactType(ContactBean.ContactType.FUNCTION_ITEM);
        bean.setAvatar(iconId);
        return bean;
    }
}
