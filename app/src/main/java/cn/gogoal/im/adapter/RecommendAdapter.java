package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.SquareCardActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.bean.group.GroupMemberInfo;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/6/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :推荐群、搜索群
 */
public class RecommendAdapter extends CommonAdapter<GroupData, BaseViewHolder> {

//    private Bitmap groupAvatarBitmap;

    private Context context;

    public RecommendAdapter(Context context, List<GroupData> datas) {
        super(R.layout.item_search_type_persion, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final GroupData data, final int position) {
        final TextView addView = holder.getView(R.id.btn_search_group_add);
        final RoundedImageView imageView = holder.getView(R.id.item_user_avatar);

        final View itemView = holder.itemView;

        if (data.getM() == null || data.getM().isEmpty()) {
            itemView.setClickable(false);
            itemView.setEnabled(false);
        } else {
            itemView.setClickable(true);
            itemView.setEnabled(true);
        }

        addView.setVisibility(View.VISIBLE);
        if (data.is_in()) {
            addView.setBackgroundColor(Color.TRANSPARENT);
            addView.setText("已加入");
            addView.setTextColor(Color.parseColor("#a9a9a9"));
            addView.setClickable(false);
            addView.setEnabled(false);
            addView.setOnClickListener(null);
        } else {
            addView.setBackgroundResource(R.drawable.shape_search_group_add_btn);
            addView.setText("加入");
            addView.setTextColor(Color.parseColor("#a9a9a9"));
            addView.setClickable(true);
            addView.setEnabled(true);

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatGroupHelper.applyIntoGroup(context,data.getConv_id());
                    addView.setText("等待审核");
                }
            });
        }

        //非官方群
        if (StringUtils.isActuallyEmpty(data.getAttr().getAvatar())) {
            ChatGroupHelper.setGroupAvatar(data.getConv_id(), new AvatarTakeListener() {
                @Override
                public void success(final Bitmap bitmap) {
                    ((FragmentActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void failed(Exception e) {
                    KLog.e("使用占位图头像--拼接出错");
                    ((FragmentActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(R.mipmap.image_placeholder);
                        }
                    });
                }
            });
        } else {
            final String groupUrl = data.getAttr().getAvatar();
            ImageDisplay.loadRoundedRectangleImage(context, groupUrl, imageView);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), SquareChatRoomActivity.class);
                if (data.is_in()) {//我在群里
                    //进入聊天
                    in.putExtra("squareName", data.getName());
                    in.putExtra("conversation_id", data.getConv_id());
                    context.startActivity(in);
                } else {//群名片
                    in = new Intent(context, SquareCardActivity.class);
                    in.putExtra("conversation_id", data.getConv_id());
                    in.putExtra("square_name", data.getName());
                    in.putExtra("square_creater", data.getC());
                    in.putParcelableArrayListExtra("square_members", data.getM_info());
                    context.startActivity(in);
                }
            }
        });

        UIHelper.setRippBg(itemView);

        holder.setText(R.id.item_tv_search_result_intro, TextUtils.isEmpty(data.getAttr().getIntro()) ? "暂无群简介" : data.getAttr().getIntro());

        holder.setText(R.id.item_tv_search_result_name, data.getName() +
                String.format(context.getString(R.string.str_group_count), null == data.getM() ? 0 : data.getM().size()));

    }

    private List<String> getImageAvatar(List<GroupMemberInfo> datas) {
        List<String> li = new ArrayList<>();
        if (null != datas && !datas.isEmpty()) {
            for (GroupMemberInfo b : datas) {
                li.add(b.getAvatar());
            }
            return li;
        }
        return new ArrayList<>();
    }
}
