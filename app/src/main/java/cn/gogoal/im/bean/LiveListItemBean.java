package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :直播列表，item的实体类，综合个人直播，
 */
public class LiveListItemBean {

//    private LiveType liveType;      //视频类型
    private String startTime;       //开始时间/结束时间
    private String imgBg;           //视频背景
    private String title;           //标题
    private String anchorAvatar;    //播主头像url

    private String anchorName;      //播主名字
    private String programme_name;  //视频平台

    private String live_Id;         //视频的live id

    private int live_status;        //视频状态

    public LiveListItemBean(
//            LiveType liveType,
            String startTime,
            String imgBg,
            String title,
            String anchorAvatar,
            String anchorName,
            String programme_name,
            String live_Id,
            int live_status) {

//        this.liveType = liveType;
        this.startTime = startTime;
        this.imgBg = imgBg;
        this.title = title;
        this.anchorAvatar = anchorAvatar;
        this.anchorName = anchorName;
        this.programme_name = programme_name;
        this.live_Id = live_Id;
        this.live_status=live_status;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public String getProgramme_name() {
        return programme_name;
    }

    public void setProgramme_name(String programme_name) {
        this.programme_name = programme_name;
    }

    public String getLive_Id() {
        return live_Id;
    }

    public void setLive_Id(String live_Id) {
        this.live_Id = live_Id;
    }

//    public LiveType getLiveType() {
//        return liveType;
//    }
//
//    public void setLiveType(LiveType liveType) {
//        this.liveType = liveType;
//    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImgBg() {
        return imgBg;
    }

    public void setImgBg(String imgBg) {
        this.imgBg = imgBg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnchorAvatar() {
        return anchorAvatar;
    }

    public void setAnchorAvatar(String anchorAvatar) {
        this.anchorAvatar = anchorAvatar;
    }

    /*
    *枚举三中直播类型
    * 个人直播，PC端发起直播，录播
    * */
//    public enum LiveType {
//        PERSIONAL, FromPc, RecordVideo;
//    }
}
