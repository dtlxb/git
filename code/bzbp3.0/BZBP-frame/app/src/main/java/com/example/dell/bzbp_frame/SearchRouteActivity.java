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
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.tool.MyThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchRouteActivity extends ListActivity {
    public static String ip = "192.168.1.97:8080/BookStore";

    private ListView listview;
    private ArrayList<Route> resultlist = new ArrayList<Route>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = this.getIntent().getExtras();

        Posto temp = new Posto();


        //temp.setLatitude((Double) bundle.getDouble("latitude"));
        //temp.setLongitude((Double) bundle.getDouble("longitude"));

        MyThread myThread1 = new MyThread();
        myThread1.setGetUrl("http://" + ip + "/rest/getRoutesBy");
        myThread1.setPosto(temp);
        myThread1.setWhat(4);
        myThread1.start();
        try {
            myThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resultlist = myThread1.getRoutes();

        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.routelist,
                new String[]{
                        "routelist_name","routelist_comment",
                        "routelist_username"},
                new int[]{
                        R.id.routelist_name,
                        R.id.routelist_comment,
                        R.id.routelist_username,
                });
        setListAdapter(adapter);

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<resultlist.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("routelist_name", resultlist.get(i).getName());
            map.put("routelist_comment", resultlist.get(i).getComment());
            map.put("routelist_username", resultlist.get(i).getUsername());
            list.add(map);
        }


        return list;
    }

}
