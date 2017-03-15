package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ImageTextBean<T> {

    private T iamge;
    private String text;

    public ImageTextBean(T iamge, String text) {
        this.iamge = iamge;
        this.text = text;
    }

    public T getIamge() {
        return iamge;
    }

    public void setIamge(T iamge) {
        this.iamge = iamge;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
