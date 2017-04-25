package cn.gogoal.im.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gogoal.im.R;

public class ImageWithTextView extends LinearLayout {

    private ImageView imgMiddle;
    private TextView textMiddle;

    private int imgResource;
    private String textString;

    public ImageWithTextView(Context context) {
        super(context);
        initView(context);
    }

    public ImageWithTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ImageWithTextView);

        imgResource=typedArray.getResourceId(R.styleable.ImageWithTextView_MiddleImg, R.mipmap.icon_share_gogoal);
        textString=typedArray.getString(R.styleable.ImageWithTextView_MiddleString);

        imgMiddle.setImageResource(imgResource);
        textMiddle.setText(textString);

        typedArray.recycle();
    }

    public ImageWithTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_gridview_home_middle, this);

        imgMiddle= (ImageView) findViewById(R.id.img_home_middle_item);
        textMiddle= (TextView) findViewById(R.id.tv_home_middle_item);
    }
}
