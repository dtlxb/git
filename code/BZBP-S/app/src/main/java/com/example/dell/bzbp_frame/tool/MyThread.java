package com.example.dell.bzbp_frame.tool;

/**
 * Created by dell on 2017/7/6.
 */


import com.example.dell.bzbp_frame.model.Comment;
import com.example.dell.bzbp_frame.model.MyLatlng;
import com.example.dell.bzbp_frame.model.Posto;
import com.example.dell.bzbp_frame.model.Praise;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by huzeyu on 2017/7/6.
 */

public class MyThread extends Thread {
    private int what ;      //选择传输的方法
    private   String getUrl;
    private ArrayList<Posto> Postos ;
    private  Praise praise;

    public Praise getPraise() {
        return praise;
    }

    public void setPraise(Praise praise) {
        this.praise = praise;
    }




    private ArrayList<Route> Routes ;

    private ArrayList<User> Users;

    private ArrayList<Comment> Comments;
    private Route route;


    Comment comment;
    private Integer rid ;//result id
    private User user;
    private Posto posto;
    private String result;

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<User> users) {
        Users = users;
    }
    public ArrayList<Comment> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        Comments = comments;
    }
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
    public Integer getRid() {
        return rid;
    }
    public ArrayList<Route> getRoutes() {
        return Routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        Routes = routes;
    }
    public void setRid(int rid) {
        this.rid = rid;
    }
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public ArrayList<Posto> getPostos() {
        return Postos;
    }

    public int getWhatt() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }


    public String getResult() {
        return result;
    }

    public Posto getPosto() {
        return posto;
    }

    public void setPosto(Posto posto) {
        this.posto = posto;
    }


    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void run() {
        if(what==0){
            result= sendHttpPost_posto(getUrl,posto);
            rid = Integer.parseInt(result);

        }
        if(what==1){
            result= sendHttpPost (getUrl, user);
        }
        if(what==2){
            Postos= getHttpPost_posto (getUrl,posto);
        }
        if(what==3){
           result= sendRoute (getUrl,route);
            rid = Integer.parseInt(result);
            LogUtil.d("1",rid+"rid");
    }

        if(what==4){
            Routes= getHttpPost_route (getUrl,posto);


        }
        if(what==5){
            result= sendComment (getUrl, comment);
        }
        if(what==6){
            Comments= getComments (getUrl, posto);
        }

        if(what==7){
            result= sendPraise (getUrl, praise);
        }
        if(what==8){

            result= getPraiseNumber(getUrl, posto);
            rid = Integer.parseInt(result);
        }
        if(what==9){

           Users= getUsersBy(getUrl, posto);

        }

    }

    public String sendHttpPost(String getUrl, User user) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(user);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                    LogUtil.d("1",str+"1");
                }

                in.close();
                br.close();
                return str;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }
    public String sendHttpPost_posto(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;
        URL url = null;

        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                    LogUtil.d("1",str+"1");
                }

                in.close();
                br.close();
                return str;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }
    public ArrayList<Posto> getHttpPost_posto(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        try {

            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                LogUtil.d("1","122222");
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                }

                gson = new Gson();
                String tmp=buffer.toString();
					/*Type listType = new TypeToken<ArrayList<Book> >(){}.getType();
					books=gson.fromJson(tmp,listType);*/



                Postos=new ArrayList<Posto>();
                JSONArray mres=new JSONArray(tmp);


                for (int i = 0; i < mres.length(); i++) {
                    JSONObject thisposto = (JSONObject)mres.getJSONObject(i);
                    LogUtil.d("1","1");
                    Posto postotmp=new Posto();
                    postotmp.setName(thisposto.getString("name"));
                    postotmp.setUsername(thisposto.getString("username"));
                    postotmp.setComment(thisposto.getString("comment"));




                    postotmp.setLatitude(thisposto.getDouble("latitude"));
                    postotmp.setLongitude(thisposto.getDouble("longitude"));
                    postotmp.setPid(Integer.parseInt(thisposto.getString("pid")));
                    postotmp.setImage(thisposto.getString("image"));


                     postotmp.setDate(thisposto.getLong("date"));




                    Postos.add(postotmp);
                }


                in.close();
                br.close();
                return  Postos;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }

    public String sendRoute(String getUrl,Route route) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(route);
            LogUtil.d("1",jsonstr);
            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                    LogUtil.d("1",str+"1");
                }

                in.close();
                br.close();
                return str;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }


    public ArrayList<Route> getHttpPost_route(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        try {

            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                LogUtil.d("1","122222");
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                }

                gson = new Gson();
                String tmp=buffer.toString();
					/*Type listType = new TypeToken<ArrayList<Book> >(){}.getType();
					books=gson.fromJson(tmp,listType);*/



                Routes=new ArrayList<Route>();
                JSONArray mres=new JSONArray(tmp);


                for (int i = 0; i < mres.length(); i++) {
                    JSONObject thisroute = (JSONObject)mres.getJSONObject(i);
                    LogUtil.d("1","1");
                    Route routetmp=new Route();
                    routetmp.setUsername(thisroute.getString("username"));
                    routetmp.setComment(thisroute.getString("comment"));
                    routetmp.setName(thisroute.getString("name"));
                    routetmp.setStart_time(thisroute.getLong("start_time"));
                    routetmp.setStart_time(thisroute.getLong("end_time"));
                    routetmp.setRid(thisroute.getInt("rid"));
                    JSONArray location_list= thisroute.getJSONArray("location_list");
                    ArrayList<MyLatlng>  locations= new  ArrayList<MyLatlng>();
                    for (int j = 0; j < location_list.length(); j++) {
                        MyLatlng Latlngtem = new MyLatlng(0.0,0.0);
                        JSONObject thisLatlng = (JSONObject)location_list.getJSONObject(j);
                        Latlngtem.setLatitude(thisLatlng.getDouble("latitude"));
                        Latlngtem.setLongitude(thisLatlng.getDouble("longitude"));
                        locations.add(Latlngtem);
                    }
                    routetmp.setLocation_list(locations);

                    JSONArray pids= thisroute.getJSONArray("pids");
                    ArrayList<Integer> Pids= new ArrayList<Integer>();
                    for (int j = 0; j < pids.length(); j++) {

                        int thisPid = pids.getInt(j);

                        Pids.add(thisPid);
                    }
                    routetmp.setPids(Pids);
                   Routes.add(routetmp);
                }


                in.close();
                br.close();
                int test = Routes.size();
                return  Routes;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }


    public String sendComment(String getUrl, Comment comment) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(comment);
            LogUtil.d("1",jsonstr);
            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                    LogUtil.d("1",str+"1");
                }

                in.close();
                br.close();
                return str;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }

    public ArrayList<Comment> getComments(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        try {

            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                LogUtil.d("1","122222");
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                }

                gson = new Gson();
                String tmp=buffer.toString();
					/*Type listType = new TypeToken<ArrayList<Book> >(){}.getType();
					books=gson.fromJson(tmp,listType);*/



                Comments=new ArrayList<Comment>();
                JSONArray mres=new JSONArray(tmp);


                for (int i = 0; i < mres.length(); i++) {
                    JSONObject thisComment = (JSONObject)mres.getJSONObject(i);
                    LogUtil.d("1","1");
                    Comment Commenttmp=new Comment();
                    Commenttmp.setContext(thisComment.getString("context"));
                    Commenttmp.setUsername(thisComment.getString("username"));
                    Commenttmp.setDate(thisComment.getLong("date"));


                    Comments.add(Commenttmp);
                }


                in.close();
                br.close();
                return  Comments;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }


    public String sendPraise(String getUrl, Praise praise) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(praise);
            LogUtil.d("1",jsonstr);
            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功

                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                    LogUtil.d("1",str+"1");
                }

                in.close();
                br.close();
                return str;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }


    public String getPraiseNumber(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        try {

            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                LogUtil.d("1","122222");
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                }

                gson = new Gson();
                String tmp=buffer.toString();
					/*Type listType = new TypeToken<ArrayList<Book> >(){}.getType();
					books=gson.fromJson(tmp,listType);*/






                in.close();
                br.close();
                return  tmp;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }


    public ArrayList<User> getUsersBy(String getUrl, Posto posto) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        try {

            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            Gson gson = new Gson();
            String jsonstr = gson.toJson(posto);

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                LogUtil.d("1","122222");
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                if ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);


                }

                gson = new Gson();
                String tmp=buffer.toString();
					/*Type listType = new TypeToken<ArrayList<Book> >(){}.getType();
					books=gson.fromJson(tmp,listType);*/



                Users=new ArrayList<User>();
                JSONArray mres=new JSONArray(tmp);


                for (int i = 0; i < mres.length(); i++) {
                    JSONObject thisuser = (JSONObject)mres.getJSONObject(i);
                    LogUtil.d("1","1");
                    User usertmp=new User();

                    usertmp.setUsername(thisuser.getString("username"));
                    usertmp.setId(thisuser.getInt("id"));
                    Users.add(usertmp);
                }


                in.close();
                br.close();
                return  Users;
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
        return null;
    }

}