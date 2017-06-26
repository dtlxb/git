package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjd on 2016/11/8 0008.
 * @Staff_id 1375
 * @phone 18930640263
 */

public class WrapContentHeightViewPager extends ViewPager {

    private List<Integer> heightList = new ArrayList<>();

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightList.add(h);
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        setMinimumHeight(getLast2Height(heightList));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getLast2Height(List<Integer> list) {
        if (list==null || list.isEmpty()){
            return 0;
        }
        Integer[] array = list.toArray(new Integer[list.size()]);
        Arrays.sort(array);
        if (array.length>=2){
            return array[array.length - 2];
        }else {
            return list.get(0);
        }
    }
}
