package cn.gogoal.im.ui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockSearchActivity;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :去股票搜索.
 */
public class MySearchView extends DrawableCenterTextView {

    public MySearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySearchView(Context context) {
        this(context,null,0);
    }

    private void init(final Context context){
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setBackgroundResource(R.drawable.shape_search_activity_edit);

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, StockSearchActivity.class));
            }
        });
    }
}
