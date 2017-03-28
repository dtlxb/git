package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import static cn.gogoal.im.common.database.LitePalApplication.getContext;
import static com.igexin.sdk.GTServiceManager.context;

/**
 * author wangjd on 2017/3/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ChooseContactActivity extends BaseActivity {

    private ICheckItemListener<ContactBean> listener;

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

    private XTitle title;

    XTitle.TextAction textAction;

    private ChooseAdapter contactAdapter;

    private int topListMaxWidth;

    //标记当前模式（false表示是【创建群】，true表示【拉人进群】）
    private boolean isAddTeamMemberMode = false;

    private Map<Integer, ContactBean> result = new HashMap<>();

    private SelectedAdapter selectedAdapter;

    private List<ContactBean> mSelectedTeamMemberAccounts = new ArrayList<>();//已经在群中的成员账号

    //本人
    private ContactBean itContactBean;

    //获取群操作
    private int squareAction;

    @Override
    public int bindLayout() {
        return R.layout.activity_choose_contaces;
    }

    @Override
    public void doBusiness(final Context mContext) {
        title = setMyTitle(R.string.title_group_chat, true);
        textAction = initTitle();

        title.addAction(textAction);

        //获取传递值
        String teamId = getIntent().getStringExtra("team_Id");
        squareAction = getIntent().getIntExtra("square_action", 0);
        itContactBean = (ContactBean) getIntent().getSerializableExtra("seri");

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        AppDevice.setViewWidth$Height(tvConstactsFlag, AppDevice.getWidth(mContext) / 4, AppDevice.getWidth(mContext) / 4);

        mSelectedTeamMemberAccounts = UserUtils.getFriendsInTeam(teamId);
        showContact();//设置列表

        rvSelectedContacts.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        this.setOnItemCheckListener(new ICheckItemListener<ContactBean>() {
            @Override
            public void checked(ArrayList<ContactBean> datas) {
                if (datas.size() >= 7) {
                    topListMaxWidth = rvSelectedContacts.getWidth();
                }
                selectedAdapter = new SelectedAdapter(new ArrayList<>(datas));
                rvSelectedContacts.setAdapter(selectedAdapter);
                rvSelectedContacts.getLayoutManager().scrollToPosition(datas.size() - 1);

                //更改标题数量统计
                ((TextView) title.getViewByAction(textAction)).setText(
                        String.format(getString(R.string.str_title_ok),
                                (result.size() > 0 ? "(" + result.size() + ")" : "")));

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
    private XTitle.TextAction initTitle() {
        return new XTitle.TextAction("完成") {
            @Override
            public void actionClick(View view) {

                if (result.size() == 0) {
                    return;//没有选择好友时，点击确定不执行操作
                } else {
                    Set idSet = new HashSet();
                    idSet.addAll(result.keySet());
                    switch (squareAction) {
                        case AppConst.CREATE_SQUARE_ROOM_DIRECT:
                            //加自己
                            idSet.add(Integer.parseInt(UserUtils.getToken()));
                            createChatGroup(idSet);
                            break;
                        case AppConst.CREATE_SQUARE_ROOM_BY_ONE:
                            //加他
                            idSet.add(itContactBean.getFriend_id());
                            //加自己
                            idSet.add(Integer.parseInt(UserUtils.getToken()));
                            createChatGroup(idSet);
                            break;
                        case AppConst.SQUARE_ROOM_ADD_ANYONE:
                            break;
                        case AppConst.SQUARE_ROOM_DELETE_ANYONE:
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    /**
     * 创建群组
     */
    public void createChatGroup(Set idSet) {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(idSet));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(ChooseContactActivity.this, "群组创建成功!!!");
                    if (squareAction == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
                        //回传选择结果,选择的结果
                        Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                        Bundle bundle = new Bundle();
                        //bundle.putSerializable("choose_friend_array", new ArrayList<>(result.values()));
                        bundle.putSerializable("conversation_id", result.getJSONObject("data").getString("conv_id"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (squareAction == AppConst.CREATE_SQUARE_ROOM_DIRECT) {

                    }
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
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

            final String simpleSpell = getSimpleSpell(contactBean.getTarget());

            final String targetZh = contactBean.getTarget().toLowerCase();

            final String reMarkEn = Pinyin.toPinyin(targetZh, "").toLowerCase();

            if (targetZh.contains(query) || reMarkEn.contains(query) || simpleSpell.contains(getSimpleSpell(query))) {

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
        List<ContactBean> userContacts = UserUtils.getUserContacts();

        if (null == userContacts || userContacts.isEmpty()) {
            KLog.e("用户没有好友");
            return;
        }

        for (ContactBean bean : userContacts) {
            bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        }

        SuspendedDecoration mDecoration = new SuspendedDecoration(getContext());

        mDecoration.setmDatas(userContacts);

        indexBar.setmSourceDatas(userContacts)//设置数据
                .invalidate();

        rvContacts.addItemDecoration(mDecoration);

        contactAdapter = new ChooseAdapter(userContacts);

        contactAdapter.notifyDataSetChanged();

        rvContacts.addItemDecoration(new NormalItemDecoration(getActivity(), Color.parseColor("#D9D9D9")));

        rvContacts.setAdapter(contactAdapter);
    }

    /**
     * 主列表视图适配器
     */
    private class ChooseAdapter extends CommonAdapter<ContactBean> {

        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new HashMap<>();

        private List<ContactBean> datas;

        ChooseAdapter(List<ContactBean> datas) {
            super(ChooseContactActivity.this, R.layout.item_contacts, datas);
            this.datas = datas;
            initMap();
        }

        private void initMap() {
            for (int i = 0; i < datas.size(); i++) {
                map.put(i, false);
            }
        }

        @Override
        protected void convert(final ViewHolder holder, final ContactBean data, final int position) {
            final SuperCheckBox checkBox = holder.getView(R.id.check_user);
            checkBox.setVisibility(View.VISIBLE);

            if (isAddTeamMemberMode) {
                //判断当前的联系人是否已经在群中，是则显示灰色勾选图标
                if ((!ArrayUtils.isEmpty(mSelectedTeamMemberAccounts)) && mSelectedTeamMemberAccounts.contains(data)) {
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                    holder.itemView.setClickable(false);
                    holder.itemView.setEnabled(false);
                }
            }

            holder.setText(R.id.item_contacts_tv_nickname, data.getTarget());
            holder.setImageUrl(R.id.item_contacts_iv_icon, (String) data.getAvatar());

            //设置Tag
            holder.itemView.setTag(position);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    map.put(position, isChecked);
//                    if (isChecked){//不需要
//                       addFriend(data,position);
//
//                    }else {
//                        removeFriends(data);
//                    }
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            checkBox.setChecked(map.get(position));

            //============================================

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        removeFriends(data);
                    } else {
                        addFriend(data, data.getFriend_id());
                    }
                    setSelectItem(position);
                }
            });

        }

        public int positionFromData(ContactBean bean) {
            return ArrayUtils.valueGetKey(datas, bean);
        }

        public void setSelectItem(int position) {
            //对当前状态取反
            if (map.get(position)) {
                map.put(position, false);
            } else {
                map.put(position, true);
            }
            notifyItemChanged(position);
        }

        public void setFilter(List<ContactBean> contactBeanList) {
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


    private void addFriend(ContactBean contactBean, int friendId) {
        result.put(friendId, contactBean);
        if (listener != null) {
            listener.checked(new ArrayList<>(result.values()));
        }

    }

    private void removeFriends(ContactBean contactBean) {

        result.remove(ArrayUtils.valueGetKey(result, contactBean));

        if (listener != null) {
            listener.checked(new ArrayList<>(result.values()));
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
            if (TextUtils.isEmpty(data.getAvatar().toString())) {
                ivHeader.setImageResource(R.mipmap.image_placeholder);
            } else {
                ImageDisplay.loadNetImage(getActivity(), data.getAvatar().toString(), ivHeader);
            }

            if (result.values().size() >= 7) {
                AppDevice.setViewWidth$Height(rvSelectedContacts, -2, topListMaxWidth);
            }

            holder.setOnClickListener(R.id.item_selected_contact, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(data);

                    contactAdapter.setSelectItem(contactAdapter.positionFromData(data));

                    result.remove(ArrayUtils.valueGetKey(result, data));

                    if (listener != null) {
                        listener.checked(new ArrayList<>(result.values()));
                    }

                    contactAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private interface ICheckItemListener<T> {
        void checked(ArrayList<T> datas);
    }
}
