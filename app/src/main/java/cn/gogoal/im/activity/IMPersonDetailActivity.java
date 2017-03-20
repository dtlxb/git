package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMpersonInfoAdapter;
import cn.gogoal.im.base.BaseActivity;

/**
 * Created by huangxx on 2017/3/17.
 */

public class IMPersonDetailActivity extends BaseActivity {

    @BindView(R.id.person_detail)
    RecyclerView personDetailRecycler;
    @BindView(R.id.add_friend_button)
    Button addFriendBtn;

    private IMpersonInfoAdapter mPersonInfoAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_person_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);

        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("基金活动1");
        strings.add("基金活动2");
        strings.add("基金活动3");
        initRecycleView(personDetailRecycler, R.drawable.shape_divider_1px);
        mPersonInfoAdapter = new IMpersonInfoAdapter(strings, IMPersonDetailActivity.this, this.getLayoutInflater());
        personDetailRecycler.setAdapter(mPersonInfoAdapter);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
