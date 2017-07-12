package com.example.dell.bzbp_frame;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPostoActivity extends ListActivity{

    public static String ip = "192.168.1.97:8080/BookStore";

    private ListView listview;
    private ArrayList<Posto> resultlist = new ArrayList<Posto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Bundle bundle = this.getIntent().getExtras();

        Posto temp = new Posto();


        //temp.setLatitude((Double) bundle.getDouble("latitude"));
        //temp.setLongitude((Double) bundle.getDouble("longitude"));

        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getPostosBy");
        myThread1.setPosto(temp);
        myThread1.setWhat(2);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String a = "/storage/emulated/0/temp/1111.jpg";
        resultlist = myThread1.getPostos();
        //Bitmap bit;
        //String picture = resultlist.get(0).getImage();
        //byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        //bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);



       /* FileOutputStream foutput = null;
        try {

            File fileImg = new File(a);
            if (fileImg.exists()) {
                fileImg.delete();
            }

            foutput = new FileOutputStream(fileImg);
            bit.compress(Bitmap.CompressFormat.PNG, 100, foutput); // 压缩图片


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmap = BitmapFactory.decodeFile(a);*/
        //test_image = (ImageView) findViewById(R.id.test_search);
        //test_image.setImageBitmap(bit);
    //}


/*
        Posto temp = new Posto();


        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/temp/1499672062863.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] appicon =  baos.toByteArray();// 转为byte数组
        temp.setImage(Base64.encodeToString(appicon, 0, appicon.length,Base64.NO_WRAP));
        temp.setName("name");
        temp.setComment("comment");
        temp.setUsername("username");
        temp.setDate(123L);
        resultlist.add(temp);*/

            SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.postolist,
                    new String[]{"postolist_image",
                            "postolist_name","postolist_comment",
                            "postolist_username","postolist_date"},
                    new int[]{
                            R.id.postolist_image,
                            R.id.postolist_name,
                            R.id.postolist_comment,
                            R.id.postolist_username,
                            R.id.postolist_date
                            });

            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;

                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
        setListAdapter(adapter);
        }



    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            Bitmap bit;
            String picture=resultlist.get(i).getImage();
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            bit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            map.put("postolist_image",bit);
            map.put("postolist_name", resultlist.get(i).getName());
            map.put("postolist_comment", resultlist.get(i).getComment());
            map.put("postolist_username", resultlist.get(i).getUsername());
            map.put("postolist_date", resultlist.get(i).getDate());
            list.add(map);
        }


        return list;
    }

}
