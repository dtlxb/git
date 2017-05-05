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
    private int newsType;       /*7(新闻)、3(公告)、9(看点，去掉)*/
    private String title;        /*标题*/
    private int newsSource;     /*个股研报(102)、个股新闻(100)、个股公告(105)*/

    public StockNewsType(int newsType, String title, int newsSource) {
        this.newsType = newsType;
        this.title = title;
        this.newsSource = newsSource;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
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
        dest.writeInt(this.newsType);
        dest.writeString(this.title);
        dest.writeInt(this.newsSource);
    }

    public StockNewsType() {
    }

    protected StockNewsType(Parcel in) {
        this.newsType = in.readInt();
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
