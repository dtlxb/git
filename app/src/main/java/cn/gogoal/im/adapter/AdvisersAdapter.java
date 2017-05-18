package cn.gogoal.im.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MyAdvisersActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class AdvisersAdapter extends CommonAdapter<Advisers, BaseViewHolder> {

    private int dialogSize;
    private Activity context;

    public AdvisersAdapter(Activity context, List<Advisers> datas, int width) {
        super(R.layout.item_dialog_advisers, datas);
        dialogSize = width;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Advisers data, int position) {
        ImageView imageAvatar = holder.getView(R.id.img_advisers_avatar);
        ViewGroup.LayoutParams params = imageAvatar.getLayoutParams();
        params.width = dialogSize;
        params.height = dialogSize;
        imageAvatar.setLayoutParams(params);

        holder.setImageUrl(context, R.id.img_advisers_avatar, data.getSaler_photo());

        holder.setText(R.id.tv_advisers_phone, StringUtils.formatPhoneNum(data.getSaler_mobile()));

        holder.setText(R.id.tv_advisers_name, data.getSaler_name());

        ImageView imgDail = holder.getView(R.id.img_dail);

        if (context instanceof MyAdvisersActivity){
            imgDail.setImageResource(R.mipmap.img_dail_f00);
        }else {
            imgDail.setImageResource(R.mipmap.img_dail_000);
        }

        imgDail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDevice.openDial(context, data.getSaler_mobile());
            }
        });
    }
}