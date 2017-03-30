package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.promeg.pinyinhelper.Pinyin;
import com.hply.imagepicker.view.SuperCheckBox;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.index.SuspendedDecoration;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.NoAlphaItemAnimator;


/**
 * author wangjd on 2017/3/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :$好友选择——加群.
 */
public class ChooseContactActivity extends BaseActivity {

    private ICheckItemListener<ContactBean> listener;

    private int actionType;
    private List<ContactBean> userContacts;
    private ContactBean itContactBean;

    public void setOnItemCheckListener(ICheckItemListener<ContactBean> listener) {
        this.listener = listener;
    }

    @BindView(R.id.rv_selected_contacts)
    RecyclerView rvSelectedContacts;

    @BindView(R.id.search_choose)
    SearchView searchChoose;

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;

    @BindView(R.id.rv_del)
    RecyclerView rvDel;

    private XTitle title;

    XTitle.TextAction textAction;

    private ChooseAdapter contactAdapter;

//    private int topListMaxWidth;

    private Map<Integer, ContactBean> result = new LinkedHashMap<>();

    private SelectedAdapter selectedAdapter;

    private List<ContactBean> mSelectedTeamMemberAccounts = new ArrayList<>();//已经在群中的成员账号

    @Override
    public int bindLayout() {
        return R.layout.activity_choose_contaces;
    }

