package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ContactBean;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:联系人列表适配器
 */
public class ContactAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {
    List<ContactBean> list;

    public ContactAdapter(Context context, List<ContactBean> datas) {
        super(R.layout.item_contacts, datas);
        list = datas;
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactBean contactBean, int position) {
        TextView textView = holder.getView(R.id.item_contacts_tv_duty);
        holder.setText(R.id.item_contacts_tv_nickname, contactBean.getTarget());
        if (contactBean.getContactType() == ContactBean.ContactType.FUNCTION_ITEM) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(contactBean.getDuty());
        }

        holder.itemView.setBackgroundResource(R.drawable.selector_normal_write2gray);

        Object avatar = contactBean.getAvatar();
        if (avatar instanceof String) {
            holder.setImageUrl(R.id.item_contacts_iv_icon, avatar.toString());
        } else if (avatar instanceof Integer) {
            holder.setImageResource(R.id.item_contacts_iv_icon, (Integer) avatar);
        }

    }

    public void addItem(ContactBean contactBean) {
        list.add(contactBean);
        this.notifyDataSetChanged();
    }

}
