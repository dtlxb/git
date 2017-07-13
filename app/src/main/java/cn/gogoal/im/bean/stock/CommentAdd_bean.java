package cn.gogoal.im.bean.stock;

/**
 * Created by daiwei on 2015/10/19.
 */
public class CommentAdd_bean {

    private String code;
    private CommentAdd_data data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CommentAdd_data getData() {
        return data;
    }

    public void setData(CommentAdd_data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommentAdd_bean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
