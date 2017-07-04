package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/25.
 */

public class KlineSettingActivity extends BaseActivity {

    private XTitle xTitle;
    private TextView titleTextView;

    //复权
    @BindView(R.id.group_authority)
    RadioGroup group_authority;
    @BindView(R.id.authority_rb1)
    RadioButton authority_rb1;
    @BindView(R.id.authority_rb2)
    RadioButton authority_rb2;
    @BindView(R.id.authority_rb3)
    RadioButton authority_rb3;

    //k线空实心
    @BindView(R.id.group_kline)
    RadioGroup group_kline;
    @BindView(R.id.kline_b1)
    RadioButton kline_b1;
    @BindView(R.id.kline_b2)
    RadioButton kline_b2;

    //光标跟随与否
    @BindView(R.id.group_light)
    RadioGroup group_light;
    @BindView(R.id.rb_follow)
    RadioButton rb_follow;
    @BindView(R.id.rb_unfollow)
    RadioButton rb_unfollow;

    //均线设置
    @BindView(R.id.et_ln1)
    EditText et_ln1;
    @BindView(R.id.et_ln2)
    EditText et_ln2;
    @BindView(R.id.et_ln3)
    EditText et_ln3;
    @BindView(R.id.tv_day1)
    TextView tv_day1;
    @BindView(R.id.tv_day2)
    TextView tv_day2;
    @BindView(R.id.tv_day3)
    TextView tv_day3;

    //蜡烛是否空心
    private boolean hollow;
    //十字是否跟随
    private boolean follow;
    //复权类型 0 不复权  1 前复权 2 后复权
    private Integer authority_type;
    //数字范围过滤
    InputFilter[] filters;
    int num1;
    int num2;
    int num3;

    @Override
    public int bindLayout() {
        return R.layout.activity_kline_setting;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        initData();
        intClick();
    }

    private void initTitle() {
        xTitle = setMyTitle("K线设置", true);
        XTitle.TextAction textAction = new XTitle.TextAction("恢复默认") {
            @Override
            public void actionClick(View view) {
                DialogHelp.getConfirmDialog(KlineSettingActivity.this, "确定K线设置恢复至默认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPTools.saveInt("authority_type", 0);
                        SPTools.saveBoolean("hollow", false);
                        SPTools.saveBoolean("follow", false);
                        SPTools.saveInt("et_ln1", 5);
                        SPTools.saveInt("et_ln2", 10);
                        SPTools.saveInt("et_ln3", 20);
                        initData();
                    }
                }).show();
            }
        };
        xTitle.addAction(textAction, 0);
        titleTextView = (TextView) xTitle.getViewByAction(textAction);
        titleTextView.setTextColor(getResColor(R.color.title_text_color));
    }

    private void initData() {
        //输入框输入设置
        authority_type = SPTools.getInt("authority_type", 0);
        hollow = SPTools.getBoolean("hollow", false);
        follow = SPTools.getBoolean("follow", false);
        filters = new InputFilter[]{new EditInputFilter()};
        et_ln1.setFilters(filters);
        et_ln2.setFilters(filters);
        et_ln3.setFilters(filters);
        num1 = SPTools.getInt("et_ln1", 5);
        num2 = SPTools.getInt("et_ln2", 10);
        num3 = SPTools.getInt("et_ln3", 20);
        et_ln1.setText(String.valueOf(num1));
        et_ln2.setText(String.valueOf(num2));
        et_ln3.setText(String.valueOf(num3));
        clearThisGuy("et_ln1");
        clearThisGuy("et_ln2");
        clearThisGuy("et_ln3");
        switch (authority_type) {
            case 0:
                authority_rb1.setChecked(true);
                break;
            case 1:
                authority_rb2.setChecked(true);
                break;
            case 2:
                authority_rb3.setChecked(true);
                break;
        }
        if (hollow) {
            kline_b2.setChecked(true);
        } else {
            kline_b1.setChecked(true);
        }
        if (follow) {
            rb_follow.setChecked(true);
        } else {
            rb_unfollow.setChecked(true);
        }
    }

    private void intClick() {

        group_authority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int rbId = group.getCheckedRadioButtonId();
                SPTools.saveInt("authority_type", rbId == authority_rb1.getId() ? 0 : (rbId == authority_rb2.getId() ? 1 : 2));
            }
        });

        group_kline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int rbId = group.getCheckedRadioButtonId();
                SPTools.saveBoolean("hollow", rbId == kline_b2.getId());
            }
        });

        group_light.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int rbId = group.getCheckedRadioButtonId();
                SPTools.saveBoolean("follow", rbId == rb_follow.getId());
            }
        });

        et_ln1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditTextSet(et_ln1, tv_day1);
                clearThisGuy("et_ln2");
                clearThisGuy("et_ln3");
                return false;
            }
        });
        et_ln2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditTextSet(et_ln2, tv_day2);
                clearThisGuy("et_ln1");
                clearThisGuy("et_ln3");
                return false;
            }
        });
        et_ln3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditTextSet(et_ln3, tv_day3);
                clearThisGuy("et_ln1");
                clearThisGuy("et_ln2");
                return false;
            }
        });

        addTextWatcher(et_ln1, "et_ln1");
        addTextWatcher(et_ln2, "et_ln2");
        addTextWatcher(et_ln3, "et_ln3");

    }

    private void clearThisGuy(String tag) {
        switch (tag) {
            case "et_ln1":
                if (TextUtils.isEmpty(et_ln1.getText().toString()) || num1 == 0) {
                    et_ln1.setText("");
                    tv_day1.setText("不显示");
                }
                break;
            case "et_ln2":
                if (TextUtils.isEmpty(et_ln2.getText().toString()) || num2 == 0) {
                    et_ln2.setText("");
                    tv_day2.setText("不显示");
                }
                break;
            case "et_ln3":
                if (TextUtils.isEmpty(et_ln3.getText().toString()) || num3 == 0) {
                    et_ln3.setText("");
                    tv_day3.setText("不显示");
                }
                break;
            default:
                break;
        }
    }

    private void EditTextSet(EditText editText, TextView textView) {

        textView.setText("日");
        editText.setText("");
        editText.setFocusable(true);
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        editText.setSelection(editText.getText().length());
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, 0);
    }

    private void addTextWatcher(final EditText editText, final String editWhat) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    switch (editWhat) {
                        case "et_ln1":
                            num1 = Integer.parseInt(s.toString().trim());
                            SPTools.saveInt("et_ln1", num1);
                            break;
                        case "et_ln2":
                            num2 = Integer.parseInt(s.toString().trim());
                            SPTools.saveInt("et_ln2", num2);
                            break;
                        case "et_ln3":
                            num3 = Integer.parseInt(s.toString().trim());
                            SPTools.saveInt("et_ln3", num3);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private class EditInputFilter implements InputFilter {
        /**
         * 最大数字
         */
        static final int MAX_VALUE = 250;
        static final int MIN_VALUE = 0;

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String oldString = dest.toString();

            if ("".equals(source.toString())) {
                return null;
            }

            if (!source.toString().equals("")) {
                int length = source.toString().length();
                for (int i = 0; i < length; i++) {
                    char c = source.toString().charAt(i);
                    if (c < '0' || c > '9') {
                        return "";
                    }
                }
                Integer num = Integer.parseInt(oldString + source.toString());
                if (num > MAX_VALUE || num < MIN_VALUE) {
                    return dest.subSequence(dstart, dend);
                }
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    }
}
