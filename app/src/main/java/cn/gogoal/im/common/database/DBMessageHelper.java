package cn.gogoal.im.common.database;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import java.util.Date;
import java.util.List;

/**
 * author wangjd on 2017/3/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class DBMessageHelper {

    private volatile static DBMessageHelper instance;

    private DBMessageHelper() {
    }

    public static DBMessageHelper getInstance() {
        if (instance == null) {
            synchronized (DBMessageHelper.class) {
                if (instance == null) {
                    instance = new DBMessageHelper();
                }
            }
        }
        return instance;
    }

    private static final String TABLENAME = "Project";
    //新建一条记录，数据库表名为Project
    public static void createProject(String name) {
        AVUser user=getCurrentUser();
        final AVObject object = new AVObject(TABLENAME);
        object.put("name", name);
        object.put("createdate", new Date().getTime());
        object.put("createaccount", user.getUsername());
        object.put("createname", user.getString("name"));
        object.put("membernum", 0);
        object.put("tasknum", 0);
        object.put("state", 1);
        SaveCallback callback = new SaveCallback() {
            @Override
            public void done(AVException e) {
                postInfo(object, e);
            }
        };
        saveProject(object, callback);
    }

    private static void saveProject(AVObject object, SaveCallback callback) {

    }

    private static void postInfo(AVObject object, AVException e) {

    }

    /***
     * 根据id查询记录
     */
    private static void getProject(String id, GetCallback callback) {
        AVQuery<AVObject> query = new AVQuery<AVObject>(TABLENAME);
        query.getInBackground(id, callback);
    }

    /***
     * 得到多条记录,按照创建时间排序
     */
    public static void getAllProject() {
        AVQuery<AVObject> query = new AVQuery<>(TABLENAME);
        query.orderByDescending("createAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                postInfoList(list, e);
            }
        });
    }

    private static void postInfoList(List<AVObject> list, AVException e) {

    }

    /**
     * 修改单条记录
     * */
    public static void addTasknum(String id) {
        GetCallback getCallback = new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    avObject.put("tasknum", avObject.getInt("tasknum") + 1);
                    SaveCallback saveCallback = new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                //异常处理
                            }
                        }
                    };
                    //更新后获取最新值
                    avObject.setFetchWhenSave(true);
                    avObject.saveInBackground(saveCallback);
                } else {
                    //异常处理
                }
            }
        };
        getProject(id, getCallback);
    }

    /***
     * 创建一个用户账户
     */
    public static void createUser(final String account, final String password, String Email, String name) {
        final AVUser user = new AVUser();
        user.setUsername(account);
        user.setPassword(password);
        user.setEmail(Email);
        user.put("name", name);
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                //逻辑处理
            }
        });
    }

    /***
     * 登录
     */
    public static void Login(String account, String password) {
        AVUser.logInInBackground(account, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                //逻辑处理
            }
        });
    }

    /***
     * 得到当前用户
     *
     * @return 当前用户
     */
    public static AVUser getCurrentUser() {
        AVUser currentUser = AVUser.getCurrentUser();
        return currentUser;
    }

    public static void logOut() {
        AVUser.logOut();
    }

    /**
     * 修改密码
     */
    public static void changePwd(String old_password, String new_password) {
        AVUser user = getCurrentUser();
        if (user != null) {
            user.updatePasswordInBackground(old_password, new_password, new UpdatePasswordCallback() {
                @Override
                public void done(AVException e) {
                    //逻辑处理
                }
            });
        }
    }

}
