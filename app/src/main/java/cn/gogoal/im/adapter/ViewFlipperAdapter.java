package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.FlipperData;

/**
 * author wangjd on 2017/6/1 0001.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ViewFlipperAdapter extends BaseAdapter {

    private FlipperClickListener listener;
    private Context context;

    private List<FlipperData> flipperDataList;

    public ViewFlipperAdapter(Context context, List<FlipperData> flipperDataList) {
        this.context = context;
        this.flipperDataList = flipperDataList;
    }

    public void setOnFlipperClickListener(FlipperClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return flipperDataList == null ? 0 : flipperDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return flipperDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_flipper, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_item_view_flipper);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.textView.setSingleLine(false);

        holder.textView.setText(flipperDataList.get(position).getType_name()+" :"+
                flipperDataList.get(position).getContent());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.click(v, position);
                }
            }
        });

        final AnimationSet animationSet=new AnimationSet(true);

        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.down_to_center);
        final Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.center_to_up);

        animationSet.addAnimation(inAnimation);
        inAnimation.startNow();
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationSet.addAnimation(outAnimation);
                        KLog.e("插入新动画");
                    }
                },1500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setFillBefore(false);
        animationSet.setRepeatMode(AnimationSet.REVERSE);
        animationSet.setRepeatCount(AnimationSet.INFINITE);

        holder.textView.setAnimation(animationSet);
//        //添加动画
//        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
//        //设置插值器
//        animationSet.setInterpolator(new LinearInterpolator());
//        //设置动画持续时长
//        animationSet.setDuration(3000);
//        //设置动画结束之后是否保持动画的目标状态
//        animationSet.setFillAfter(true);
//        //设置动画结束之后是否保持动画开始时的状态
//        animationSet.setFillBefore(false);
//        //设置重复模式
//        animationSet.setRepeatMode(AnimationSet.REVERSE);
//        //设置重复次数
//        animationSet.setRepeatCount(AnimationSet.INFINITE);
//        //设置动画延时时间
//        animationSet.setStartOffset(2000);
//        //取消动画
//        animationSet.cancel();
//        //释放资源
//        animationSet.reset();
//        //开始动画
//        mIvImg.startAnimation(animationSet);

//        holder.textView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.down_to_center));

        return convertView;
    }


    public interface FlipperClickListener {
        void click(View view, int position);
    }

    public static class ViewHolder {
        private TextView textView;
    }
}
