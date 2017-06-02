package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.IMPersonDetailActivity;
import cn.gogoal.im.activity.PhoneContactsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.XLayout;


/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SearchPersionFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<ContactBean> searchResultList;
    private SearchPersionResultAdapter adapter;

    private int page=1;

//    private EmptyWrapper wrapper;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(final Context mContext) {
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        searchResultList = new ArrayList<>();

        adapter = new SearchPersionResultAdapter(searchResultList);

        recyclerView.setAdapter(adapter);

        View headView= LayoutInflater.from(mContext)
                .inflate(R.layout.item_contacts,new LinearLayout(mContext),false);
        RoundedImageView headImage= (RoundedImageView)
                headView.findViewById(R.id.item_contacts_iv_icon);
        TextView headText= (TextView)
                headView.findViewById(R.id.item_contacts_tv_nickname);
        headImage.setImageResource(R.mipmap.chat_phone_contacts);
        headText.setText(R.string.im_phone_num);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PhoneContactsActivity.class));
            }
        });

        adapter.addHeaderView(headView);

        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page <= 100) { //100页×20条/页，最多显示2000条搜索结果
                    page++;
                    searchPersion(AppConst.REFRESH_TYPE_LOAD_MORE,keyword);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(),"没有更多数据");

                }
                adapter.loadMoreComplete();
            }
        },recyclerView);

    }

    private void searchPersion(int loadType,String keyWord){
        searchPersion(keyWord);
    }

    @Subscriber(tag = "SEARCH_PERSION_TAG")
    private void searchPersion(final String keyword) {
        adapter.setEnableLoadMore(false);

        this.keyword=keyword;
        searchResultList.clear();
//        xLayout.setStatus(XLayout.Loading);

        final WaitDialog waitDialog = WaitDialog.getInstance("努力搜索中...", R.mipmap.login_loading, true);
        waitDialog.show(getChildFragmentManager());

        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("page", String.valueOf(page));
        param.put("rows", "20");
        param.put("keyword", keyword);
        KLog.e("token=" + UserUtils.getToken() + "&keyword=" + keyword);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                KLog.e(responseInfo);

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    JSONObject jsonObject = JSONObject.parseObject(responseInfo);

                    searchResultList.addAll(JSONObject.parseArray(jsonObject.getJSONArray("data")
                            .toJSONString(), ContactBean.class));

                    adapter.setEnableLoadMore(true);

                    adapter.loadMoreComplete();

                    adapter.notifyDataSetChanged();

                    xLayout.setStatus(XLayout.Success);
                    waitDialog.dismiss();

                    adapter.removeAllHeaderView();

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    xLayout.setEmptyText(String.format(getString(R.string.str_result), keyword) + "的用户");
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                    @Override
                    public void onReload(View v) {
                        searchPersion(AppConst.REFRESH_TYPE_RELOAD,keyword);
                    }
                });
                UIHelper.toastError(getActivity(), msg, xLayout);

            }
        };
        new GGOKHTTP(param, GGOKHTTP.SEARCH_FRIEND, ggHttpInterface).startGet();
    }

//    private class LoadSearchResult extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            if (getActivity() != null)
//                mAdapter = new Adapter(mList);
//            return "Executed";
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
////            recyclerView.setAdapter(mAdapter);
////            if (RecentActivity.this != null)
////                recyclerView.addItemDecoration(new DividerItemDecoration(RecentActivity.this, DividerItemDecoration.VERTICAL_LIST));
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//    }

    private class SearchPersionResultAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {

        SearchPersionResultAdapter(List<ContactBean> datas) {
            super(R.layout.item_search_type_persion, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ContactBean data, int position) {
            holder.getView(R.id.btn_search_group_add).setVisibility(View.GONE);

            RoundedImageView imageView = holder.getView(R.id.item_user_avatar);
            holder.setText(R.id.item_tv_search_result_name, data.getNickname());
            holder.setText(R.id.item_tv_search_result_intro,data.getDuty());
            try {
                ImageDisplay.loadRoundedRectangleImage(mContext,
                        data.getAvatar(),
                        imageView);
            } catch (Exception e) {
                KLog.e(e.getMessage());
            }

            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), IMPersonDetailActivity.class);
                    intent.putExtra("account_id", data.getUserId());
                    KLog.e(data.getUserId());
                    startActivity(intent);
                }
            });
        }
    }

}
