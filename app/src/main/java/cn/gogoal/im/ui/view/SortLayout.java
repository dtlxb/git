package cn.gogoal.im.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/5/23 0023.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SortLayout extends RelativeLayout {

    private TextView textView;

    public SortLayout(Context context) {
        super(context);
    }

    public SortLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SortLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_sort,this);

    }
}
