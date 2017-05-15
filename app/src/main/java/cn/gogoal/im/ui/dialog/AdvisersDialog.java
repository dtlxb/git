package cn.gogoal.im.ui.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AdvisersAdapter;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.bean.AdvisersBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯投资顾问
 */
public class AdvisersDialog extends BaseCentDailog {

    private List<Advisers> datas;

    private AdvisersAdapter advisersAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_advisers;
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public int getWidth() {
        return 89 * AppDevice.getWidth(getContext()) / 125;
    }

    @Override
    public void bindView(View v) {
        RecyclerView rvAdvisers = (RecyclerView) v.findViewById(R.id.rv_advisers);
        rvAdvisers.setLayoutManager(new LinearLayoutManager(getContext()));

        v.findViewById(R.id.img_advisers_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvisersDialog.this.dismiss();
            }
        });

        datas = new ArrayList<>();
        advisersAdapter = new AdvisersAdapter(getActivity(), datas, getWidth());
        rvAdvisers.setAdapter(advisersAdapter);

        //先拿缓存
        String advisersList = SPTools.getString("ADVISERS_LIST", "");
        if (!StringUtils.isActuallyEmpty(advisersList)) {
            List<Advisers> list = JSONObject.parseArray(advisersList, Advisers.class);
            datas.addAll(list);
            advisersAdapter.notifyDataSetChanged();
        }

        //再请求覆盖
        getAdvisers();

    }

    /**
     * 请求获取投资顾问
     */
    private void getAdvisers() {
        new GGOKHTTP(UserUtils.getTokenParams(), GGOKHTTP.GET_MY_ADVISERS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    datas.clear();
                    List<Advisers> data = JSONObject.parseObject(responseInfo, AdvisersBean.class).getData();
                    SPTools.saveString("ADVISERS_LIST", JSONObject.toJSONString(data));
                    datas.addAll(data);
                    advisersAdapter.notifyDataSetChanged();

                } else if (code == 1001) {

                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getContext(), msg);
            }
        }).startGet();
    }

}
