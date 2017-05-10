package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BaseIconText<T,E> {
    private E text;
    private T iamge;

    private String url;

    public BaseIconText(E text) {
        this.text = text;
    }

    public BaseIconText(T iamge,E text) {
        this.text = text;
        this.iamge = iamge;
    }

    public BaseIconText(T iamge,E text, String url) {
        this.text = text;
        this.iamge = iamge;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public E getText() {
        return text;
    }

    public void setText(E text) {
        this.text = text;
    }

    public T getIamge() {
        return iamge;
    }

    public void setIamge(T iamge) {
        this.iamge = iamge;
    }
}
