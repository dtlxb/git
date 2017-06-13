package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.common.AppDevice;

/**
 * author wangjd on 2017/6/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :添加朋友
 */
public class AddFraendListActivity extends BaseActivity {

    @BindArray(R.array.addFriendArray)
    String[] addFriendArray;

    int[] icons = {R.mipmap.img_add_friend,
            R.mipmap.img_add_group,
            R.mipmap.img_add_friend_scan,
            R.mipmap.chat_phone_contacts};

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layout_main)
    LinearLayout layoutMain;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(final Context context) {
        setMyTitle("添加朋友", true);

        final int iconImageMargin = AppDevice.dp2px(context,10);

        recyclerView.setAdapter(new CommonAdapter<BaseIconText<Integer, String>, BaseViewHolder>(
                R.layout.item_type_mine_icon_text, getDatas()) {
            @Override
            protected void convert(BaseViewHolder holder, BaseIconText<Integer, String> data, final int position) {
                AppCompatImageView itemImage = holder.getView(R.id.item_img_normal);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemImage.getLayoutParams();
                params.setMargins(AppDevice.dp2px(context,15), iconImageMargin, iconImageMargin, iconImageMargin);

                holder.setText(R.id.item_text_normal, data.getText());
                itemImage.setImageResource(data.getIamge());
                holder.setVisible(R.id.view_divider, position != addFriendArray.length - 1);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position<2) {
                            Intent intent = new Intent(context, SearchPersonSquareActivity.class);
                            intent.putExtra("search_index", position);
                            startActivity(intent);
                        }else if (position==3){
                            startActivity(new Intent(context,PhoneContactsActivity.class));
                        }else {
                            startActivity(new Intent(context,QrcodeProcessActivity.class));
                        }
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, AppDevice.dp2px(context, 20), 0, 0);
        recyclerView.setPadding(0, 1, 0, 1);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_line_top_and_bottom_unpress));
        layoutMain.setBackgroundColor(0xffEFEFF4);

    }

    private List<BaseIconText<Integer, String>> getDatas() {
        List<BaseIconText<Integer, String>> data = new ArrayList<>();
        for (int i = 0; i < addFriendArray.length; i++) {
            data.add(new BaseIconText<>(icons[i], addFriendArray[i]));
        }
        return data;
    }
}
