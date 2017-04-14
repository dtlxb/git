package cn.gogoal.im.bean.stock;

/**
 * author wangjd on 2017/4/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ChartImageBean {

    /**
     * message : 成功
     * data : {"pe_img":"http://ggimage.go-goal.cn/prod/pe/603223_20170412.png!suntime","eps_img":"http://ggimage.go-goal.cn/prod/eps/603223_20170412.png!suntime","profit_img":"http://ggimage.go-goal.cn/prod/profit/603223_20170412.png!suntime"}
     * code : 0
     */

    private String message;
    private ChartImage data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChartImage getData() {
        return data;
    }

    public void setData(ChartImage data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ChartImage {
        /**
         * pe_img : http://ggimage.go-goal.cn/prod/pe/603223_20170412.png!suntime
         * eps_img : http://ggimage.go-goal.cn/prod/eps/603223_20170412.png!suntime
         * profit_img : http://ggimage.go-goal.cn/prod/profit/603223_20170412.png!suntime
         */

        private String pe_img;
        private String eps_img;
        private String profit_img;

        public String getPe_img() {
            return pe_img;
        }

        public void setPe_img(String pe_img) {
            this.pe_img = pe_img;
        }

        public String getEps_img() {
            return eps_img;
        }

        public void setEps_img(String eps_img) {
            this.eps_img = eps_img;
        }

        public String getProfit_img() {
            return profit_img;
        }

        public void setProfit_img(String profit_img) {
            this.profit_img = profit_img;
        }
    }
}
