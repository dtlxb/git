package cn.gogoal.im.common.database;

/**
 * author wangjd on 2017/3/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description : 数据库操作工具类封装
 */
public class DataBaseUtils {
    private static final DataBaseUtils ourInstance = new DataBaseUtils();

    public static DataBaseUtils getInstance() {
        return ourInstance;
    }

    private DataBaseUtils() {
    }
}