    @Override
    public void doBusiness(final Context mContext) {
        title = setMyTitle(R.string.title_group_chat, true);

        rvContacts.setItemAnimator(new NoAlphaItemAnimator());
        rvSelectedContacts.setItemAnimator(new NoAlphaItemAnimator());

        /*
         * 选择好友类型：
         * 1101.选择好友直接创建群，
         * 1100.选择好友和单聊好友创建群，
         * 1102.原来存在的群继续添加好友，
         * 1103.原来存在的群中移除好友
         * */
        actionType = getIntent().getIntExtra("square_action", 0);

        KLog.e(actionType);
        //actionType = 1102,1103,
        String teamId = getIntent().getStringExtra("conversation_id");

        if (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
            rvDel.setVisibility(View.VISIBLE);
            rvDel.addItemDecoration(new NormalItemDecoration(mContext));
            rvDel.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rvDel.setAdapter(new DeleteAdapter(UserUtils.getFriendsInTeam(teamId)));
        }

        //1100
        itContactBean = (ContactBean) getIntent().getSerializableExtra("seri");

        if (actionType == AppConst.SQUARE_ROOM_ADD_ANYONE || actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
            mSelectedTeamMemberAccounts = UserUtils.getFriendsInTeam(teamId);
        }

        textAction = initTitleAction(teamId);

        title.addAction(textAction);

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        AppDevice.setViewWidth$Height(tvConstactsFlag, AppDevice.getWidth(mContext) / 4, AppDevice.getWidth(mContext) / 4);

        KLog.e(mSelectedTeamMemberAccounts);

        userContacts = UserUtils.getUserContacts();

        KLog.e(userContacts.size());

        if (actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE && itContactBean != null) {
            userContacts.add(itContactBean);
        }

        showContact();//设置列表

        rvSelectedContacts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        //增减监听
        this.setOnItemCheckListener(new ICheckItemListener<ContactBean>() {
            @Override
            public void checked(ArrayList<ContactBean> datas,ContactBean data,boolean isAdd) {

                if (actionType != AppConst.SQUARE_ROOM_DELETE_ANYONE) {
                    selectedAdapter = new SelectedAdapter(new ArrayList<>(datas));
                    rvSelectedContacts.setAdapter(selectedAdapter);
                    rvSelectedContacts.getLayoutManager().scrollToPosition(datas.size() - 1);
                }

                //更改标题数量统计
                title.setActionText(textAction,
                        String.format(getString(actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE ?
                                        R.string.str_title_del : R.string.str_title_ok),
                                (result.size() + (actionType==AppConst.SQUARE_ROOM_DELETE_ANYONE?0:mSelectedTeamMemberAccounts.size()) > 0 ?
                                        "(" + (result.size() + (actionType==AppConst.SQUARE_ROOM_DELETE_ANYONE?0:mSelectedTeamMemberAccounts.size())) + ")" : "")));

            }
        });

        searchChoose.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    List<ContactBean> filterList = filter(UserUtils.getUserContacts(), newText);

                    contactAdapter.setFilter(filterList);
                    rvContacts.scrollToPosition(0);
                } else {
                    contactAdapter.setFilter(UserUtils.getUserContacts());
                }
                return true;
            }
        });

    }

    /**
     * 初始化标题
     */
    private XTitle.TextAction initTitleAction(String teamId) {

        String titleText;
        if (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
            titleText = "删除";
            title.setActionTextColor(Color.RED);

            title.setTitle("移除聊天成员");
        } else if (actionType == AppConst.SQUARE_ROOM_ADD_ANYONE) {
            titleText = String.format(getString(R.string.str_title_ok), UserUtils.getFriendsInTeam(teamId).size() > 0 ? "(" + mSelectedTeamMemberAccounts.size() + ")" : "");
        } else {
            titleText = "完成";
        }

        return new XTitle.TextAction(titleText) {
            @Override
            public void actionClick(View view) {

                if (result.size() == 0) {
                    return;//没有选择好友时，点击确定不执行操作
                }

                HashSet<Integer> userFriendsIdList = UserUtils.getUserFriendsIdList(result.values());

                if (actionType == AppConst.CREATE_SQUARE_ROOM_BUILD) {
                    userFriendsIdList.add(UserUtils.checkToken(getActivity()) ? -1 : Integer.parseInt(UserUtils.getToken()));
                    createChatGroup(userFriendsIdList);
                } else if (actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
                    userFriendsIdList.add(UserUtils.checkToken(getActivity()) ? -1 : Integer.parseInt(UserUtils.getToken()));
                    userFriendsIdList.add(itContactBean.getFriend_id());
                    createChatGroup(userFriendsIdList);
                } else {
                    Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                    intent.putExtra("square_action", actionType);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("choose_friend_array",new ArrayList<>(result.values()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    UIHelper.toast(getActivity(), actionType == AppConst.SQUARE_ROOM_ADD_ANYONE ? "添加成功" : "移除成功");
                    finish();
                }
            }
        };
    }

    /**
     * 创建群组
     */
    public void createChatGroup(final HashSet<Integer> userIdList) {
        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(userIdList));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject resultJson = JSONObject.parseObject(responseInfo);
                if (resultJson.getIntValue("code") == 0) {

                    Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                    intent.putExtra("square_action", actionType);
                    intent.putExtra("conversation_id", resultJson.getJSONObject("data").getString("conv_id"));

                    intent.putIntegerArrayListExtra("choose_friend_array", new ArrayList<>(userIdList));

                    startActivity(intent);

                    UIHelper.toast(ChooseContactActivity.this, "群组创建成功!!!");
                    finish();
                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CREATE_GROUP_CHAT, ggHttpInterface).startGet();
    }

    /**
     * 筛选逻辑
     */
    private List<ContactBean> filter(List<ContactBean> contactBeanList, String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        query = query.toLowerCase();
        final List<ContactBean> filteredModelList = new ArrayList<>();
        for (ContactBean contactBean : contactBeanList) {

            final String targetZh = contactBean.getTarget().toLowerCase();//原始文本

            final String targetEn = Pinyin.toPinyin(targetZh, "").toLowerCase();//

            final String simpleSpell = getSimpleSpell(contactBean.getTarget());

            if (targetZh.contains(query) ||
                    targetEn.contains(query) ||
                    simpleSpell.substring(0, getSimpleSpell(query).length()).equals(getSimpleSpell(query))) {

                filteredModelList.add(contactBean);
            }
        }
        return filteredModelList;
    }

    private String getSimpleSpell(String target) {
        char[] chars = target.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            builder.append(Pinyin.toPinyin(c).charAt(0));
        }
        return builder.toString().toLowerCase();
    }

    private void showContact() {
        if (null == userContacts || userContacts.isEmpty()) {
            KLog.e("用户没有好友");
            return;
        }

        for (ContactBean bean : userContacts) {
            bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        }

        SuspendedDecoration mDecoration = new SuspendedDecoration(getActivity());

        mDecoration.setmDatas(userContacts);

        indexBar.setmSourceDatas(userContacts)//设置数据
                .invalidate();

        rvContacts.addItemDecoration(mDecoration);

        contactAdapter = new ChooseAdapter(userContacts);

        contactAdapter.notifyDataSetChanged();

        rvContacts.addItemDecoration(new NormalItemDecoration(getActivity(), Color.parseColor("#D9D9D9")));

//        if (actionType==AppConst.CREATE_SQUARE_ROOM_BUILD){
//            HeaderAndFooterWrapper wrapper=new HeaderAndFooterWrapper(contactAdapter);
//            View headView= LayoutInflater.from(getActivity()).inflate(R.layout.header_group_cheat_rv,new LinearLayout(getActivity()));
//            wrapper.addHeaderView(headView);
//            rvContacts.setAdapter(wrapper);
//        }else {
        rvContacts.setAdapter(contactAdapter);
//        }
    }

    /**
     * 主列表视图适配器 ☆☆☆
     */
    private class ChooseAdapter extends CommonAdapter<ContactBean> {

        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new LinkedHashMap<>();

        private List<ContactBean> datas;

        ChooseAdapter(List<ContactBean> datas) {
            super(ChooseContactActivity.this, R.layout.item_contacts, datas);
            this.datas = datas;
            initMap();
        }

        private void initMap() {
            for (ContactBean bean : datas) {
                map.put(bean.getFriend_id(), false);
            }
        }

        @Override
        protected void convert(final ViewHolder holder, final ContactBean data, final int position) {

            holder.setText(R.id.item_contacts_tv_nickname, data.getTarget());
            holder.setImageUrl(R.id.item_contacts_iv_icon, (String) data.getAvatar());

            //设置Tag
            holder.itemView.setTag(position);

            final SuperCheckBox checkBox = holder.getView(R.id.check_user);
            checkBox.setVisibility(View.VISIBLE);

            if (String.valueOf(data.getFriend_id()).equals(UserUtils.getToken())) {
                checkBox.setChecked(true);
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                checkBox.setEnabled(false);
            }
            if (actionType == AppConst.SQUARE_ROOM_ADD_ANYONE || actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
                //判断当前的联系人是否已经在群中，是则显示灰色勾选图标
                if ((!ArrayUtils.isEmpty(mSelectedTeamMemberAccounts)) && mSelectedTeamMemberAccounts.contains(data)) {
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                    holder.itemView.setClickable(false);
                    holder.itemView.setEnabled(false);
                }
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    map.put(data.getFriend_id(), isChecked);
                }
            });
            // 设置CheckBox的状态
            if (map.get(data.getFriend_id()) == null) {
                map.put(data.getFriend_id(), false);
            }
            checkBox.setChecked(map.get(data.getFriend_id()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        removeFriends(data);
                    } else {
                        addFriend(data);
                    }
                    setSelectItem(data.getFriend_id(), position);
                }
            });

        }

        private void setSelectItem(int selectItemId, int position) {
            //checkbox 及其Item 对当前状态取反
            if (map.get(selectItemId)) {
                map.put(selectItemId, false);
            } else {
                map.put(selectItemId, true);
            }
            if (position != -1) {
                notifyItemChanged(position);
            } else {
                notifyDataSetChanged();
            }
        }

        private int dataGetPosition(ContactBean contactBean) {
            return datas.indexOf(contactBean);
        }

        private void setFilter(List<ContactBean> contactBeanList) {
            if (null == datas) {
                datas = new ArrayList<>();
            }

            datas.clear();

            if (null != contactBeanList && !contactBeanList.isEmpty()) {
                datas.addAll(contactBeanList);
            }

            notifyDataSetChanged();
        }

    }


    private void addFriend(ContactBean contactBean) {
        result.put(contactBean.getFriend_id(), contactBean);

        if (listener != null) {
            listener.checked(new ArrayList<>(result.values()),contactBean,true);
        }

    }

    private void removeFriends(ContactBean contactBean) {

        result.remove(valueGetKey(result, contactBean));

        if (listener != null) {
            listener.checked(new ArrayList<>(result.values()),contactBean,false);
        }
    }

    /*
    * 已选列表适配器
    * */
    private class SelectedAdapter extends CommonAdapter<ContactBean> {

        public SelectedAdapter(List<ContactBean> datas) {
            super(ChooseContactActivity.this, R.layout.item_selected_contact_rv, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final ContactBean data, final int position) {
            ImageView ivHeader = holder.getView(R.id.ivHeader);
            if (TextUtils.isEmpty((CharSequence) data.getAvatar())) {
                ivHeader.setImageResource(R.mipmap.image_placeholder);
            } else {
                ImageDisplay.loadNetImage(getActivity(), data.getAvatar().toString(), ivHeader);
            }

//            if (result.values().size() >= 7) {
//                AppDevice.setViewWidth$Height(rvSelectedContacts, -2, topListMaxWidth);
//            }else {
//                AppDevice.setViewWidth$Height(rvSelectedContacts,-2,-2);
//            }

            holder.setOnClickListener(R.id.item_selected_contact, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(data);

                    contactAdapter.setSelectItem(data.getFriend_id(), contactAdapter.dataGetPosition(data));

                    result.remove(valueGetKey(result, data));
                    result.remove(valueGetKey(result, data));

                    if (listener != null) {
                        listener.checked(new ArrayList<>(result.values()),data,false);
                    }
                }
            });
        }
    }

    private int valueGetKey(Map<Integer, ContactBean> map, ContactBean contactBean) {
        for (Map.Entry<Integer, ContactBean> entry : map.entrySet()) {
            if (entry.getValue() == contactBean) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private interface ICheckItemListener<T> {
        void checked(ArrayList<T> datas,T data , boolean isAdd);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (searchChoose.getQuery().length() > 0) {
                searchChoose.findViewById(R.id.search_close_btn).performClick();
                contactAdapter.setFilter(UserUtils.getUserContacts());
                searchChoose.clearFocus();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 删除好友列表
     */
    private class DeleteAdapter extends CommonAdapter<ContactBean> {

        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new HashMap<>();

        //是否显示单选框,默认false
        private boolean isshowBox = false;

        private List<ContactBean> datas;

        private DeleteAdapter(List<ContactBean> datas) {
            super(getActivity(), R.layout.item_contacts, datas);
            this.datas = datas;
            initMap();
        }

        private void initMap() {
            for (int i = 0; i < datas.size(); i++) {
                map.put(datas.get(i).getFriend_id(), false);
            }
        }

        @Override
        protected void convert(ViewHolder holder, final ContactBean data, final int position) {
            holder.setText(R.id.item_contacts_tv_nickname, data.getTarget());
            holder.setImageUrl(R.id.item_contacts_iv_icon, (String) data.getAvatar());

            //设置Tag
            holder.itemView.setTag(position);

            final SuperCheckBox checkBox = holder.getView(R.id.check_user);
            checkBox.setVisibility(View.VISIBLE);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    map.put(data.getFriend_id(), isChecked);
                }
            });
            // 设置CheckBox的状态
            if (map.get(data.getFriend_id()) == null) {
                map.put(data.getFriend_id(), false);
            }
            checkBox.setChecked(map.get(data.getFriend_id()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        removeFriends(data);
                    } else {
                        addFriend(data);
                    }
                    setSelectItem(data.getFriend_id(), position);
                }
            });

        }

        private void setSelectItem(int selectItemId, int position) {
            //checkbox 及其Item 对当前状态取反
            if (map.get(selectItemId)) {
                map.put(selectItemId, false);
            } else {
                map.put(selectItemId, true);
            }
            notifyItemChanged(position);
        }
    }

}
