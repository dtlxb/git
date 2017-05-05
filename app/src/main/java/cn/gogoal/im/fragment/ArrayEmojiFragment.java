package cn.gogoal.im.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.EmojiBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGEmoticons;
import cn.gogoal.im.common.SPTools;

/**
 * Created by huangxx on 2017/5/3.
 */

public class ArrayEmojiFragment extends Fragment {

    int mNum;
    private RecyclerView emoji_recyclerview;
    private List<EmojiBean> emojiBeanList;
    private List<EmojiBean> emojiBeanTotalList;
    private EmojiAdapter adapter;
    private int keyBordHeight;

    public static ArrayEmojiFragment newInstance(int num) {
        return new ArrayEmojiFragment(num);
    }

    public ArrayEmojiFragment(int mNum) {
        this.mNum = mNum;
    }

    public EmojiAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里加载每个 fragment的显示的 View
        View view = inflater.inflate(R.layout.fragment_array_emoji, container, false);
        emoji_recyclerview = ((RecyclerView) view.findViewById(R.id.emoji_recyclerview));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        initData();
        adapter = new EmojiAdapter(R.layout.item_emoji, emojiBeanList);
        emoji_recyclerview.setLayoutManager(gridLayoutManager);
        emoji_recyclerview.setAdapter(adapter);
        return view;
    }

    private void initData() {
        keyBordHeight = SPTools.getInt("soft_keybord_height", AppDevice.dp2px(getActivity(), 220));

        emojiBeanList = new ArrayList<>();
        emojiBeanTotalList = new ArrayList<>();
        Iterator iterator = GGEmoticons.GGEmoticonHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            int val = (int) entry.getValue();
            EmojiBean emojiBean = new EmojiBean(key, val);
            emojiBeanTotalList.add(emojiBean);
        }
        if (mNum == 0) {
            for (int i = 0; i < 20; i++) {
                emojiBeanList.add(emojiBeanTotalList.get(i));
            }
        } else if (mNum == 1) {
            for (int i = 20; i < emojiBeanTotalList.size(); i++) {
                emojiBeanList.add(emojiBeanTotalList.get(i));
            }
        }
        emojiBeanList.add(new EmojiBean("[擦掉]", R.mipmap.img_emoji_20));
    }

    public class EmojiAdapter extends CommonAdapter<EmojiBean, BaseViewHolder> {

        public EmojiAdapter(int layoutId, List<EmojiBean> data) {
            super(layoutId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final EmojiBean data, final int position) {
            LinearLayout emoji_layout = holder.getView(R.id.emoji_layout);
            ViewGroup.LayoutParams params = emoji_layout.getLayoutParams();
            ImageView imageView = holder.getView(R.id.emoji_iv);
            params.height = (keyBordHeight - AppDevice.dp2px(getActivity(), 70)) / 3;
            params.width = AppDevice.getWidth(getActivity()) / 7;

            emoji_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("emojiBean", data);
                    BaseMessage baseMessage = new BaseMessage("emojiInfo", map);
                    AppManager.getInstance().sendMessage("oneEmoji", baseMessage);
                }
            });

            imageView.setImageResource(data.getEmojiUrl());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
