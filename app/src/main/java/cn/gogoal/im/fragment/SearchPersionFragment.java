package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.IMPersonDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
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
//    private EmptyWrapper wrapper;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        searchResultList = new ArrayList<>();

        adapter = new SearchPersionResultAdapter(searchResultList);

        recyclerView.setAdapter(adapter);

    }

    @Subscriber(tag = "SEARCH_PERSION_TAG")
    private void searchPersion(final String keyword) {
        searchResultList.clear();
        xLayout.setStatus(XLayout.Loading);
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("keyword", keyword);
        KLog.e("token=" + UserUtils.getToken() + "&keyword=" + keyword);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    JSONObject jsonObject = JSONObject.parseObject(responseInfo);
                    List<ContactBean> resultList = JSONObject.parseArray(jsonObject.getJSONArray("data")
                            .toJSONString(), ContactBean.class);

                    searchResultList.addAll(resultList);
                    adapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    xLayout.setEmptyText(String.format(getString(R.string.str_result),keyword)+"的用户");
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
                        searchPersion(keyword);
                    }
                });
                UIHelper.toastError(getActivity(), msg,xLayout);

            }
        };
        new GGOKHTTP(param, GGOKHTTP.SEARCH_FRIEND, ggHttpInterface).startGet();
    }

    private class SearchPersionResultAdapter extends CommonAdapter<ContactBean,BaseViewHolder> {

        SearchPersionResultAdapter(List<ContactBean> datas) {
            super(R.layout.item_search_type_persion, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ContactBean data, int position) {
            KLog.e(JSONObject.toJSONString(data));
            holder.setText(R.id.item_tv_search_result_name, data.getNickname());
            try {
                holder.setImageUrl(R.id.item_user_avatar, (String) data.getAvatar());
            } catch (Exception e) {
                KLog.e(e.getMessage());
            }
            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), IMPersonDetailActivity.class);
                    intent.putExtra("account_id",data.getUserId());
                    KLog.e(data.getUserId());
                    startActivity(intent);
                }
            });
        }
    }

}
