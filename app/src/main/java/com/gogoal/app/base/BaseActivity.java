package com.gogoal.app.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gogoal.app.R;
import com.gogoal.app.ui.view.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * 通用的activity页面
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    private View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);

        setContentView(mContentView);

        ButterKnife.bind(this);

        initView(mContentView);

        doBusiness(this);
    }

    @Override
    public void initView(View view) {

    }

    public void setContentView(View contextView) {
        super.setContentView(mContentView);
        setStatusBar(R.color.colorPrimary);
    }

    protected void setStatusBar(int statusBarColorId) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, statusBarColorId), 0);
    }

    @Override
    public void resume() {
//        /**
//         * 设置为纵屏
//         */
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    /**
     * 初始化垂直的RecycleView
     */
    public void initRecycleView(RecyclerView recyclerView, Integer dividerId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (dividerId != null) {
            if (dividerId != 0x00) {
                try {
                    DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
                    itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), dividerId));//R.drawable.shape_divider
                    recyclerView.addItemDecoration(itemDecoration);
                } catch (Exception e) {
                    throw new IllegalArgumentException("initRecycleView(RecyclerView,Integer)第二个参数必须是一个分割线shape资源或者填0或者null");
                }
            } else {
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
            }
        }
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public BaseActivity getActivity() {
        return this;
    }

}
