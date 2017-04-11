package cn.gogoal.im.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.MainAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.ui.view.XLayout;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

/**
 * 投研
 */
public class FoundFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindArray(R.array.found_fun0)
    String[] functionArr0;

    @BindArray(R.array.found_fun1)
    String[] functionArr1;

    public FoundFragment() {

    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        xLayout.setStatus(XLayout.Success);

        setFragmentTitle(R.string.title_found);
        initRecycleView(recyclerView, R.drawable.shape_divider_15dp);

        List<FoundData> datas = getFunctionDatas();//模拟数据

        MainAdapter mainAdapter = new MainAdapter(getContext(), R.layout.item_foundfragment, datas);
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.header_touyan,new LinearLayout(getContext()),false);
        mainAdapter.addHeaderView(headView);

        recyclerView.setAdapter(mainAdapter);
    }


    /**
     * 模拟数据，后续可以简单修改变成服务端请求数据
     */
    private List<FoundData> getFunctionDatas() {

        String[] titles = getResources().getStringArray(R.array.found_title);

        int[][] iconArray = {{R.mipmap.function_icon_onlive, R.mipmap.function_icon_report,
                R.mipmap.function_icon_school, R.mipmap.function_icon_1q, R.mipmap.cache_found_js_native},
                {R.mipmap.function_icon_improtant_report, R.mipmap.function_icon_title, R.mipmap.function_icon_news}};

        String[][] iconDescription = {functionArr0, functionArr1};

        List<FoundData> datas = new ArrayList<>();

        for (int i = 0; i < iconArray.length; i++) {

            List<FoundData.ItemPojos> itemPojoses = new ArrayList<>();

            for (int j = 0; j < iconArray[i].length; j++) {
                itemPojoses.add(new FoundData.ItemPojos(iconDescription[i][j], iconArray[i][j], "https://m.baidu.com"));
            }


            datas.add(new FoundData(itemPojoses, titles[i]));
        }

        //-----------------------添加测试--------------------------------

        FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("股票行情", R.mipmap.cache_found_js_native, "tag");
        FoundData.ItemPojos itemPojos2 = new FoundData.ItemPojos("自选股", R.mipmap.cache_found_js_native, "tag");
        FoundData.ItemPojos itemPojos3 = new FoundData.ItemPojos("个股详情", R.mipmap.cache_found_js_native, "tag");
        FoundData.ItemPojos itemPojos4 = new FoundData.ItemPojos("文字一分钟", R.mipmap.cache_found_js_native, "tag");

        List<FoundData.ItemPojos> listTest = new ArrayList<>();
        listTest.add(itemPojos1);
        listTest.add(itemPojos2);
        listTest.add(itemPojos3);
        listTest.add(itemPojos4);
        datas.add(new FoundData(listTest, "测试部分"));
        return datas;
    }

}
