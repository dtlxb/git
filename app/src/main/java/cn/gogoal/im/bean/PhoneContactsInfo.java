package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :匹配结果 返回
 */
public class PhoneContactsInfo {

    /**
     * message : 成功
     * data : [{"name":"安安","in_use":false,"mobile":"13333333333"},{"name":"格格","in_use":false,"mobile":"13564646464"},{"name":"格格","in_use":false,"mobile":"13694646464"},{"name":"123","in_use":false,"mobile":"13821546089"},{"name":"菲菲","in_use":false,"mobile":"14725839564"},{"name":"恩恩","in_use":false,"mobile":"15555555555"},{"name":"185 1685 3695","in_use":false,"mobile":"18516853695"},{"friend_info":{"conv_id":"591a99fb2f301e00588bcfac","friend_id":393028,"group":null,"remark":null,"nickname":"杨春雨","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","duty":"java开发工程师"},"is_friend":true,"name":"大黄","in_use":true,"account_info":{"account_id":393028,"account_name":"E00018282","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","city":"湖北省武汉市","duty":"java开发工程师","mobile":"18796014744","nickname":"杨春雨"},"mobile":"18796014744"},{"name":"舟舟","in_use":false,"mobile":"18888888888"},{"is_friend":false,"name":"王尼玛","in_use":true,"account_info":{"account_id":357006,"account_name":"E00003645","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_C317F15BB2B3AA91.jpg","city":"","duty":"android开发","mobile":"18930640263","nickname":"隔壁小王"},"mobile":"18930640263"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<PhoneContactData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PhoneContactData> getData() {
        return data;
    }

    public void setData(List<PhoneContactData> data) {
        this.data = data;
    }

}
