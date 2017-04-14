package cn.gogoal.im.adapter.copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import hply.com.niugu.bean.CommentData;
import hply.com.niugu.bean.CommentData_Sons;
import hply.com.niugu.view.InnerListView;


/**
 * Created by daiwei on 2015/10/14.
 */
public class CommentListAdapter extends MyBaseAdapter<CommentData> {

  private ArrayList<CommentData> list;
  private CommentDataSonsAdapter adapter;

  public CommentListAdapter(ArrayList<CommentData> list) {
    super(list);
    this.list = list;
  }

  @Override
  public View getView(final int position, View convertView, final ViewGroup parent) {
    final HoldView holdView;
    if (convertView == null) {
      holdView = new HoldView();
      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comments_list_item, parent,false);
      holdView.comment_head_iv = (ImageView) convertView.findViewById(R.id.comment_head_iv);
      holdView.comment_useName = (TextView) convertView.findViewById(R.id.comment_useName);
      holdView.comment_number = (TextView) convertView.findViewById(R.id.comment_number);
      holdView.comment_thumb_up = (RelativeLayout) convertView.findViewById(R.id.comment_thumb_up);
      holdView.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
      holdView.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
      holdView.comment_text = (TextView) convertView.findViewById(R.id.comment_text);
      holdView.comment_lv = (InnerListView) convertView.findViewById(R.id.comment_lv);
      convertView.setTag(holdView);
    } else {
      holdView = (HoldView) convertView.getTag();
      resetHolder(holdView);
    }
    final CommentData on_data = list.get(position);
    //加载头像
    ImageDisplay.loadCircleNetImage(parent.getContext(),on_data.getAvoter(), holdView.comment_head_iv);

    holdView.comment_useName.setText(on_data.getUsername());
    holdView.comment_number.setText(on_data.getPraise_sum() + "");
    holdView.comment_thumb_up.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int num = Integer.parseInt(holdView.comment_number.getText().toString());
        num++;
        holdView.comment_number.setText(String.valueOf(num));
        AppManager.getInstance().sendMessage("CommentActivity", num+","+position);
        Map<String, String> params = new HashMap<String, String>();
        params.put("target_id", on_data.getId());
        params.put("type", "20");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
          @Override
          public void onSuccess(String responseInfo) {
          }

          @Override
          public void onFailure(String msg) {
            UIHelper.toast(parent.getContext(), "点赞失败!请检查网络设置后重试");
          }
        };
        new GGOKHTTP(params, GGOKHTTP.PRAISE_ADD, ggHttpInterface).startGet();
      }
    });
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String date = on_data.getDate();
      holdView.comment_time.setText(CalendarUtils.formatFriendly(dateFormat.parse(date)));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    final String content = on_data.getContent();
    if (content.length() <= 80) {
      holdView.comment_content.setText(content);
      holdView.comment_text.setVisibility(View.GONE);
    } else {
      holdView.comment_content.setText(content.substring(0, 80));
      holdView.comment_text.setVisibility(View.VISIBLE);
      holdView.comment_text.setText("显示全部");
    }
    holdView.comment_text.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (holdView.comment_text.getText().equals("显示全部")) {
          holdView.comment_text.setText("收起");
          holdView.comment_content.setText(content);
        } else if (holdView.comment_text.getText().equals("收起")) {
          holdView.comment_text.setText("显示全部");
          holdView.comment_content.setText(content.substring(0, 80));
        }
      }
    });
    if (on_data.getSons() != null) {
      ArrayList<CommentData_Sons> sons = on_data.getSons();
      adapter = new CommentDataSonsAdapter(sons);
      holdView.comment_lv.setAdapter(adapter);
      adapter.notifyDataSetChanged();
    }
    return convertView;
  }

  private void resetHolder(HoldView holdView){
    ArrayList<CommentData_Sons> sons = new ArrayList<>();
    adapter = new CommentDataSonsAdapter(sons);
    holdView.comment_lv.setAdapter(adapter);
    adapter.notifyDataSetChanged();
  }

  class HoldView {
    ImageView comment_head_iv;
    TextView comment_useName;
    TextView comment_number;
    RelativeLayout comment_thumb_up;
    TextView comment_time;
    TextView comment_content;
    TextView comment_text;
    InnerListView comment_lv;
  }
}
