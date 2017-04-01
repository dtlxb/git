package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.TestActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ImageTextBean;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.WeakReferenceHandler;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.rv_mine)
    RecyclerView rvMine;

    @BindView(R.id.img_mine_avatar)
    ImageView imageAvatar;

    @BindView(R.id.item_mine)
    LinearLayout itemSetting;

    final List<String> image = new ArrayList<>();

    private WeakReferenceHandler<MineFragment> handler = new WeakReferenceHandler<MineFragment>(Looper.getMainLooper(), this) {
        @Override
        protected void handleMessage(MineFragment fragment, Message message) {
            Glide.clear(imageAvatar);
            Bitmap bitmap = (Bitmap) message.obj;
            imageAvatar.setImageBitmap(bitmap);
            endIndex--;
        }
    };
    private int endIndex=9;

    public MineFragment() {
    }

    @BindArray(R.array.mine_arr)
    String[] mineTitle;

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(getString(R.string.title_mine));
        rvMine.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvMine.addItemDecoration(new NormalItemDecoration(mContext));
        rvMine.setAdapter(new MineAdapter(mContext, iniData()));

        image.add("http://wx.qlogo.cn/mmopen/80ZltqkYFOb4h4anTRINWvjKlnsRJUAQibW13cNsplFcU7Cb1XbYKUxbUKia9TeAHntNcEWN7Sqv4Rqa8wpdCeYpJ3KO0bnzIw/0");
        image.add("http://hackfile.ufile.ucloud.cn/avatar_1_500%C3%97500.jpg");
        image.add("http://wx.qlogo.cn/mmopen/KicTFAbRPtbEh0AlDp80uZwQNgLgj7L9jtvV5MS2tic4QbRD5RIdfAP6AUzwZxf3RFEJ9NuLO8Lt1sHPxnPUSajOecVtgZ100w/0");
        image.add("http://wx.qlogo.cn/mmopen/B3tJFu5Es25YBrHcTDtqrnicufZ5fGRficO2rKh0da2l8Uysv3F7kZ7icicbiaCD8w84jbAonRFyWmia0ChtN52ugMUBcH0OcUkMEw/0");
        image.add("http://wx.qlogo.cn/mmopen/w8nankfwONibXcBrZOIUrKU1icZYVyFa5LC7Y1MiaIjB8wmDxxxjuWGCmJE8oY3FMljDe6TCiapvpxPBnibUK7E0tarIxKJdMUJHc/0");
        image.add("http://wx.qlogo.cn/mmopen/ibdcYntS4HaY3F9bIUSb5VgXafVDTxGOM4GlXiceqdUrpyEPib4wkHVyD6dImNs2IntswxxTQmulfVgGAdibKSQqdA/0");
        image.add("http://wx.qlogo.cn/mmopen/w8nankfwON861nzpic7ufwUfTtmUDdhVHWc2OOLfEe7h9ibC7oXNZInaUyicjn8QRYZwPKeia0m9bXic18gFJMRBGvzTDsB0SY73d/0");
        image.add("http://wx.qlogo.cn/mmopen/ajNVdqHZLLAMhuMfbmcumIo5YIpx6sYBIzvniatIV7Wbw1KRln4ONKjx2SLsTqib7pAhHTvvzjRTMkKMuDibWuZjA/0");
        image.add("http://wx.qlogo.cn/mmopen/PiajxSqBRaEKlabJJEx5lOHd2ric2mFPWAiaicdVmoJf77vfdPGGajtLmXUOBeFcnUMB1TkY3RZ7YeNvibE7K3TOib3w/0");

    }

    private List<ImageTextBean<Integer>> iniData() {
        List<ImageTextBean<Integer>> list = new ArrayList<>();
        for (String con : mineTitle) {
            list.add(new ImageTextBean<>(R.mipmap.image_placeholder, con));
        }
        return list;
    }

    @OnClick({R.id.item_mine})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_mine:

                test();

                break;
        }
    }

    private void test() {
        if (endIndex>=1) {
            Glide.get(getContext()).clearMemory();

            GroupFaceImage.getInstance(getActivity(),
                    image.subList(0, endIndex)
            ).load(new GroupFaceImage.OnMatchingListener() {
                @Override
                public void onSuccess(Bitmap mathingBitmap) {

                    Glide.get(getContext()).clearDiskCache();

                    Message message = handler.obtainMessage();
                    message.obj = mathingBitmap;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else {
            endIndex=9;
        }
    }

    private class MineAdapter extends CommonAdapter<ImageTextBean<Integer>> {

        private MineAdapter(Context context, List<ImageTextBean<Integer>> datas) {
            super(context, R.layout.item_mine, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final ImageTextBean<Integer> data, final int position) {
            holder.setImageResource(R.id.iv_mine_item_icon, data.getIamge());
            holder.setText(R.id.tv_mine_item_text, data.getText());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), TestActivity.class));
                            break;
                        default:
                            UIHelper.toast(v.getContext(), data.getText());
                            break;
                    }
                }
            });
        }
    }

}
