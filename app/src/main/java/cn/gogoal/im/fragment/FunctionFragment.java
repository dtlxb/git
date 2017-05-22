package cn.gogoal.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.adapter.ChatFunctionAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by huangxx on 2017/5/3.
 */

public class FunctionFragment extends Fragment {

    public static final int GET_STOCK = 1113;
    private RecyclerView recyclerView;
    private ChatFunctionAdapter chatFunctionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.functions_recycler);

        //多功能消息框
        List<FoundData.ItemPojos> itemPojosList = new ArrayList<>();
        itemPojosList.add(new FoundData.ItemPojos("照片", R.mipmap.chat_add_photo, "tag"));
        itemPojosList.add(new FoundData.ItemPojos("股票", R.mipmap.chat_add_stock, "tag"));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        chatFunctionAdapter = new ChatFunctionAdapter(getContext(), itemPojosList);
        recyclerView.setAdapter(chatFunctionAdapter);

        //多功能消息发送
        chatFunctionAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        //发照片
                        ImageTakeUtils.getInstance().takePhoto(getContext(), 9, false, new ITakePhoto() {
                            @Override
                            public void success(List<String> uriPaths, boolean isOriginalPic) {
                                if (uriPaths != null) {
                            /*//返回的图片集合不为空，执行上传操作
                            if (isOriginalPic) {
                                //批量发送至公司后台
                                doUpload(uriPaths);
                            } else {
                            }*/
                                    //压缩后上传
                                    compressPhoto(uriPaths);
                                }
                            }

                            @Override
                            public void error() {

                            }
                        });
                        break;
                    case 1:
                        //跳转股票页面发送股票
                        Intent intent = new Intent(getActivity(), StockSearchActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("num", 2);
                        bundle.putBoolean("show_add_btn", false);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, GET_STOCK);
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

//    private void doUpload(final List<String> uriPaths) {
//        for (int i = 0; i < uriPaths.size(); i++) {
//            sendImageToZyyx(new File(uriPaths.get(i)));
//        }
//    }

    private void compressPhoto(final List<String> uriPaths) {
        KLog.e(uriPaths);
        for (int i = 0; i < uriPaths.size(); i++) {
            Luban.get(getActivity())
                    .load(new File(uriPaths.get(i)))                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)                           //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() {      //设置回调

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(final File file) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photoFile", file);
                            BaseMessage baseMessage = new BaseMessage("photoInfo", map);
                            AppManager.getInstance().sendMessage("onePhoto", baseMessage);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    }).launch();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == GET_STOCK) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("stock_name", data.getStringExtra("stock_name"));
                map.put("stock_code", data.getStringExtra("stock_code"));
                BaseMessage baseMessage = new BaseMessage("stockInfo", map);
                AppManager.getInstance().sendMessage("oneStock", baseMessage);
            }
        }
    }
}
