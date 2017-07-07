package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import cn.gogoal.im.R;
import cn.gogoal.im.common.MyStockSortInteface;
import cn.gogoal.im.ui.widget.CenterImageSpan;


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
        this.defaultText = defaultText + "  ";
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
        this.setGravity(Gravity.CENTER);
        this.setOnClickListener(new SortClick());
        this.setTextColor(ContextCompat.getColor(context, R.color.textColor_333333));
    }

    private class SortClick implements OnClickListener {
        @Override
        public void onClick(View v) {
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
            } else if (sortType == -1) {
                if (sortInteface != null) {
                    sortInteface.doSort(v, 0);
                    setViewStateNormal();
                }
            }
        }
    }

    public void setViewStateNormal() {
        sortType = 0;
        setText(setSortContent(R.mipmap.img_sort_normal));
    }

    public void setViewStateDown() {
        sortType = -1;
        setText(setSortContent(R.mipmap.img_sort_down));
    }

    public void setViewStateUp() {
        sortType = 1;
        setText(setSortContent(R.mipmap.img_sort_up));
    }


    private SpannableString setSortContent(int drawableId) {
        SpannableString spannableString = new SpannableString(defaultText);
        CenterImageSpan imageSpan = new CenterImageSpan(getContext(), drawableId);
        spannableString.setSpan(imageSpan, defaultText.length() - 1, defaultText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
