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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ICheckItemListener;
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
    private int topListWidth;

    private GGShareEntity entity;

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
         * 1100.选择好友和单聊好友创建群，
         * 1101.选择好友直接创建群，
         * 1102.原来存在的群继续添加好友，
         * 1103.原来存在的群中移除好友
         * 1104.群@人
         * 1205.分享消息给很多好友
         * */
        actionType = getIntent().getIntExtra("square_action", 0);

        if (actionType==AppConst.SQUARE_ROOM_AT_SHARE_MESSAGE){
            entity = getIntent().getParcelableExtra("share_web_data");
        }
        //actionType = 1102,1103,1104
        String teamId = getIntent().getStringExtra("conversation_id");

        switch (actionType){
            case AppConst.SQUARE_ROOM_AT_SHARE_MESSAGE:
//                GGShareEntity shareEntity
                break;
        }
        if (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE || actionType == AppConst.LIVE_CONTACT_SOMEBODY) {
            rvDel.setVisibility(View.VISIBLE);
            rvDel.addItemDecoration(new NormalItemDecoration(mContext));
            rvDel.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rvDel.setAdapter(new DeleteAdapter(UserUtils.getOthersInTeam(teamId, actionType)));
        }

        //1100
        itContactBean = (ContactBean) getIntent().getSerializableExtra("seri");

        if (actionType == AppConst.SQUARE_ROOM_ADD_ANYONE) {
            mSelectedTeamMemberAccounts.addAll(UserUtils.getFriendsInTeam(teamId));
        } else if (actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
            mSelectedTeamMemberAccounts.add(itContactBean);
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

        if (actionType == AppConst.SQUARE_ROOM_AT_SOMEONE) {
            userContacts = UserUtils.getFriendsInTeam(teamId);
        } else {
            userContacts = UserUtils.getUserContacts();
        }

        /*if (actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE && itContactBean != null) {
            userContacts.add(itContactBean);
        }*/

        showContact();//设置列表

        rvSelectedContacts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));


        //增减监听
        this.setOnItemCheckListener(new ICheckItemListener<ContactBean>() {
            @Override
            public void checked(Set<ContactBean> datas, ContactBean data, boolean isAdd) {
                if (actionType != AppConst.SQUARE_ROOM_DELETE_ANYONE) {
                    selectedAdapter = new SelectedAdapter(new ArrayList<>(datas));
                    rvSelectedContacts.setAdapter(selectedAdapter);
                    rvSelectedContacts.getLayoutManager().scrollToPosition(datas.size() - 1);
                }

                if (isAdd) {
                    if (datas.size() >= 8)
                        topListWidth = rvSelectedContacts.getWidth();
                    AppDevice.setViewWidth$Height(searchChoose, AppDevice.getWidth(mContext) - topListWidth, -2);
                } else {
                    if (datas.size() >= 6) {
                        AppDevice.setViewWidth$Height(searchChoose, AppDevice.getWidth(mContext) - topListWidth, -2);
                    }
                }

                //更改标题数量统计
                title.setActionText(textAction,
                        String.format(getString(actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE ?
                                        R.string.str_title_del : R.string.str_title_ok),
                                (result.size() + (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE ? 0 : mSelectedTeamMemberAccounts.size()) > 0 ?
                                        "(" + (result.size() + (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE ? 0 : mSelectedTeamMemberAccounts.size())) + ")" : "")));

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
        } else if (actionType == AppConst.LIVE_CONTACT_SOMEBODY) {
            titleText = "";
        } else {
            titleText = "完成";
        }

        return new XTitle.TextAction(titleText) {
            @Override
            public void actionClick(View view) {

                if (result.size() == 0) {
                    return;//没有选择好友时，点击确定不执行操作
                }

                TreeSet<Integer> userFriendsIdList = UserUtils.getUserFriendsIdList(result.values());

                switch (actionType) {
                    case AppConst.CREATE_SQUARE_ROOM_BUILD:
                        userFriendsIdList.add(UserUtils.checkToken(getActivity()) ? -1 : Integer.parseInt(UserUtils.getMyAccountId()));
                        createChatGroup(userFriendsIdList);
                        break;
                    case AppConst.CREATE_SQUARE_ROOM_BY_ONE:
                        userFriendsIdList.add(UserUtils.checkToken(getActivity()) ? -1 : Integer.parseInt(UserUtils.getMyAccountId()));
                        userFriendsIdList.add(itContactBean.getFriend_id());
                        createChatGroup(userFriendsIdList);
                        break;
                    case AppConst.SQUARE_ROOM_ADD_ANYONE://加人
                    case AppConst.SQUARE_ROOM_DELETE_ANYONE://删人
                    case AppConst.SQUARE_ROOM_AT_SOMEONE://@人
                        Intent intent = new Intent();
                        intent.putExtra("square_action", actionType);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("choose_friend_array", new ArrayList<>(result.values()));
                        intent.putExtras(bundle);
                        setResult(actionType, intent);
                        //UIHelper.toast(getActivity(), actionType == AppConst.SQUARE_ROOM_ADD_ANYONE ? "添加成功" : "移除成功");
                        finish();
                        break;
                    case AppConst.SQUARE_ROOM_AT_SHARE_MESSAGE://分享给很多人

                        break;
                }
            }
        };
    }

    /**
     * 创建群组
     */
    public void createChatGroup(final TreeSet<Integer> userIdList) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(userIdList));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject resultJson = JSONObject.parseObject(responseInfo);
                if (resultJson.getIntValue("code") == 0) {

                    Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                    Bundle mbundle = new Bundle();
                    mbundle.putInt("square_action", actionType);
                    mbundle.putString("conversation_id", resultJson.getJSONObject("data").getString("conv_id"));
                    intent.putExtras(mbundle);
                    startActivity(intent);

                    UIHelper.toast(ChooseContactActivity.this, "群组创建成功!!!");
                    finish();
                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, null);
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
                    targetEn.startsWith(query) ||
                    simpleSpell.contains(query)) {

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
    private class ChooseAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {

        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new LinkedHashMap<>();

        private List<ContactBean> datas;

        ChooseAdapter(List<ContactBean> datas) {
            super(R.layout.item_contacts, datas);
            this.datas = datas;
            initMap();
        }

        private void initMap() {
            for (ContactBean bean : datas) {
                map.put(bean.getFriend_id(), false);
            }
        }

        @Override
        protected void convert(final BaseViewHolder holder, final ContactBean data, final int position) {

            holder.setText(R.id.item_contacts_tv_nickname, data.getTarget());
            holder.setImageUrl(R.id.item_contacts_iv_icon, (String) data.getAvatar());

            //设置Tag
            holder.itemView.setTag(position);

            final SuperCheckBox checkBox = holder.getView(R.id.check_user);
            checkBox.setVisibility(View.VISIBLE);

            if (String.valueOf(data.getFriend_id()).equals(UserUtils.getMyAccountId())) {
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
            listener.checked(ArrayUtils.mapValue2Set(result), contactBean, true);
        }

    }

    private void removeFriends(ContactBean contactBean) {

        result.remove(valueGetKey(result, contactBean));

        if (listener != null) {
            listener.checked(ArrayUtils.mapValue2Set(result), contactBean, false);
        }
    }

    /*
    * 已选列表适配器
    * */
    private class SelectedAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {

        public SelectedAdapter(List<ContactBean> datas) {
            super(R.layout.item_selected_contact_rv, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ContactBean data, final int position) {
            ImageView ivHeader = holder.getView(R.id.ivHeader);
            if (TextUtils.isEmpty((CharSequence) data.getAvatar())) {
                ivHeader.setImageResource(R.mipmap.image_placeholder);
            } else {
                ImageDisplay.loadImage(getActivity(), data.getAvatar().toString(), ivHeader);
            }

//            if (isAdd) {
//                if (datas.size() >=9) {//8个item宽度
//                    AppDevice.setViewWidth$Height(rvSelectedContacts, topListWidth, -2);
//                } else {
//                    AppDevice.setViewWidth$Height(rvSelectedContacts, -2, -2);
//                }
//            }else {
//                if (datas.size() >= 7) {
//                    AppDevice.setViewWidth$Height(rvSelectedContacts, topListWidth, -2);
//                } else {
//                    AppDevice.setViewWidth$Height(rvSelectedContacts, -2, -2);
//                }
//            }

            holder.setOnClickListener(R.id.item_selected_contact, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(data);

                    contactAdapter.setSelectItem(data.getFriend_id(), contactAdapter.dataGetPosition(data));

                    result.remove(valueGetKey(result, data));

                    if (listener != null) {
                        listener.checked(ArrayUtils.mapValue2Set(result), data, false);
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
    private class DeleteAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {

        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new HashMap<>();

        //是否显示单选框,默认false
        private boolean isshowBox = false;

        private List<ContactBean> datas;

        private DeleteAdapter(List<ContactBean> datas) {
            super(R.layout.item_contacts, datas);
            this.datas = datas;
            initMap();
        }

        private void initMap() {
            for (int i = 0; i < datas.size(); i++) {
                map.put(datas.get(i).getFriend_id(), false);
            }
        }

        @Override
        protected void convert(BaseViewHolder holder, final ContactBean data, final int position) {
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
                    if (actionType == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
                        if (checkBox.isChecked()) {
                            removeFriends(data);
                        } else {
                            addFriend(data);
                        }
                        setSelectItem(data.getFriend_id(), position);
                    } else {
                        checkBox.setChecked(true);
                        Intent intent = new Intent();
                        intent.putExtra("square_action", actionType);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("choose_connect_livebean", data);
                        intent.putExtras(bundle);
                        setResult(actionType, intent);
                        finish();
                    }
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
