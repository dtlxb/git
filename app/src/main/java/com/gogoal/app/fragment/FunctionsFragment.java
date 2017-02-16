package com.gogoal.app.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.FunctionBean;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 应用
 */
public class FunctionsFragment extends BaseFragment {

    @BindView(R.id.function_list)
    RecyclerView function_list;

    @BindView(R.id.report_list)
    RecyclerView report_list;

    private  List<FunctionBean> functions=new ArrayList<>();
    private  List<FunctionBean> reports=new ArrayList<>();

    public FunctionsFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_functions;
    }

    @Override
    public void doBusiness(Context mContext) {
        intData();
        FunctionsAdapter functionsAdapter=new FunctionsAdapter(functions);
        FunctionsAdapter reportsAdapter=new FunctionsAdapter(reports);
        function_list.addItemDecoration(new GridSpacingItemDecoration(3, AppDevice.dp2px(getContext(), 1), false));
        report_list.addItemDecoration(new GridSpacingItemDecoration(3, AppDevice.dp2px(getContext(), 1), false));
        function_list.setAdapter(functionsAdapter);
        report_list.setAdapter(reportsAdapter);
    }

    private void intData() {
        functions.clear();
        reports.clear();
        FunctionBean functionLive=new FunctionBean("Go-Goal直播",R.mipmap.function_icon_onlive);
        FunctionBean functionReport=new FunctionBean("中国研究员专业网",R.mipmap.function_icon_report);
        FunctionBean functionSchool=new FunctionBean("Go-Goal学院",R.mipmap.function_icon_school);
        FunctionBean functionOneQ=new FunctionBean("金融1Q",R.mipmap.function_icon_1q);

        functions.add(functionLive);
        functions.add(functionReport);
        functions.add(functionSchool);
        functions.add(functionOneQ);

        FunctionBean stockReport=new FunctionBean("精要研报",R.mipmap.function_icon_improtant_report);
        FunctionBean stockTitle=new FunctionBean("优选主题",R.mipmap.function_icon_title);
        FunctionBean stockNews=new FunctionBean("公司大事件",R.mipmap.function_icon_news);

        reports.add(stockReport);
        reports.add(stockTitle);
        reports.add(stockNews);
    }

    class FunctionsAdapter extends RecyclerView.Adapter<FunctionsAdapter.FunctionViewHolder>{

        List<FunctionBean> mData;
        public FunctionsAdapter(List<FunctionBean> list) {
            this.mData=list;
        }

        @Override
        public FunctionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FunctionViewHolder holder=new FunctionViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_function,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(FunctionViewHolder holder, int position) {
            holder.imageView.setImageResource(mData.get(position).getFunction_map());
            holder.textView.setText(mData.get(position).getFunction_name());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class FunctionViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView textView;

            public FunctionViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.function_icon);
                textView= (TextView) itemView.findViewById(R.id.function_name);
            }
        }

    }

}
