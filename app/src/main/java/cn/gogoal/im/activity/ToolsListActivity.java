package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SectionListAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.SectionToolsData;
import cn.gogoal.im.bean.ToolBean;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :工具中心
 */
public class ToolsListActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private List<SectionToolsData> toolsDatas;
    private SectionListAdapter sectionAdapter;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("工具中心", true);

        toolsDatas = new ArrayList<>();
        sectionAdapter = new SectionListAdapter(mContext, toolsDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setBackgroundColor(0xfff8f8f8);
        getToolsList();

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getToolsList();
            }
        });
    }

    private void getToolsList() {
        xLayout.setStatus(XLayout.Loading);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        new GGOKHTTP(map, GGOKHTTP.GET_ALLCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getInteger("code");
                if (code == 0) {
                    toolsDatas.clear();
                    List<ToolData> data = JSONObject.parseObject(responseInfo, ToolBean.class).getData();
                    for (int i = 0; i < data.size(); i++) {
                        ToolData toolData = data.get(i);

                        SectionToolsData titleData = new SectionToolsData(true, toolData.getTitle());
                        titleData.setParentPosition(-1);
                        toolsDatas.add(titleData);

                        List<ToolData.Tool> tools = toolData.getDatas();
                        for (int j=0;j<tools.size();j++) {
                            tools.get(j).setSimulatedArg(false);
                            SectionToolsData sectionToolsData = new SectionToolsData(tools.get(j), tools.size());
                            sectionToolsData.setParentPosition(i);
                            sectionToolsData.setChildPosition(j);
                            toolsDatas.add(sectionToolsData);
                        }
                    }
                    sectionAdapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }
}
