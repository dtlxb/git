package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hply.imagepicker.view.SuperCheckBox;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.retry.EasyAdapterForRecyclerView;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.retry.EasyViewHolderForRecyclerView;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.bar.QuickIndexBar;
import cn.gogoal.im.ui.view.EasyRecyclerView;
import cn.gogoal.im.ui.view.XTitle;


/**
 * author wangjd on 2017/3/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :群聊时好友选择.
 */
public class ChooseContactActivity extends BaseActivity {

    //标记当前模式（false表示是【创建群】，true表示【拉人进群】）
    private boolean isAddTeamMemberMode = false;

    private List<ContactBean> mSelectedTeamMemberAccounts = new ArrayList<>();//已经在群中的成员账号

    private List<ContactBean> mContacts = new ArrayList<>();

    private EasyAdapterForRecyclerView<ContactBean> mSelectedContactsAdapter;

    //选中的结果，用map，key为RecyclerView的position,view为人物对象，方面移除添加，以及修改选中标示
    private HashMap<Integer, ContactBean> result = new HashMap<>();

    @BindView(R.id.rvSelectedContacts)
    EasyRecyclerView mRvSelectedContacts;

    @BindView(R.id.etKey)
    EditText mEtKey;

    @BindView(R.id.rvContacts)
    EasyRecyclerView mRvContacts;

    @BindView(R.id.quickIndexBar)
    QuickIndexBar mIndexBar;

    @BindView(R.id.tvLetter)
    TextView mTvLetter;

    private View buildTeamHeadView;

    private XTitle title;

    private XTitle.TextAction finishAction;

    @Override
    public int bindLayout() {
        return R.layout.activity_choose_contact;
    }

    @Override
    public void doBusiness(final Context mContext) {
        title = setMyTitle(R.string.title_group_chat, true);

        String teamId = getIntent().getStringExtra("team_Id");

        //true==群已经存在，继续添加好友 或者 移除好友 模式
        isAddTeamMemberMode=getIntent().getBooleanExtra("is_add_team_member_mode",false);

        finishAction = initTitle(title);

        title.addAction(finishAction);

        initDatas();

        setListAdapter(teamId);

        mIndexBar.setFlag(mTvLetter);

        initListener();
    }

    private void initDatas() {

        try {
            //获取用户的全部好友
            List<ContactBean> userContacts = UserUtils.getUserContacts();

            mContacts.clear();

            if (!ArrayUtils.isEmpty(userContacts)) {
                mContacts.addAll(userContacts);
            }

        } catch (Exception e) {
            KLog.e(e);
        }

        setSelectedContactsAdapter();
    }

