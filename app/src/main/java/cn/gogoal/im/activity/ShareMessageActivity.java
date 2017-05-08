package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ShareListBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/5/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :${分享选择页面}.
 */
public class ShareMessageActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.title_bar)
    XTitle titleBar;

    private List<ShareListBean> datas;
    private ShareListAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("选择联系人").setLeftText("\u3000取消");
        BaseActivity.initRecycleView(recyclerView, 0);

        datas = new ArrayList<>();
        adapter = new ShareListAdapter(datas);

        recyclerView.setAdapter(adapter);

        datas.add(new ShareListBean(ShareListBean.LIST_TYPE_SEARCH));
        datas.add(addFunctionHead("新朋友", R.mipmap.contacts_new_friend));
        datas.add(addFunctionHead("我的群组", R.mipmap.group_contacts));

        getRecentConversation();
    }

    private ShareListBean addFunctionHead(String name, int iconId) {
        ShareListBean<Integer> bean = new ShareListBean<>(ShareListBean.LIST_TYPE_ITEM);
        bean.setItemImage(iconId);
        bean.setText(name);
        return bean;
    }

    public void getRecentConversation() {
        JSONArray recentArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", null);
        if (recentArray != null) {
            datas.add(new ShareListBean(ShareListBean.LIST_TYPE_SECTION));
            List<IMMessageBean> messageBeen = JSON.parseArray(recentArray.toJSONString(), IMMessageBean.class);
            for (IMMessageBean bean : messageBeen) {
                ShareListBean<String> shareListBean = new ShareListBean<>(
                        ShareListBean.LIST_TYPE_ITEM, bean.getAvatar(), bean.getNickname(), bean);
                datas.add(shareListBean);
            }
        }
        adapter.notifyDataSetChanged();

    }

    private class ShareListAdapter extends BaseMultiItemQuickAdapter<ShareListBean, BaseViewHolder> {
        public ShareListAdapter(List<ShareListBean> data) {
            super(data);
            addItemType(ShareListBean.LIST_TYPE_SEARCH, R.layout.include_search_edit);
            addItemType(ShareListBean.LIST_TYPE_SECTION, android.R.layout.simple_list_item_1);
            addItemType(ShareListBean.LIST_TYPE_ITEM, R.layout.item_contacts);
        }

        @Override
        protected void convert(BaseViewHolder holder, ShareListBean data, int position) {
            switch (holder.getItemViewType()) {
                case ShareListBean.LIST_TYPE_SEARCH:
                    EditText etSearch = holder.getView(R.id.layout_2search);

                    break;
                case ShareListBean.LIST_TYPE_SECTION:
                    TextView sectionView = holder.getView(android.R.id.text1);
                    sectionView.setText("最近会话");
                    AppDevice.setViewWidth$Height(sectionView,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            AppDevice.dp2px(ShareMessageActivity.this,30));

                    sectionView.setTextColor(0xff888888);
                    sectionView.setBackgroundColor(0xffe8e8e8);
                    break;
                case ShareListBean.LIST_TYPE_ITEM:
                    AppCompatImageView icon = holder.getView(R.id.item_contacts_iv_icon);
                    if (data.getItemImage() instanceof Integer) {
                        icon.setImageResource((Integer) data.getItemImage());
                    } else if (data.getItemImage() instanceof String) {
                        Glide.with(ShareMessageActivity.this).load((String) data.getItemImage())
                                .into(icon);
                    }
                    holder.setText(R.id.item_contacts_tv_nickname, data.getText());
                    break;
            }
        }
    }
}
