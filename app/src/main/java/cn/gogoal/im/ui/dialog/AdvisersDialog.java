package cn.gogoal.im.ui.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AdvisersAdapter;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.Impl;
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
        advisersAdapter = new AdvisersAdapter(getActivity(), this, datas, 4 * getWidth() / 25);
        rvAdvisers.setAdapter(advisersAdapter);

        //先取缓存，再请求覆盖
        UserUtils.getAdvisers(new Impl<String>() {
            @Override
            public void response(int code, String data) {
                switch (code) {
                    case Impl.RESPON_DATA_SUCCESS:
                        datas.clear();
                        datas.addAll(JSONObject.parseArray(data, Advisers.class));
                        advisersAdapter.notifyDataSetChanged();
                        break;
                    default:
                        UIHelper.toast(getContext(), data);
                        break;
                }
            }
        });

    }

}
