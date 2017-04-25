package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;

/**
 * author wangjd on 2017/3/1 0001.
 * Staff_id 1375
 * phone 18930640263
 *
 * 底部弹出式 列表dialog
 */
public class BottomSheetListDialog extends BaseBottomDialog {

    private ArrayList<String> itemTexts;

    private DialogItemClick dialogItemClick;

    public static BottomSheetListDialog getInstance(ArrayList<String> itemTexts) {
        BottomSheetListDialog dialog = new BottomSheetListDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("itemTexts", itemTexts);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_bottom_normal_list;
    }

    @Override
    public void bindView(final View v) {
        itemTexts=getArguments().getStringArrayList("itemTexts");
        TextView tvCancle= (TextView) v.findViewById(R.id.tv_dialog_cancle);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetListDialog.this.dismiss();
            }
        });

        ListView listView= (ListView) v.findViewById(R.id.lsv_dialog);

        listView.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.item_dialog_text_simple_1,itemTexts));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dialogItemClick!=null){
                    dialogItemClick.onItemClick(BottomSheetListDialog.this, (TextView) view,position);
                }
            }
        });
    }

    public void setOnDialogItemClickListener(DialogItemClick dialogItemClick) {
        this.dialogItemClick = dialogItemClick;
    }

    public interface DialogItemClick{
        void onItemClick(BottomSheetListDialog dialog, TextView view, int position);
    }

}
