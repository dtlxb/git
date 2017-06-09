package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.TextDrawable;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/6/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :面对面建群
 */
public class Face2FaceActivity extends BaseActivity {

    private static final int face2faceBgColoe = 0xff1b1f22;

    private static final int face2faceTextColoe = 0xff626669;

    @BindView(R.id.rv_soft_input)
    RecyclerView rvSoftInput;

    private List<Drawable> keyboardDrawables;

    private SoftKeyboardAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_face2face;
    }

    @Override
    public void doBusiness(Context mContext) {
        setStatusColor(face2faceBgColoe);
        XTitle xTitle = setMyTitle("面对面建群", true);
        xTitle.setLeftImageResource(R.mipmap.image_title_back_255);
        xTitle.setBackgroundColor(face2faceBgColoe);
        xTitle.setLeftTextColor(Color.WHITE);
        xTitle.setTitleColor(Color.WHITE);

        keyboardDrawables = new ArrayList<>();
        adapter = new SoftKeyboardAdapter(keyboardDrawables);
        rvSoftInput.setAdapter(adapter);
        rvSoftInput.addItemDecoration(new SoftDivider());
        makeDatas();
    }

    private void makeDatas() {
        for (int i = 1; i < 10; i++) {
            keyboardDrawables.add(getTextDrawable(i));
        }

        keyboardDrawables.add(new ColorDrawable(0x00000000));
        keyboardDrawables.add(TextDrawable.builder().buildRect(String.valueOf(0), 0xff626669));
        keyboardDrawables.add(ContextCompat.getDrawable(getActivity(), R.mipmap.img_keyboard_del));
        adapter.notifyDataSetChanged();
    }

    private Drawable getTextDrawable(int num){
        TextDrawable.IBuilder iBuilder = TextDrawable
                .builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .endConfig().rect();
        return iBuilder.build(String.valueOf(num),face2faceBgColoe);
    }

    private class SoftKeyboardAdapter extends CommonAdapter<Drawable, BaseViewHolder> {

        private SoftKeyboardAdapter(List<Drawable> data) {
            super(R.layout.item_soft_input, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, Drawable data, int position) {
            holder.setImageDrawable(R.id.iv_item_keyboard_del, data);

        }
    }

    private class SoftDivider extends XDividerItemDecoration {

        private SoftDivider() {
            super(Face2FaceActivity.this, 1, 0xff292e31);
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] show = new boolean[4];
            show[1] = true;
            show[2] = (1 + itemPosition) % 3 != 0;
            return show;
        }
    }
}
