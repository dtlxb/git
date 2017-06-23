package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * author wangjd on 2017/5/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :投研小工具data
 */
public class ToolData {
    /**
     * datas : [{"id":1,"title_id":1,"desc":"研报","iconUrl":"http://file.go-goal.cn/ggimages/aicon/5df50c7c.png","introduce":"测试数据","url":"www.baidu.com","position":1,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-04-28 12:03:59","content":"测试数据","code":"G3_001","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":8,"title_id":1,"desc":"数据","iconUrl":"http://file.go-goal.cn/ggimages/aicon/a67be642.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":2,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 11:54:44","code":"G3_002","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":10,"title_id":1,"desc":"诊股","iconUrl":"http://file.go-goal.cn/ggimages/aicon/9ce03b24.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":3,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:11:11","code":"G3_003","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":12,"title_id":1,"desc":"自选","iconUrl":"http://file.go-goal.cn/ggimages/aicon/720e1e9b.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":4,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:15:13","code":"G3_004","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":9,"title_id":1,"desc":"个股","iconUrl":"http://file.go-goal.cn/ggimages/aicon/1ff89061.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":5,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 12:01:41","code":"G3_005","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":11,"title_id":1,"desc":"行情","iconUrl":"http://file.go-goal.cn/ggimages/aicon/c1fdc5a9.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":6,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:12:49","code":"G3_006","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":13,"title_id":1,"desc":"业绩","iconUrl":"http://file.go-goal.cn/ggimages/aicon/116e1fdc.png","introduce":"测试数据","content":"测试数据","code":"G3_007","url":"www.baidu.com","position":7,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 15:15:08","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":7,"title_id":1,"desc":"主题","iconUrl":"http://file.go-goal.cn/ggimages/aicon/85daea43.png","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":8,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 11:53:50","code":"G3_008","funintroduce":"测试功能数据","title":"模块","isShow":1},{"id":5,"title_id":1,"desc":"公告中心","iconUrl":"http://file.go-goal.cn/ggimages/aicon/1f809923.png","introduce":"测试数据","url":"www.baidu.com","position":9,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-02 13:51:30","content":"测试数据","code":"G3_010","funintroduce":"测试功能数据","title":"模块","isShow":1}]
     * title : 模块
     * title_id : 1
     */

    private String title;
    private int title_id;
    private ArrayList<Tool> datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public ArrayList<Tool> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Tool> datas) {
        this.datas = datas;
    }

    public static class Tool implements Cloneable,Parcelable {
        /**
         * id : 1           item id
         * //         * title_id : 1     分组Id
         * desc : 研报
         * iconUrl : http://file.go-goal.cn/ggimages/aicon/5df50c7c.png
         * introduce : 测试数据
         * url : www.baidu.com
         * //         * position : 1
         * showHotFlag : 0
         * type : 10000         跳转类型
         * isClick : 0
         * insert_time : 2017-04-28 12:03:59
         * content : 测试数据
         * code : G3_001
         * funintroduce : 测试功能数据
         * title : 模块
         * isShow : 1
         */
        private int isExploit;
        private int isPutaway;

        private int isOpen;
        private int id;
        private int title_id;
        private String desc;
        private String iconUrl;
        private String introduce;
        private String url;
        private int position;
        private int showHotFlag;
        private int type;
        private int isClick;
        private String insert_time;
        private String content;
        private String code;
        private String funintroduce;
        private String title;
        private int isShow;

        private boolean simulatedArg;//，添加字段，是否是模拟数据

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public boolean isSimulatedArg() {
            return simulatedArg;
        }

        public void setSimulatedArg(boolean simulatedArg) {
            this.simulatedArg = simulatedArg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTitle_id() {
            return title_id;
        }

        public void setTitle_id(int title_id) {
            this.title_id = title_id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getShowHotFlag() {
            return showHotFlag;
        }

        public void setShowHotFlag(int showHotFlag) {
            this.showHotFlag = showHotFlag;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsClick() {
            return isClick;
        }

        public void setIsClick(int isClick) {
            this.isClick = isClick;
        }

        public String getInsert_time() {
            return insert_time;
        }

        public void setInsert_time(String insert_time) {
            this.insert_time = insert_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFunintroduce() {
            return funintroduce;
        }

        public void setFunintroduce(String funintroduce) {
            this.funintroduce = funintroduce;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public int getIsExploit() {
            return isExploit;
        }

        public void setIsExploit(int isExploit) {
            this.isExploit = isExploit;
        }

        public int getIsPutaway() {
            return isPutaway;
        }

        public void setIsPutaway(int isPutaway) {
            this.isPutaway = isPutaway;
        }

        @Override
        public Tool clone() {
            try {
                return (Tool) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Tool && ((Tool) obj).getId() == getId();
        }


        @Override
        public int hashCode() {
            return getId();
        }

        public Tool() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.isExploit);
            dest.writeInt(this.isPutaway);
            dest.writeInt(this.isOpen);
            dest.writeInt(this.id);
            dest.writeInt(this.title_id);
            dest.writeString(this.desc);
            dest.writeString(this.iconUrl);
            dest.writeString(this.introduce);
            dest.writeString(this.url);
            dest.writeInt(this.position);
            dest.writeInt(this.showHotFlag);
            dest.writeInt(this.type);
            dest.writeInt(this.isClick);
            dest.writeString(this.insert_time);
            dest.writeString(this.content);
            dest.writeString(this.code);
            dest.writeString(this.funintroduce);
            dest.writeString(this.title);
            dest.writeInt(this.isShow);
            dest.writeByte(this.simulatedArg ? (byte) 1 : (byte) 0);
        }

        protected Tool(Parcel in) {
            this.isExploit = in.readInt();
            this.isPutaway = in.readInt();
            this.isOpen = in.readInt();
            this.id = in.readInt();
            this.title_id = in.readInt();
            this.desc = in.readString();
            this.iconUrl = in.readString();
            this.introduce = in.readString();
            this.url = in.readString();
            this.position = in.readInt();
            this.showHotFlag = in.readInt();
            this.type = in.readInt();
            this.isClick = in.readInt();
            this.insert_time = in.readString();
            this.content = in.readString();
            this.code = in.readString();
            this.funintroduce = in.readString();
            this.title = in.readString();
            this.isShow = in.readInt();
            this.simulatedArg = in.readByte() != 0;
        }

        public static final Creator<Tool> CREATOR = new Creator<Tool>() {
            @Override
            public Tool createFromParcel(Parcel source) {
                return new Tool(source);
            }

            @Override
            public Tool[] newArray(int size) {
                return new Tool[size];
            }
        };
    }
}
