package cn.gogoal.im.bean.stock;

import cn.gogoal.im.adapter.baseAdapter.entity.SectionEntity;

/**
 * Author wangjd on 2017/7/2 0002.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==事件选股 列表 item==
 */
public class SectionEventStockData extends SectionEntity<Stock> {

    private EventTitle eventTitle;

    public EventTitle getEventTitle() {
        return eventTitle;
    }

    public SectionEventStockData(boolean isHeader, EventTitle header) {
        super(isHeader, header.getTitle());
        eventTitle=header;
    }

    public SectionEventStockData(Stock stock) {
        super(stock);
    }

    public static class EventTitle {
        private int id;
        private String title;
        private String date;
        private int titlePosition;
        private String desc;

        public EventTitle(int titlePosition, String title, String date, String desc) {
            this.title = title;
            this.date = date;
            this.desc = desc;
            this.titlePosition = titlePosition;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getTitlePosition() {
            return titlePosition;
        }

        public void setTitlePosition(int titlePosition) {
            this.titlePosition = titlePosition;
        }
    }
}
