package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.XLayout;

/**
 * Created by huangxx on 2017/5/15.
 */

public class SearchMessagesActivity extends BaseActivity {

    @BindView(R.id.xLayout)
    XLayout xLayout;
    @BindView(R.id.layout_2search)
    AppCompatEditText layout2search;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public int bindLayout() {
        return R.layout.activity_message_search;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.im_str_look_message, true);
        layout2search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(layout2search.getText())) {

                        AppDevice.hideSoftKeyboard(layout2search);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
