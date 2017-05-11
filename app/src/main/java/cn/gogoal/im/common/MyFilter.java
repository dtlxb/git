package cn.gogoal.im.common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * author wangjd on 2017/5/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${文件类型过滤}.
 */
public class MyFilter implements FilenameFilter {
    private String type;

    public MyFilter(String type) {
        this.type = type;
    }

    public boolean accept(File dir, String name) {
        return name.endsWith(type);
    }
}