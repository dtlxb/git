package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by huangxx on 2017/6/21.
 */

public class MoneyBean {

    private List<FiveDayMessageBean> fiveDayMessage;
    private TodayInfoBean todayInfo;

    public List<FiveDayMessageBean> getFiveDayMessage() {
        return fiveDayMessage;
    }

    public void setFiveDayMessage(List<FiveDayMessageBean> fiveDayMessage) {
        this.fiveDayMessage = fiveDayMessage;
    }

    public TodayInfoBean getTodayInfo() {
        return todayInfo;
    }

    public void setTodayInfo(TodayInfoBean todayInfo) {
        this.todayInfo = todayInfo;
    }

    @Override
    public String toString() {
        return "MoneyBean{" +
                "fiveDayMessage=" + fiveDayMessage +
                ", todayInfo=" + todayInfo +
                '}';
    }
}
