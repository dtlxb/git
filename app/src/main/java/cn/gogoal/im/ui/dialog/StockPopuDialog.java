package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 个股底部 弹窗，文字一分钟，数据一分钟，同业比较，投资者互动
 */
public class StockPopuDialog extends BaseBottomDialog {

    public static StockPopuDialog newInstance(String stockCode, String stockName) {
        StockPopuDialog dialog = new StockPopuDialog();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", stockCode);
        bundle.putString("stock_name", stockName);
        dialog.setArguments(bundle);
        return dialog;
    }

    private int imgSize;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share_bottom;
    }

    @Override
    public void bindView(View v) {
        imgSize = 14 * AppDevice.getWidth(getActivity()) / 100;

        String stockCode = getArguments().getString("stock_code");
        String stockName = getArguments().getString("stock_name");

        RecyclerView rvShareList = (RecyclerView) v.findViewById(R.id.rv_share_list);
        TextView tvShareCancle = (TextView) v.findViewById(R.id.tv_dialog_cancle);

        rvShareList.setLayoutManager
                (new GridLayoutManager(
                        v.getContext(), 4));

        rvShareList.setAdapter(new BottomSheetAdapter(getDatas(), stockCode, stockName));

        tvShareCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockPopuDialog.this.dismiss();
            }
        });

    }

    private List<BaseIconText<Integer, String>> getDatas() {
        String[] desc = {"文字一分钟", "数据一分钟", "同业比较", "投资者互动"};
        int[] images = {R.mipmap.img_stock_words_minute, R.mipmap.img_stock_data_minute,
                R.mipmap.img_stock_profession_compare, R.mipmap.img_stock_interactiveinvestor};

        String[] urls = {AppConst.GG_TEXT1MINUTE, AppConst.GG_DATA1MINUTE, AppConst.GG_TONG_YE, null};

        List<BaseIconText<Integer, String>> datas = new ArrayList<>();
        for (int i = 0; i < desc.length; i++) {
            datas.add(new BaseIconText<>(images[i], desc[i], urls[i]));
        }
        return datas;
    }

    private class BottomSheetAdapter extends CommonAdapter<BaseIconText<Integer, String>, BaseViewHolder> {

        private String stockCode;
        private String stockName;

        private BottomSheetAdapter(List<BaseIconText<Integer, String>> data, String stockCode, String stockName) {
            super(R.layout.item_dialog_bottom_sheet, data);
            this.stockCode = stockCode;
            this.stockName = stockName;
        }

        @Override
        protected void convert(BaseViewHolder holder, final BaseIconText<Integer, String> data, final int position) {
            holder.setText(R.id.item_simple_list_vertical_text, data.getText());

            AppCompatImageView imageView = holder.getView(R.id.item_simple_list_vertical_image);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = imgSize;
            params.height = imgSize;
            imageView.setLayoutParams(params);
            imageView.setImageResource(data.getIamge());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != 3) {
                        NormalIntentUtils.go2WebActivity(
                                v.getContext(),
                                data.getUrl() + "?stock_code=" + stockCode + "&stock_name=" + stockName,
                                data.getText(), true);
                    }

                    StockPopuDialog.this.dismiss();
                }
            });
        }
    }
}
