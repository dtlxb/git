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

    public BaseIconText(E text) {
        this.text = text;
    }

    public BaseIconText(E text, T iamge) {
        this.text = text;
        this.iamge = iamge;
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
