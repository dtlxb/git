package com.gogoal.app.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gogoal.app.R;
import com.gogoal.app.ui.view.SelectorButton;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class EditTextDialog extends BaseBottomDialog {

    private EditText mEditText;

    private SelectorButton btnSend;

    OnSendMessageListener listener;
    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_text;
    }

    @Override
    public void bindView(View v) {
        mEditText = (EditText) v.findViewById(R.id.edit_text);
        btnSend= (SelectorButton) v.findViewById(R.id.dialog_send);

        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null && (!TextUtils.isEmpty(mEditText.getText().toString()))){
                    listener.doSend(v,mEditText.getText().toString());
                }
            }
        });
    }

    public void setOnSendButtonClick(OnSendMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public float getDimAmount() {
        return 0.6f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnSendMessageListener{
        void doSend(View view,String msg);
    }
}