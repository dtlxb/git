package cn.gogoal.im.adapter;

import android.content.Context;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SocialLiveData;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: description
 */
public class SocialLiveAdapter extends CommonAdapter<SocialLiveData, BaseViewHolder> {

    private Context context;

    public SocialLiveAdapter(Context context, List<SocialLiveData> data) {
        super(R.layout.item_social_live, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, SocialLiveData data, int position) {

    }
}
