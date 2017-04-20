package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.MultiItemEntity;

/**
 * author wangjd on 2017/4/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MineItem implements MultiItemEntity {

    public static final int TYPE_HEAD = 1;

    public static final int TYPE_SPACE = 2;

    public static final int TYPE_ICON_TEXT_ITEM = 3;

    private int itemType;

    private int iconRes;

    private String itemText;

    public int getIconRes() {
        return iconRes;
    }

    public String getItemText() {
        return itemText;
    }

    public MineItem(int itemType, int iconRes, String itemText) {
        this.itemType = itemType;
        this.iconRes = iconRes;
        this.itemText = itemText;
    }


    public MineItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
