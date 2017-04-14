package cn.gogoal.im.common;

import java.util.Set;

/**
 * author wangjd on 2017/4/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :选中接口
 */
public interface ICheckItemListener<T> {
    void checked(Set<T> datas, T data, boolean isAdd);
}
