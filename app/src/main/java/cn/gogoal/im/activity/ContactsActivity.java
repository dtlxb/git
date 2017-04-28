package cn.gogoal.im.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
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
import cn.gogoal.im.adapter.ContactAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.index.SuspendedDecoration;

/**
 * 联系人
 */
public class ContactsActivity extends BaseActivity {

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;

    private boolean added = true;
    private ContactAdapter contactAdapter;
    private ArrayList<ContactBean> contactBeanList;

    public ContactsActivity() {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_contacts, true);

        ViewGroup.LayoutParams tvParams = tvConstactsFlag.getLayoutParams();
        tvParams.width = AppDevice.getWidth(getActivity()) / 4;
        tvParams.height = AppDevice.getWidth(getActivity()) / 4;
        tvConstactsFlag.setLayoutParams(tvParams);

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        contactBeanList = new ArrayList<>();

        //添加联系人功能头部item
        addContactHead();

        getData();//联系人列表数据

        contactAdapter = new ContactAdapter(getActivity(), contactBeanList);

        contactAdapter.notifyDataSetChanged();

        rvContacts.addItemDecoration(new NormalItemDecoration(mContext, Color.parseColor("#f8f8f8")));

        rvContacts.setAdapter(contactAdapter);

        contactAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent;
                //单聊处理
                if (position == 0) {
                    intent = new Intent(getActivity(), IMNewFrienActivity.class);
                    intent.putExtra("add_type", 0x01);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(getActivity(), SquareCollectActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), SingleChatRoomActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("conversation_id", contactBeanList.get(position).getConv_id());
                    bundle.putString("nickname", contactBeanList.get(position).getNickname());
                    bundle.putBoolean("need_update", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        contactAdapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(CommonAdapter adapter, View view, final int position) {
                if (position > 1) {
                    DialogHelp.getSelectDialog(getActivity(), "", new String[]{"删除联系人"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //服务器中清除
                            deleteFriend(contactBeanList.get(position).getFriend_id());
                            //缓存中清除
                            UserUtils.deleteContactsSomeone(contactBeanList.get(position).getFriend_id());
                            //刷新列表
                            contactBeanList.remove(position);
                            contactAdapter.notifyDataSetChanged();
                        }
                    }, false).show();
                }
                return false;
            }
        });

    }

    public void srcollShowIndexBar(boolean show) {
        indexBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void getData() {
        //缓存的联系人请求数据
        String friendResponseInfo = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", "");
        if (friendResponseInfo.equals("")) {
            getFriendList(contactBeanList);
        } else {
            if (!JSONObject.parseObject(friendResponseInfo).getJSONArray("data").isEmpty()) {
                parseContactDatas(friendResponseInfo, contactBeanList);
            }
        }
    }

    /**
     * 添加联系人列表头部
     */
    private void addContactHead() {
        contactBeanList.add(addFunctionHead("新朋友", R.mipmap.cache_img_contacts_0));
        contactBeanList.add(addFunctionHead("我的群组", R.mipmap.cache_img_contacts_1));
        /*contactBeanList.add(addFunctionHead("标签", R.mipmap.cache_img_contacts_2));
        contactBeanList.add(addFunctionHead("公众号", R.mipmap.cache_img_contacts_3));*/
    }

    private ContactBean<Integer> addFunctionHead(String name, @DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setRemark(name);
        bean.setBaseIndexTag("↑");
        bean.setContactType(ContactBean.ContactType.FUNCTION_ITEM);
        bean.setAvatar(iconId);
        return bean;
    }

    private void deleteFriend(int friendID) {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("friend_id", String.valueOf(friendID));
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    UIHelper.toast(getActivity(), "此人删除成功");
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.del_friend, ggHttpInterface).startGet();
    }

    private void getFriendList(final List<ContactBean> contactBeanList) {

        Map<String, String> param = new HashMap<>();

        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", responseInfo);
                    parseContactDatas(responseInfo, contactBeanList);

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", "{\"code\":0,\"data\":[],\"message\":\"成功\"}");
                } else {
                    UIHelper.toastError(getActivity(), GGOKHTTP.getMessage(responseInfo));
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FRIEND_LIST, ggHttpInterface).startGet();
    }

    private void parseContactDatas(String responseInfo, List<ContactBean> contactBeanList) {
        List<ContactBean<String>> list = new ArrayList<>();
        BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                responseInfo,
                new TypeReference<BaseBeanList<ContactBean<String>>>() {
                });
        list.clear();
        list.addAll(beanList.getData());

        for (ContactBean<String> bean : list) {
            bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        }

        contactBeanList.addAll(list);

        KLog.e(contactBeanList);

        SuspendedDecoration mDecoration = new SuspendedDecoration(getActivity());

        mDecoration.setmDatas(contactBeanList);

        indexBar.setmSourceDatas(contactBeanList)//设置数据
                .invalidate();

        if (added) {
            rvContacts.addItemDecoration(mDecoration);
            added = false;
        }
    }
}
