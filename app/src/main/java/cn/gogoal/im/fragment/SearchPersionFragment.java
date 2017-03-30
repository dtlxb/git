package cn.gogoal.im.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
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

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_persion;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Subscriber(tag = "SEARCH_PERSION_TAG")
    private void searchPersion(String keyword) {
        KLog.e(keyword);

        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("keyword",keyword);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                } else {
                    UIHelper.toastResponseError(getActivity(),responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.SEARCH_FRIEND, ggHttpInterface).startGet();
    }

}
