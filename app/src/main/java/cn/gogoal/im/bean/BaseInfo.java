package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BaseInfo {

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : {"nickname":"E00002639","duty":"开发","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","org_name":"朝阳永续","account_id":348638,"email":"760723155@qq.com","full_name":"黄星星","gender":"未","mobile":"15316070306","account_name":"E00002639","department":"技术部"}
     * executeTime : 15
     */

    private int code;
    private String message;
    private String requestId;
    private ContactBean data;
    private int executeTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ContactBean getData() {
        return data;
    }

    public void setData(ContactBean data) {
        this.data = data;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

}
