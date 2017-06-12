package cn.gogoal.im.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.Face2FaceActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SoftKeyboard;
import cn.gogoal.im.common.AppDevice;

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
        final TextView tvNum = holder.getView(R.id.tv_item_keyboard_num);

        holder.setVisible(R.id.iv_item_keyboard_del, data.getDrawable() != null);
        tvNum.setVisibility(TextUtils.isEmpty(data.getNum()) ? View.GONE : View.VISIBLE);
        tvNum.setText(TextUtils.isEmpty(data.getNum()) ? "" : data.getNum());
        tvNum.setClickable(false);
        tvNum.setEnabled(false);

        holder.setImageDrawable(R.id.iv_item_keyboard_del,
                data.getDrawable() != null ? data.getDrawable() :
                        ContextCompat.getDrawable(context, android.R.color.transparent));


        final View itemView = holder.itemView;
        itemView.setClickable(data.getDrawable() != null || !TextUtils.isEmpty(data.getNum()));
        itemView.setEnabled(data.getDrawable() != null || !TextUtils.isEmpty(data.getNum()));

        if (Build.VERSION.SDK_INT >= 21) {
            itemView.setElevation(AppDevice.dp2px(context, 2));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIHelper.toast(v.getContext(), "pos=" + position);
                Face2FaceActivity activity= (Face2FaceActivity) context;
                if (!TextUtils.isEmpty(data.getNum())) {
                    activity.inputImagePsw(data.getNum());
                    KLog.e("输入密码=====>"+data.getNum());
                }else {
                    activity.delImagePsw();
                    KLog.e("删除密码=====");
                }
            }
        });

//        itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (!TextUtils.isEmpty(data.getNum())) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            tvNum.setTextColor(Color.WHITE);
//                            itemView.setBackgroundResource(R.drawable.selector_soft_press);
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            tvNum.setTextColor(Color.parseColor("#626669"));
//                            itemView.setBackgroundColor(Color.TRANSPARENT);
//                            break;
//                    }
//                }
//                return false;
//            }
//        });
    }
}