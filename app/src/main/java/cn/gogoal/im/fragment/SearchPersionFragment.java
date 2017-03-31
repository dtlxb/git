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
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SearchPersionFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<ContactBean> searchResultList;
    private SearchPersionResultAdapter adapter;
//    private EmptyWrapper wrapper;

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_persion;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        searchResultList = new ArrayList<>();

        adapter = new SearchPersionResultAdapter(searchResultList);

//        wrapper = new EmptyWrapper(adapter);
//
//        wrapper.setEmptyView(UIHelper.getEmptyView(mContext));

        recyclerView.setAdapter(adapter);
    }

    @Subscriber(tag = "SEARCH_PERSION_TAG")
    private void searchPersion(String keyword) {
        searchResultList.clear();
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("keyword", keyword.toUpperCase());
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

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {

                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.SEARCH_FRIEND, ggHttpInterface).startGet();
    }

    private class SearchPersionResultAdapter extends CommonAdapter<ContactBean> {

        SearchPersionResultAdapter(List<ContactBean> datas) {
            super(SearchPersionFragment.this.getContext(), R.layout.item_search_type_persion, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final ContactBean data, int position) {
            KLog.e(JSONObject.toJSONString(data));
            holder.setText(R.id.item_tv_user_name, data.getNickname());
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
