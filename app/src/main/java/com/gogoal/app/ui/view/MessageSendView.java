package com.gogoal.app.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogoal.app.R;

/**
 * Created by huangxx on 2016/11/16.
 */

public class MessageSendView extends LinearLayout {

    private EditText input_text;
    private TextView send_text;
    private InputMethodManager inputMethodManager;
    private LiveMessageDelegate delegate;

    public void setDelegate(LiveMessageDelegate delegate) {
        this.delegate = delegate;
    }

    public interface LiveMessageDelegate {
        void sendMessage(String text);

        void onClick(int type);
    }

    public MessageSendView(Context context) {
        super(context);
        initView(context);
    }

    public MessageSendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MessageSendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        View.inflate(context, R.layout.message_send_view, this);
        input_text = (EditText) findViewById(R.id.live_chat_edit);
        send_text = (TextView) findViewById(R.id.message_send);
        inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (input_text.getText().toString().trim().equals("")) {
                } else {
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input_text.getText().toString().trim().equals("")) {
                } else {
                }
            }
        });

        send_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //键盘收起
                //hide_keyboard_from(input_text);
                if (null != delegate) {
                    delegate.sendMessage(input_text.getText().toString());
                }
                input_text.setText("");
            }
        });

    }



    public void hide_keyboard_from(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void show_keyboard_from(View view) {
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
