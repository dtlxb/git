package cn.gogoal.im.bean.group;

import cn.gogoal.im.bean.group.GroupData;

/**
 * author wangjd on 2017/5/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :群详情中群信息
 */
public class GroupInfoBean {

    /**
     * message : 成功
     * data : {"attr":{"chat_type":1002},"c":"348635","m":["348635","357006","348628","96338"],"name":"李达康、ljliu789、davesally......","conv_id":"5902b8d72f301e005801dd2d","m_size":4,"is_in":true,"is_creator":false,"name_in_group":"高育良","m_info":[{"account_id":348635,"account_name":"E00002638","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_C348B5570D407B7D.jpg","city":"","duty":"开发工程师","nickname":"davesally"},{"account_id":96338,"account_name":"E039065","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_96338_1495172632245.jpg","city":"","duty":"未设置","nickname":"祁同伟"},{"account_id":348628,"account_name":"E00002636","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_348628_1495074730620.jpg","city":"","duty":"ios开发工程师","nickname":"刘龙洁"},{"account_id":357006,"account_name":"E00003645","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_1CE7EE6183FCADAB.jpg","city":"","duty":"android开发","nickname":"高育良"}]}
     * code : 0
     */

    private String message;
    private GroupData data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GroupData getData() {
        return data;
    }

    public void setData(GroupData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
