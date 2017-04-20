package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.gogoal.im.R;

/**
 * Created by huangxx on 2017/4/19.
 */

public class EditTextWithEye extends FrameLayout {

    private final SearchView.SearchAutoComplete etInput;
    private SearchView editText;
    private ImageView button;
    private RelativeLayout eyeLayout;


    public EditTextWithEye(Context context) {
        this(context, null);
    }


    public EditTextWithEye(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        //在构造函数中将布局文件clear_textview.xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.view_edittext_with_eye, this, true);
        //得到相应控件
        editText = (SearchView) findViewById(R.id.edittext);
        etInput = (SearchView.SearchAutoComplete) editText.findViewById(R.id.search_src_text);
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        button = (ImageView) findViewById(R.id.imagebutton);
        eyeLayout = (RelativeLayout) findViewById(R.id.layout_eye);
        //给button注册相应的事件
        eyeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("false")) {
                    v.setTag("true");
                    etInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    button.setImageResource(R.mipmap.login_eye_open);
                } else {
                    v.setTag("false");
                    button.setImageResource(R.mipmap.login_eye_closed);
                    etInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                editText.requestFocus();
            }
        });

    }

    //设置子控件相关属性的方法

    public void setEditTextHint(String hint) {
        this.editText.setQueryHint(hint);
    }

    public void setEditTextText(String s) {
        this.editText.setQuery(s, false);
    }

    public String getEditTextText() {
        return this.editText.getQuery().toString();
    }

}
