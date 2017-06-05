package cn.gogoal.im.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.database.crud.DataSupport;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.index.SuspendedDecoration;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.XTitle;

/**
 * 联系人
 */
public class ContactsActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvContactsFlag;

    @BindView(R.id.tv_to_search)
    DrawableCenterTextView tvSearch;

    private boolean added = true;
    private ContactAdapter contactAdapter;
    private ArrayList<ContactBean> contactBeanList;

    private TextView textViewFooter;

    @Override
    public int bindLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setMyTitle(R.string.title_contacts, true);
        XTitle.TextAction textAction = new XTitle.TextAction("添加") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, SearchPersonSquareActivity.class);
                intent.putExtra("search_index", 0);
                startActivity(intent);
            }
        };
        xTitle.addAction(textAction, 0);
        TextView RightText = (TextView) xTitle.getViewByAction(textAction);
        RightText.setTextColor(getResColor(R.color.textColor_333333));

        ViewGroup.LayoutParams tvParams = tvContactsFlag.getLayoutParams();
        tvParams.width = AppDevice.getWidth(getActivity()) / 4;
        tvParams.height = AppDevice.getWidth(getActivity()) / 4;
        tvContactsFlag.setLayoutParams(tvParams);

        textViewFooter = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        textViewFooter.setPadding(0, AppDevice.dp2px(mContext, 15), 0, AppDevice.dp2px(mContext, 45));
        textViewFooter.setLayoutParams(params);
        textViewFooter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textViewFooter.setGravity(Gravity.CENTER);
        textViewFooter.setTextColor(getResColor(R.color.textColor_666666));

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvContactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        contactBeanList = new ArrayList<>();

        //添加联系人功能头部item
        addContactHead();

        getData();//联系人列表数据

        contactAdapter = new ContactAdapter(getActivity(), contactBeanList, 0);

        contactAdapter.addFooterView(textViewFooter);

        contactAdapter.notifyDataSetChanged();

        rvContacts.addItemDecoration(new NormalItemDecoration(getActivity(),
                getResColor(R.color.colorDivider_d9d9d9)));

        rvContacts.setAdapter(contactAdapter);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IMSearchLocalActivity.class);
                startActivity(intent);
            }
        });

        contactAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent;
                //单聊处理
                if (position == 0) {
                    intent = new Intent(ContactsActivity.this, IMNewFriendActivity.class);
                    intent.putExtra("add_type", 0x01);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(ContactsActivity.this, MyGroupsActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(ContactsActivity.this, PhoneContactsActivity.class);
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
                if (position > 2) {
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
        if (TextUtils.isEmpty(friendResponseInfo)) {
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
        contactBeanList.add(addFunctionHead("新朋友", R.mipmap.contacts_new_friend));
        contactBeanList.add(addFunctionHead("我的群组", R.mipmap.group_contacts));
        contactBeanList.add(addFunctionHead("手机通讯录", R.mipmap.chat_phone_contacts));
        //contactBeanList.add(addFunctionHead("公众号", R.mipmap.cache_img_contacts_3));
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
                    upDataFootCount(UserUtils.getUserContacts());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.del_friend, ggHttpInterface).startGet();
    }

    /**
     * 更新底部好友人数统计
     */
    public void upDataFootCount(List<ContactBean> list) {
        if (list.size() > 0) {
            textViewFooter.setText(String.format(getString(R.string.str_friends_count), list.size()));
        } else {
            textViewFooter.setText("你还没有好友，赶快去添加一些吧");
        }
    }

    private void getFriendList(final List<ContactBean> contactBeanList) {

        Map<String, String> param = new HashMap<>();

        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    //SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", responseInfo);
                    UserUtils.saveContactInfo(responseInfo);
                    parseContactDatas(responseInfo, contactBeanList);

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    UserUtils.saveContactInfo("{\"code\":0,\"data\":[],\"message\":\"成功\"}");
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
        List<ContactBean> list = new ArrayList<>();
        /*BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                responseInfo,
                new TypeReference<BaseBeanList<ContactBean<String>>>() {
                });*/

        BaseBeanList<UserBean<String>> users = JSONObject.parseObject(
                responseInfo,
                new TypeReference<BaseBeanList<UserBean<String>>>() {
                });

        //缓存到数据库
        list.clear();
        for (int i = 0; i < users.getData().size(); i++) {
            users.getData().get(i).save();
            list.add(new ContactBean());
        }

        /*list.clear();
        list.addAll(beanList.getData());*/

        upDataFootCount(list);

        for (ContactBean bean : list) {
            bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        }

        contactBeanList.addAll(list);

        SuspendedDecoration mDecoration = new SuspendedDecoration(getActivity());

        mDecoration.setmDatas(contactBeanList);

        indexBar.setmSourceDatas(contactBeanList)//设置数据
                .invalidate();

        if (added) {
            rvContacts.addItemDecoration(mDecoration);

            added = false;
        }

        KLog.e(DataSupport.findFirst(UserBean.class));

    }
}
