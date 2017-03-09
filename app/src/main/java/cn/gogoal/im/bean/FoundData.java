package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/2/17 0017.
 * Staff_id 1375
 * phone 18930640263
 */
public class FoundData {

    private List<ItemPojos> itemPojos;

    private String title;

    public FoundData(List<ItemPojos> itemPojos, String title) {
        this.itemPojos = itemPojos;
        this.title = title;
    }

    public List<ItemPojos> getItemPojos() {
        return itemPojos;
    }

    public void setItemPojos(List<ItemPojos> itemPojos) {
        this.itemPojos = itemPojos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class ItemPojos {
        private String itemTextDescription;

        private String iconUrl;

        private int iconRes;

        private String url;

        public ItemPojos(String itemTextDescription, int iconRes, String url) {
            this.itemTextDescription = itemTextDescription;
            this.iconRes = iconRes;
            this.url = url;
        }

        public String getItemTextDescription() {
            return itemTextDescription;
        }

        public void setItemTextDescription(String itemTextDescription) {
            this.itemTextDescription = itemTextDescription;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getIconRes() {
            return iconRes;
        }

        public void setIconRes(int iconRes) {
            this.iconRes = iconRes;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}