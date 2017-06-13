package cn.gogoal.im.common;

/**
 * author wangjd on 2017/6/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public interface Impl<T> {

    int RESPON_DATA_EMPTY = 1001;//无数据

    int RESPON_DATA_SUCCESS = 0;//成功

    int RESPON_DATA_ERROR = 500;//出错

    void response(int code, T data);
}