    private void setSelectedContactsAdapter() {

        if (mSelectedContactsAdapter == null) {

            mSelectedContactsAdapter = new EasyAdapterForRecyclerView<ContactBean>(this, R.layout.item_selected_contact_rv, new ArrayList<>(result.values())) {
                @Override
                public void convert(final EasyViewHolderForRecyclerView helper, final ContactBean item, final int position) {

                    //动态设置列表宽度
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectedContacts.getLayoutParams();
//                    params.weight = mSelectedContacts.size() > 5 ? 4 : 0;
                    int parentWidth = ((LinearLayout) mRvSelectedContacts.getParent()).getWidth();
                    int childWidth = parentWidth * 4 / 5;
                    params.width = result.size() > 5 ? childWidth : params.WRAP_CONTENT;
                    mRvSelectedContacts.setLayoutParams(params);

                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    if (TextUtils.isEmpty(item.getAvatar().toString())) {
                        ivHeader.setImageResource(R.mipmap.image_placeholder);
                    } else {
                        ImageDisplay.loadNetImage(getActivity(), item.getAvatar().toString(), ivHeader);
                    }

                    helper.getView(R.id.item_selected_contact).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSelectedContactsAdapter.removeItem(item);
                            // TODO: 删除item

                            try {
                                View childAt = mRvContacts.getLayoutManager().findViewByPosition(ArrayUtils.valueGetKey(result,item));
                                SuperCheckBox checkBox = (SuperCheckBox) childAt.findViewById(R.id.check_user);
                                checkBox.setChecked(false);
                            }catch (Exception e){

                            }

                            //选择结果中移除
                            removeFriends(item);
                        }
                    });
                }

            };
            mRvSelectedContacts.setAdapter(mSelectedContactsAdapter);
        } else {
            mSelectedContactsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化标题
     */
    private XTitle.TextAction initTitle(XTitle title) {
        return finishAction = new XTitle.TextAction("完成") {
            @Override
            public void actionClick(View view) {

                if (result.size()==0){
                    return;//没有选择好友时，点击不执行操作
                }
                //回传选择结果,选择的结果
                Intent intent=new Intent();
                Collection<ContactBean> contactBeens = result.values();
                Bundle bundle=new Bundle();
                bundle.putSerializable("choose_friend_array",new ArrayList<>(contactBeens));
                intent.putExtras(bundle);
                setResult(10086,intent);
                finish();
            }
        };
    }

    private void setListAdapter(String teamId) {
        //获取当前群已经保存的用户好友
        mSelectedTeamMemberAccounts = UserUtils.getFriendsInTeam(teamId);

        EasyAdapterForRecyclerView<ContactBean> mAdapter = new EasyAdapterForRecyclerView<ContactBean>(getActivity(), R.layout.item_contacts, mContacts) {
            @Override
            public void convert(final EasyViewHolderForRecyclerView helper, final ContactBean item, final int position) {
                if (null == item) {
                    return;
                }

                final CheckBox checker = helper.getView(R.id.check_user);

                helper.setText(R.id.item_contacts_tv_nickname, item.getTarget());

                try {
                    ImageDisplay.loadNetImage(getActivity(), item.getAvatar().toString(), (ImageView) helper.getView(R.id.item_contacts_iv_icon));
                } catch (Exception e) {
                    e.getMessage();
                    KLog.e("[" + item.getTarget() + "]加载头像出现无法预料的错误[" + position + "]");
                }

                String str = "";
                //得到当前字母
                String currentLetter = item.getmPinyin().charAt(0) + "";

                if (position == 0) {
                    str = currentLetter;
                } else {
                    //得到上一个字母
                    String preLetter = mContacts.get(position - 1).getmPinyin().charAt(0) + "";
                    //如果和上一个字母的首字母不同则显示字母栏
                    if (!preLetter.equalsIgnoreCase(currentLetter)) {
                        str = currentLetter;
                    }

                    int nextIndex = position + 1;
                    if (nextIndex < mContacts.size() - 1) {
                        //得到下一个字母
                        String nextLetter = mContacts.get(nextIndex).getmPinyin().charAt(0) + "";
                        //如果和下一个字母的首字母不同则隐藏下划线
                        if (!nextLetter.equalsIgnoreCase(currentLetter)) {
                            helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                        } else {
                            helper.setViewVisibility(R.id.vLine, View.VISIBLE);
                        }
                    } else {
                        helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                    }
                }
                if (position == mContacts.size() - 1) {
                    helper.setViewVisibility(R.id.vLine, View.GONE);
                }

                //根据str是否为空决定字母栏是否显示
                if (TextUtils.isEmpty(str)) {
                    helper.setViewVisibility(R.id.tvIndex, View.GONE);
                } else {
                    helper.setViewVisibility(R.id.tvIndex, View.VISIBLE);
                    helper.setText(R.id.tvIndex, currentLetter);
                }


                helper.setViewVisibility(R.id.check_user, View.VISIBLE);

                if (isAddTeamMemberMode) {
                    //判断当前的联系人是否已经在群中，是则显示灰色勾选图标
                    if ((!ArrayUtils.isEmpty(mSelectedTeamMemberAccounts)) && mSelectedTeamMemberAccounts.contains(item)){
                        checker.setChecked(true);
                        checker.setEnabled(false);
                        helper.getView(R.id.item_contacts_layout).setClickable(false);
                        helper.getView(R.id.item_contacts_layout).setEnabled(false);
                    }
                }

                //条目点击勾选好友
                helper.getView(R.id.item_contacts_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checker.isChecked()) {
                            checker.setChecked(false);
                            //已选择的横向列表 去掉选中项
                            if (mSelectedContactsAdapter.getData().contains(item)) {
                                mSelectedContactsAdapter.removeItem(item);
                            }

                            //选择结果中移除
                            removeFriends(item);

                        } else {
                            checker.setChecked(true);
                            //增加选中项 到已选择的横向列表
                            mSelectedContactsAdapter.addLastItem(item);
                            //添加到结果
                            addFriend(item, helper.getAdapterPosition());

                        }

                        mRvSelectedContacts.getLayoutManager().scrollToPosition(mSelectedContactsAdapter.getItemCount() - 1);

                    }
                });
            }

        };

        if (!isAddTeamMemberMode) {
            //加入头部
            buildTeamHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.header_group_cheat_rv, null);
            mAdapter.addHeaderView(buildTeamHeadView);
        }
        //设置适配器
        if (mRvContacts != null) {
            mRvContacts.setAdapter(mAdapter.getHeaderAndFooterAdapter());
        }
        ;
    }

    private void initListener() {
        mIndexBar.setListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                //显示字母提示

                //滑动对对应字母条目处
                if ("↑".equalsIgnoreCase(letter)) {
                    mRvContacts.moveToPosition(0);
                } else if ("☆".equalsIgnoreCase(letter)) {
                    mRvContacts.moveToPosition(0);
                } else {
                    //找出第一个对应字母的位置后，滑动到指定位置
                    for (int index = 0; index < mContacts.size(); index++) {
                        ContactBean contact = mContacts.get(index);
                        String c = contact.getmPinyin().charAt(0) + "";
                        if (c.equalsIgnoreCase(letter) && mRvContacts.getAdapter().getItemCount() > 0) {
                            mRvContacts.moveToPosition(index);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void addFriend(ContactBean contactBean, int itemPos){
        result.put(itemPos,contactBean);

        //更改标题数量统计
        ((TextView) title.getViewByAction(finishAction)).setText(
                String.format(getString(R.string.str_title_ok),
                        (result.size() > 0 ? "(" + result.size() + ")" : "")));
    }

    private void removeFriends(ContactBean contactBean){

         result.remove(ArrayUtils.valueGetKey(result,contactBean));

        //更改标题数量统计
        ((TextView) title.getViewByAction(finishAction)).setText(
                String.format(getString(R.string.str_title_ok),
                        (result.size() > 0 ? "(" + result.size() + ")" : "")));
    }

}
