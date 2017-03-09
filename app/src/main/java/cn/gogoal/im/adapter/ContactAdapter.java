package cn.gogoal.im.adapter;

import android.content.Context;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.ContactBean;

/**
 * author wangjd on 2017/3/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 联系人列表适配器
 */
public class ContactAdapter extends CommonAdapter<ContactBean> {

    public ContactAdapter(Context context, List<ContactBean> datas) {
        super(context, R.layout.item_contacts, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ContactBean contactBean, int position) {
        holder.setText(R.id.item_contacts_tv_nickname, contactBean.getTarget());

        holder.itemView.setBackgroundResource(R.drawable.selector_normal_write2gray);

        Object avatar = contactBean.getAvatar();
        if (avatar instanceof String) {
            holder.setImageUrl(R.id.item_contacts_iv_icon,avatar.toString());
        }else if (avatar instanceof Integer){
            holder.setImageResource(R.id.item_contacts_iv_icon, (Integer) avatar);
        }

    }
}
