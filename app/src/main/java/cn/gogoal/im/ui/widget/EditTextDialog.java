package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class EditTextDialog extends BaseBottomDialog {

    private EditText mEditText;

    private TextView btnSend;

    OnSendMessageListener listener;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_live_chat;
    }

    @Override
    public void bindView(View v) {
        mEditText = (EditText) v.findViewById(R.id.player_edit);
        btnSend = (TextView) v.findViewById(R.id.send_text);

        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditText.getText().toString().equals("") || !mEditText.getText().toString().matches(".*[^ ].*")) {
                    btnSend.setVisibility(View.GONE);
                } else {
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.doSend(v, mEditText);
                }
            }
        });
    }

    public void setOnSendButtonClick(OnSendMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public float getDimAmount() {
        return 0f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnSendMessageListener {
        void doSend(View view, EditText mEditText);
    }
}