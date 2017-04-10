package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;

import cn.gogoal.im.R;
import cn.gogoal.im.common.MyStockSortInteface;


/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :排序控件.
 */
public class SortView extends AppCompatTextView {

    private MyStockSortInteface sortInteface;

    private volatile int sortType = 0;

    public int getSortType() {
        return sortType;
    }

    private String defaultText;

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public void seOntSortListener(MyStockSortInteface sortInteface) {
        this.sortInteface = sortInteface;
    }

    public SortView(Context context) {
        this(context, null, 0);
    }

    public SortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/sort.ttf");
        this.setTypeface(iconfont);
//        if (TextUtils.isEmpty(defaultText)) {
//            this.setClickable(false);
//            this.setFocusable(false);
//        }else {
        this.setClickable(true);
        this.setOnClickListener(new SortClick());
//        }
    }

    private class SortClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            KLog.e(((TextView) v).getText());
            if (sortType == 0) {
                if (sortInteface != null) {
                    sortInteface.doSort(v, 1);
                    setViewStateUp();
                }
            } else if (sortType == 1) {
                if (sortInteface != null) {
                    sortInteface.doSort(v, -1);
                    setViewStateDown();
                }
            } else if (sortType ==-1) {
                if (sortInteface != null) {
                    sortInteface.doSort(v, 0);
                    setViewStateNormal();
                }
            }
        }
    }

    public void setViewStateNormal() {
        sortType = 0;
        setText(String.format(getContext().getString(R.string.mystock_sort_normal), defaultText));
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor_333333));
    }

    public void setViewStateDown() {
        sortType=-1;
        setText(String.format(getContext().getString(R.string.mystock_sort_down), defaultText));
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    public void setViewStateUp() {
        sortType=1;
        setText(String.format(getContext().getString(R.string.mystock_sort_up), defaultText));
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }
}
