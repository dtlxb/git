package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * author wangjd on 2017/4/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class HintCenterEditText extends AppCompatEditText {

    private Drawable[] drawables;

    public HintCenterEditText(Context context) {
        super(context);
        init(context);
    }

    public HintCenterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HintCenterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        drawables=getCompoundDrawables();

        HintCenterEditText.this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    HintCenterEditText.this.setCompoundDrawables(null,null,null,null);
                }else {
                    HintCenterEditText.this.setCompoundDrawables(drawables[0],
                            drawables[1],drawables[2],drawables[3]);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 3, 0);
            }
        }
        super.onDraw(canvas);
    }

}
