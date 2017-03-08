package cn.gogoal.im.adapter;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * author wangjd on 2017/3/2 0002.
 * Staff_id 1375
 * phone 18930640263
 *
 * 联系人列表适配器
 */
public class ContactAdapter extends CommonAdapter<ContactBean> {

    public ContactAdapter(Context context, List<ContactBean> datas) {
        super(context, R.layout.item_contacts, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ContactBean contactBean, int position) {
        holder.setText(R.id.item_contacts_tv_nickname,contactBean.getmName());

        holder.itemView.setBackgroundResource(R.drawable.selector_normal_write2gray);

        ImageView imageAvatar=holder.getView(R.id.item_contacts_iv_icon);

        try {
            String avatar=contactBean.getmAvatar().toString();
            if (avatar.contains("http")){
                ImageDisplay.loadNetImage(getmContext(),contactBean.getmAvatar().toString(),imageAvatar);
            }else {
                imageAvatar.setImageResource(Integer.parseInt(avatar));
            }

        }catch (Exception e){
            imageAvatar.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
