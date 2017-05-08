package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.MultiItemEntity;

/**
 * author wangjd on 2017/5/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ShareListBean<T> implements MultiItemEntity {

    public static final int LIST_TYPE_SEARCH = 0x0001;

    public static final int LIST_TYPE_SECTION = 0x0002;

    public static final int LIST_TYPE_ITEM = 0x0003;

    private int listType;

    private T itemImage;

    private String text;

    private IMMessageBean bean;

    public ShareListBean(int listType) {
        this.listType = listType;
    }

    public ShareListBean(int listType, T itemImage, String text) {
        this.listType = listType;
        this.itemImage = itemImage;
        this.text = text;
    }

    public ShareListBean(int listType, T itemImage, String text, IMMessageBean bean) {
        this.listType = listType;
        this.itemImage = itemImage;
        this.text = text;
        this.bean = bean;
    }

    public IMMessageBean getBean() {
        return bean;
    }

    public void setBean(IMMessageBean bean) {
        this.bean = bean;
    }

    public T getItemImage() {
        return itemImage;
    }

    public void setItemImage(T itemImage) {
        this.itemImage = itemImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getItemType() {
        return listType;
    }
}
