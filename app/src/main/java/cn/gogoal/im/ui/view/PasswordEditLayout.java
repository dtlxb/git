package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/4/20 0020.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class PasswordEditLayout extends LinearLayout {

    private XEditText xEditText;

    private AppCompatCheckBox checkBoxPassword;

    public PasswordEditLayout(Context context) {
        this(context,null,0);
    }

    public PasswordEditLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PasswordEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        LayoutInflater.from(context).inflate(R.layout.view_password,this);

        xEditText= (XEditText) findViewById(R.id.et_psw);
        checkBoxPassword= (AppCompatCheckBox) findViewById(R.id.checkbox_psw);

        xEditText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        checkBoxPassword.setButtonDrawable(ContextCompat.getDrawable(context,R.mipmap.img_psw_eye_closed));

        checkBoxPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    xEditText.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());  //密码以明文显示
                    xEditText.setSelection(xEditText.getText().length());
                    checkBoxPassword.setButtonDrawable(ContextCompat.getDrawable(context, R.mipmap.img_psw_eye_open));
                } else {
                    xEditText.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());  //以密文显示
                    xEditText.setSelection(xEditText.getText().length());
                    checkBoxPassword.setButtonDrawable(ContextCompat.getDrawable(context, R.mipmap.img_psw_eye_closed));
                }
            }
        });
    }
}
