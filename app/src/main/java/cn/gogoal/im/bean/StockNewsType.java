package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author wangjd on 2017/5/3 0003.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :== 个股中新闻、公告、研报类型实体 ==
 */
public class StockNewsType implements Parcelable {

    public static final int STOCK_INFOMATION_SOURCE_NEWS = 7;//新闻

    public static final int STOCK_INFOMATION_SOURCE_NOTICES = 3;//公告

    public static final int STOCK_INFOMATION_SOURCE_VIEWPOINTS = 9;//看点

    private int source;       /*7(新闻)、3(公告)、9(看点，去掉)*/
    private String title;        /*标题*/
    private int newsSource;     /*个股研报(102)、个股新闻(100)、个股公告(105)*/

    public StockNewsType(int source, String title, int newsSource) {
        this.source = source;
        this.title = title;
        this.newsSource = newsSource;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(int newsSource) {
        this.newsSource = newsSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.source);
        dest.writeString(this.title);
        dest.writeInt(this.newsSource);
    }

    public StockNewsType() {
    }

    protected StockNewsType(Parcel in) {
        this.source = in.readInt();
        this.title = in.readString();
        this.newsSource = in.readInt();
    }

    public static final Creator<StockNewsType> CREATOR = new Creator<StockNewsType>() {
        @Override
        public StockNewsType createFromParcel(Parcel source) {
            return new StockNewsType(source);
        }

        @Override
        public StockNewsType[] newArray(int size) {
            return new StockNewsType[size];
        }
    };
}
