package cn.gogoal.im.adapter;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
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
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :投资顾问弹窗和我的专属投资顾问共同的适配器
 */
public class AdvisersAdapter extends CommonAdapter<Advisers, BaseViewHolder> {

    private int itemAvatarSize;
    private Activity context;

    private DialogFragment dialogFragment;

    public AdvisersAdapter(Activity context, List<Advisers> datas, int itemAvatarSize) {
        super(R.layout.item_dialog_advisers, datas);

        this.itemAvatarSize = itemAvatarSize;
        this.context = context;
    }

    public AdvisersAdapter(Activity context,DialogFragment dialogFragment, List<Advisers> datas, int itemAvatarSize) {
        super(R.layout.item_dialog_advisers, datas);

        this.context = context;
        this.dialogFragment=dialogFragment;
        this.itemAvatarSize = itemAvatarSize;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Advisers data, int position) {
        ImageView imageAvatar = holder.getView(R.id.img_advisers_avatar);

        ViewGroup.LayoutParams params = imageAvatar.getLayoutParams();
        params.width = itemAvatarSize;
        params.height = itemAvatarSize;
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
                if (dialogFragment!=null){
                    dialogFragment.dismiss();
                }
            }
        });

        holder.getView(R.id.layout_advisers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDevice.copyTextToBoard(context,data.getSaler_name()+":"+data.getSaler_mobile());
                UIHelper.toast(context,"专属顾问信息已复制到剪切板");
            }
        });
    }
}