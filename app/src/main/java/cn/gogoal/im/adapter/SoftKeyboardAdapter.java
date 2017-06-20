package cn.gogoal.im.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.Face2FaceActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SoftKeyboard;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.TextDrawable;

/**
 * author wangjd on 2017/6/9 0009.
 * Staff_id 1375
 * phone 18930640263
 * description :自定义键盘适配器
 */
public class SoftKeyboardAdapter extends CommonAdapter<SoftKeyboard, BaseViewHolder> {

    private Context context;

    public SoftKeyboardAdapter(Context context, List<SoftKeyboard> data) {
        super(R.layout.item_soft_input, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final SoftKeyboard data, final int position) {
        final ImageView itemImage = (ImageView) holder.itemView;

        if (TextUtils.isEmpty(data.getNum())) {
            if (data.getDrawable() == null) {
                itemImage.setImageDrawable(new ColorDrawable(0x00000000));
            } else {
                itemImage.setImageDrawable(data.getDrawable());
            }
        } else {
            itemImage.setImageDrawable(getTextDrawable(data.getNum(), false));
        }

        itemImage.setClickable(data.getDrawable() != null || !TextUtils.isEmpty(data.getNum()));
        itemImage.setEnabled(data.getDrawable() != null || !TextUtils.isEmpty(data.getNum()));

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIHelper.toast(v.getContext(), "pos=" + position);
                Face2FaceActivity activity = (Face2FaceActivity) context;
                if (!TextUtils.isEmpty(data.getNum())) {
                    activity.inputImagePsw(data.getNum());
                } else {
                    activity.delImagePsw();
                }
            }
        });

        itemImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!TextUtils.isEmpty(data.getNum()))
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            itemImage.setImageDrawable(getTextDrawable(
                                    data.getNum(),true
                            ));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            itemImage.setImageDrawable(getTextDrawable(
                                    data.getNum(),true
                            ));
                            break;
                        case MotionEvent.ACTION_UP:
                            itemImage.setImageDrawable(getTextDrawable(
                                    data.getNum(),false
                            ));
                            break;
                        default:
                            itemImage.setImageDrawable(getTextDrawable(
                                    data.getNum(),false
                            ));
                            break;
                    }
                return false;
            }
        });

    }

    private Drawable getTextDrawable(String num, boolean pressed) {
        TextDrawable.IBuilder iBuilder = TextDrawable
                .builder()
                .beginConfig()
                .textColor(getResColor(pressed ? android.R.color.white : R.color.face2faceKeyTextColor))
                .fontSize(AppDevice.dp2px(context, 25))
                .endConfig().rect();
        return iBuilder.build(num, ContextCompat.getColor(context, R.color.face2faceBg));
    }

    public interface KeyNumClickListener {
        void onNumClick(String num);
    }
}