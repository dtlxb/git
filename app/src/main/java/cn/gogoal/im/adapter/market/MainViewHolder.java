package cn.gogoal.im.adapter.market;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    public static volatile int existing = 0;
    public static int createdTimes = 0;

    public MainViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        createdTimes++;
        existing++;
    }

    @Override
    protected void finalize() throws Throwable {
        existing--;
        super.finalize();
    }

    public Context getItemContext(){
        return context;
    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    public void setText(@IdRes int resId, String text) {
        TextView textView = findView(resId);
        textView.setText(text);
    }

    public void setTextResColor(@IdRes int resId, @ColorRes int textColor) {
        TextView textView = findView(resId);
        textView.setTextColor(ContextCompat.getColor(context,textColor));
    }
    public void setTextColor(@IdRes int resId, @ColorInt int textColor) {
        TextView textView = findView(resId);
        textView.setTextColor(textColor);
    }

    public void setBackgroundRes(@IdRes int resId, @DrawableRes int bgRes){
        View view=findView(resId);
        view.setBackgroundResource(bgRes);
    }

    public @ColorInt int getResColor(@ColorRes int colorRes){
        return ContextCompat.getColor(context,colorRes);
    }

    public void setOnClickListener(@IdRes int viewId, View.OnClickListener listener){
        if (listener!=null) {
            findView(viewId).setOnClickListener(listener);
        }
    }
}
