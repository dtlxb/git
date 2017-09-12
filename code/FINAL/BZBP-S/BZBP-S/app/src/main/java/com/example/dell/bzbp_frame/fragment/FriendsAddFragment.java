package com.example.dell.bzbp_frame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.bzbp_frame.R;
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.User;
import com.example.dell.bzbp_frame.tool.MyThread;

public class FriendsAddFragment extends Fragment {
    private Context mContext;
    private View view;
    private Bundle bundle;
    private User user;
    public static String ip;

    private String friendsadd_choice;

    private EditText edit_text_friendsadd;
    private Spinner spinner_friendsadd;
    private Button button_friendsadd;
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

         initData();

         initView(inflater,container);

         initListener();

         return view;
     }

     private void initData(){
         mContext = this.getActivity();
         Bundle bundle = getArguments();
         user = (User)bundle.getSerializable("user");
         ip = this.getString(R.string.ipv4);
     }

     private void initView(LayoutInflater inflater, ViewGroup container){
         view = inflater.inflate(R.layout.fragment_friends_add, container, false);
         edit_text_friendsadd = (EditText)view.findViewById(R.id.edit_text_friendsadd);
         spinner_friendsadd = (Spinner)view.findViewById(R.id.spinner_friendsadd);
         button_friendsadd = (Button)view.findViewById(R.id.button_friendsadd);
     }

     private void initListener(){
         spinner_friendsadd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view,
                                        int pos, long id) {

                 String[] friendsadd = getResources().getStringArray(R.array.addfriend);
                 friendsadd_choice = friendsadd[pos];
             }
             @Override
             public void onNothingSelected(AdapterView<?> parent) {
                 friendsadd_choice = "find by ID";
             }
         });

         button_friendsadd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //-----------------------------------------------

                 if(edit_text_friendsadd.getText().toString().equals("")) {
                     Toast.makeText(mContext,"输入不能为空", Toast.LENGTH_LONG).show();
                     return;
                 }
                 Posto temp = new Posto();//pid为自己，rid为输入的目标id,name为输入的username
                 temp.setPid(user.getId());
                 MyThread myThread1 = new MyThread();

                 if(friendsadd_choice.equals("find by ID")){
                     try {
                         String input = edit_text_friendsadd.getText().toString();
                         int id = Integer.valueOf(input);//把字符串强制转换为数字
                         temp.setBelong_rid(id);
                         myThread1.setGetUrl("http://"+ip+"/rest/applyFriend");
                     } catch (Exception e) {
                         Toast.makeText(mContext,"请输入整数ID", Toast.LENGTH_LONG).show();
                         return;
                     }
                 }else{
                     temp.setName(edit_text_friendsadd.getText().toString());
                     myThread1.setGetUrl("http://"+ip+"/rest/applyFriendByName");
                 }

                 myThread1.setWhat(0);
                 myThread1.setPosto(temp);
                 myThread1.start();

                 try {
                     myThread1.join();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 String apply_result = myThread1.getResult();
                 int apply_result_int = Integer.parseInt(apply_result);
                 if(apply_result_int<0){
                     Toast.makeText(mContext,"不合法的对象", Toast.LENGTH_LONG).show();
                 }else{
                     Toast.makeText(mContext,"申请已提交", Toast.LENGTH_LONG).show();
                 }
             }
         });
     }
}
