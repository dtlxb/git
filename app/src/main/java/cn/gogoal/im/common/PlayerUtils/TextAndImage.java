package cn.gogoal.im.common.PlayerUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by dave.
 * Date: 2017/3/23.
 * Desc: 文字适配图片显示
 */

public class TextAndImage {

    /**
     * 该方法建立文本布局
     */
    public static void makeSpan(int finalHeight, int finalWidth, TextView mTextView) {

        /**
         * Get the text
         */
        String plainText = mTextView.getText().toString();
        Spanned htmlText = Html.fromHtml(plainText);
        SpannableString mSpannableString = new SpannableString(htmlText);

        int allTextStart = 0;
        int allTextEnd = htmlText.length() - 1;

        /**
         *计算线条数=图像高度。
         *你可以改进它…这只是一个例子
         */
        int lines;
        Rect bounds = new Rect();
        mTextView.getPaint().getTextBounds(plainText.substring(0, plainText.length()), 0, 1, bounds);

        //float textLineHeight = mTextView.getPaint().getTextSize();
        float fontSpacing = mTextView.getPaint().getFontSpacing();
        lines = (int) (finalHeight / (fontSpacing)) - 1;

        /**
         * Build the layout with LeadingMarginSpan2
         */
        MyLeadingMarginSpan2 span = new MyLeadingMarginSpan2(lines, finalWidth + 10);
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        mTextView.setText(mSpannableString);

    }

    /**
     *
     */
    static class MyLeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {

        private int margin;
        private int lines;

        MyLeadingMarginSpan2(int lines, int margin) {
            this.margin = margin;
            this.lines = lines;
        }

        /**
         * Apply the margin
         *
         * @param first
         * @return
         */
        @Override
        public int getLeadingMargin(boolean first) {
            if (first) {
                return margin;
            } else {
                return 0;
            }
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                      int top, int baseline, int bottom, CharSequence text,
                                      int start, int end, boolean first, Layout layout) {
        }


        @Override
        public int getLeadingMarginLineCount() {
            return lines;
        }
    }

}
