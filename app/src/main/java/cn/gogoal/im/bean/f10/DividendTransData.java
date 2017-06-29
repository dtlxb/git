package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/29.
 * Desc: description
 */
public class DividendTransData {
    /*{
        "stock_record_date":null,
            "dividend_date":null,
            "ex-dividend_date":null,
            "project_schedule":"董事会预案",
            "dividend_program":"10派1.58元(含税)",
            "date":"2017-03-17"
    }*/

    private String dividend_program;
    private String date;

    public DividendTransData(String dividend_program, String date) {
        this.dividend_program = dividend_program;
        this.date = date;
    }

    public String getDividend_program() {
        return dividend_program;
    }

    public void setDividend_program(String dividend_program) {
        this.dividend_program = dividend_program;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
