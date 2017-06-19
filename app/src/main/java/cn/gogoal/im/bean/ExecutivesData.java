package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/6/16.
 * Desc: description
 */
public class ExecutivesData {

    /*"sex":"男",
            "degree":"博士研究生",
            "duty":"董事长",
            "duty_time":"2016-12-10",
            "age":49,
            "name":"谢永林",
            "brief":"谢永林：1968年9月出生*/

    private String sex;
    private String degree;
    private String duty;
    private String duty_time;
    private String age;
    private String name;
    private String brief;

    public ExecutivesData(String name, String degree, String duty) {
        this.name = name;
        this.degree = degree;
        this.duty = duty;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getDuty_time() {
        return duty_time;
    }

    public void setDuty_time(String duty_time) {
        this.duty_time = duty_time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
