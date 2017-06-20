package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 个股底部 弹窗，文字一分钟，数据一分钟，同业比较，投资者互动
 */
public class StockPopuDialog extends BaseBottomDialog {

    public static StockPopuDialog newInstance(Stock stockInfo) {
        StockPopuDialog dialog = new StockPopuDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("stock_info", stockInfo);
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

        Stock stock = getArguments().getParcelable("stock_info");

        RecyclerView rvShareList = (RecyclerView) v.findViewById(R.id.rv_share_list);
        TextView tvShareCancle = (TextView) v.findViewById(R.id.tv_dialog_cancle);

        rvShareList.setLayoutManager
                (new GridLayoutManager(
                        v.getContext(), 3));

        rvShareList.setAdapter(new BottomSheetAdapter(getDatas(), stock));

        tvShareCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockPopuDialog.this.dismiss();
            }
        });

    }

    private List<BaseIconText<Integer, String>> getDatas() {
        String[] desc = {"文字一分钟", "数据一分钟", "同业比较"};
        int[] images = {R.mipmap.img_stock_words_minute, R.mipmap.img_stock_data_minute,
                R.mipmap.img_stock_profession_compare};

        String[] urls = {AppConst.GG_TEXT1MINUTE, AppConst.GG_DATA1MINUTE, AppConst.GG_TONG_YE, null};

        List<BaseIconText<Integer, String>> datas = new ArrayList<>();
        for (int i = 0; i < desc.length; i++) {
            datas.add(new BaseIconText<>(images[i], desc[i], urls[i]));
        }
        return datas;
    }

    private class BottomSheetAdapter extends CommonAdapter<BaseIconText<Integer, String>, BaseViewHolder> {

        private Stock stock;

        private String[] codesParams = {"G3_003_05", "G3_003_04", "G3_003_08"};

        private BottomSheetAdapter(List<BaseIconText<Integer, String>> data, Stock stock) {
            super(R.layout.item_dialog_bottom_sheet, data);
            this.stock = stock;
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


            final HashMap<String, String> args = new HashMap<>();
            args.put("stock_code", stock.getStock_code());
            args.put("stock_name", stock.getStock_name());
            args.put("code", codesParams[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(
                            v.getContext(),
                            data.getUrl() + "?" + StringUtils.map2ggParameter(args),
                            data.getText(), true);

                    StockPopuDialog.this.dismiss();
                }
            });
        }
    }
}
