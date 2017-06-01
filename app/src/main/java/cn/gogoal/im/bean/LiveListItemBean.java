package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :直播列表，item的实体类，综合个人直播，
 */
public class LiveListItemBean {
    private int live_source;//来源，个人(1)还是后台(2)
    private int live_status;//视频状态，直播中(1)还是预约直播(2),还是回放视频(-1)
    private String live_id;//视频唯一标识码
    private String liveImgBg;//视频的背景图
    private String title;//视频标题
    private String anchorAvatar;//作者头像
    private String anchorName;//作者名字

    private String programme_name;//视频机构

    private String startTime;//开始时间

    private String playerCount;//在线人数

    private boolean havePermissions;//是否需要邀约，是否具有权限


    public LiveListItemBean(int live_source, int live_status, String live_id, String liveImgBg, String title, String anchorAvatar, String anchorName, String programme_name, String startTime, String playerCount, boolean havePermissions) {
        this.live_source = live_source;
        this.live_status = live_status;
        this.live_id = live_id;
        this.liveImgBg = liveImgBg;
        this.title = title;
        this.anchorAvatar = anchorAvatar;
        this.anchorName = anchorName;
        this.programme_name = programme_name;
        this.startTime = startTime;
        this.playerCount = playerCount;
        this.havePermissions = havePermissions;
    }

    public boolean isHavePermissions() {
        return havePermissions;
    }

    public void setHavePermissions(boolean havePermissions) {
        this.havePermissions = havePermissions;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public int getLive_source() {
        return live_source;
    }

    public void setLive_source(int live_source) {
        this.live_source = live_source;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getLiveImgBg() {
        return liveImgBg;
    }

    public void setLiveImgBg(String liveImgBg) {
        this.liveImgBg = liveImgBg;
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
}
