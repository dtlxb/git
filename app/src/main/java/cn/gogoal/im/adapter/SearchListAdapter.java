package cn.gogoal.im.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.ContactsActivity;
import cn.gogoal.im.activity.MyGroupsActivity;
import cn.gogoal.im.activity.PhoneContactsActivity;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.SearchBean;
import cn.gogoal.im.bean.SearchData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by huangxx on 2017/5/19.
 */

public class SearchListAdapter extends BaseSectionQuickAdapter<SearchData, BaseViewHolder> {

    private Activity mActivity;

    public SearchListAdapter(Activity context, List<SearchData> data) {
        super(R.layout.item_contacts, R.layout.item_search_type_title, data);
        this.mActivity = context;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, SearchData titleData) {
        holder.setText(R.id.search_item_title, titleData.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, SearchData data, int position) {

        final SearchBean searchBean = data.t;

        TextView textView = holder.getView(R.id.item_contacts_tv_duty);
        final RoundedImageView imageView = holder.getView(R.id.item_contacts_iv_icon);
        holder.setText(R.id.item_contacts_tv_nickname, searchBean.getNickname());

        if (!TextUtils.isEmpty(searchBean.getIntro())) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(searchBean.getIntro());
        } else {
            textView.setVisibility(View.GONE);
        }

        holder.itemView.setBackgroundResource(R.drawable.selector_normal_write2gray);

        if (searchBean.getChatType() == AppConst.IM_CHAT_TYPE_SQUARE) {
            ChatGroupHelper.setGroupAvatar(searchBean.getConversationId(), new AvatarTakeListener() {
                @Override
                public void success(final Bitmap bitmap) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                }

                @Override
                public void failed(Exception e) {

                }
            });
        } else {
            ImageDisplay.loadRoundedRectangleImage(mActivity, searchBean.getAvatar(), imageView);
        }

        //设置点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Bundle bundle = new Bundle();
                String convId = searchBean.getConversationId();
                String nickname = searchBean.getNickname();
                if (searchBean.getChatType() == AppConst.IM_CHAT_TYPE_SINGLE) {
                    intent = new Intent(mActivity, SingleChatRoomActivity.class);
                    bundle.putString("conversation_id", convId);
                    bundle.putString("nickname", nickname);
                    bundle.putBoolean("need_update", false);
                    intent.putExtras(bundle);
                } else if (searchBean.getChatType() == AppConst.IM_CHAT_TYPE_SQUARE) {
                    intent = new Intent(mActivity, SquareChatRoomActivity.class);
                    bundle.putString("conversation_id", convId);
                    bundle.putString("squareName", nickname);
                    bundle.putBoolean("need_update", true);
                    intent.putExtras(bundle);
                }
                mActivity.startActivity(intent);
                AppManager.getInstance().finishActivity(MyGroupsActivity.class);
                AppManager.getInstance().finishActivity(PhoneContactsActivity.class);
                AppManager.getInstance().finishActivity(ContactsActivity.class);
                mActivity.finish();
            }
        });
    }
}
