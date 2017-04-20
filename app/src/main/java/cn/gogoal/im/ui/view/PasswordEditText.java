package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/4/20 0020.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class PasswordEditText extends LinearLayoutCompat {

    private SearchView etPassword;

    private AppCompatImageView imageFlag;

    private EditText etInput;

    private AppCompatCheckBox checkBoxPassword;

    public PasswordEditText(Context context) {
        this(context, null, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_layout_password, this);
        etPassword = (SearchView) findViewById(R.id.et_password);

        checkBoxPassword= (AppCompatCheckBox) findViewById(R.id.checkbox_psw);
        etPassword = (SearchView) findViewById(R.id.et_password);
        etInput = (EditText) etPassword.findViewById(R.id.search_src_text);

        checkBoxPassword.setButtonDrawable(ContextCompat.getDrawable(context,R.mipmap.icon_visible));

    }
}
