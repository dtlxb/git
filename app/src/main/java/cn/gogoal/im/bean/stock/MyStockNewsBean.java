//package cn.gogoal.im.bean.stock;
//
//import java.util.List;
//
///**
// * Author wangjd on 2017/4/10 0010.
// * EmployeeNumber 1375
// * Phone 18930640263
// * Description :我的自选股 新闻、公告实体
// */
//public class MyStockNewsBean {
//
//    private String message;
//    private int code;
//    private List<DataBean> data;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        private String author;
//        private int comment_sum;
//        private String title;
//        private String origin_link;
//        private int origin_id;
//        private int source;
//        private String origin;
//        private int praise_sum;
//        private int favor_sum;
//        private int type;
//        private String date;
//        private int share_sum;
//        private List<StockBean> stock;
//
//        public String getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(String author) {
//            this.author = author;
//        }
//
//        public int getComment_sum() {
//            return comment_sum;
//        }
//
//        public void setComment_sum(int comment_sum) {
//            this.comment_sum = comment_sum;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getOrigin_link() {
//            return origin_link;
//        }
//
//        public void setOrigin_link(String origin_link) {
//            this.origin_link = origin_link;
//        }
//
//        public int getOrigin_id() {
//            return origin_id;
//        }
//
//        public void setOrigin_id(int origin_id) {
//            this.origin_id = origin_id;
//        }
//
//        public int getSource() {
//            return source;
//        }
//
//        public void setSource(int source) {
//            this.source = source;
//        }
//
//        public String getOrigin() {
//            return origin;
//        }
//
//        public void setOrigin(String origin) {
//            this.origin = origin;
//        }
//
//        public int getPraise_sum() {
//            return praise_sum;
//        }
//
//        public void setPraise_sum(int praise_sum) {
//            this.praise_sum = praise_sum;
//        }
//
//        public int getFavor_sum() {
//            return favor_sum;
//        }
//
//        public void setFavor_sum(int favor_sum) {
//            this.favor_sum = favor_sum;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public String getDate() {
//            return date;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public int getShare_sum() {
//            return share_sum;
//        }
//
//        public void setShare_sum(int share_sum) {
//            this.share_sum = share_sum;
//        }
//
//        public List<StockBean> getStock() {
//            return stock;
//        }
//
//        public void setStock(List<StockBean> stock) {
//            this.stock = stock;
//        }
//
//        public static class StockBean {
//            /**
//             * stock_price : 31.22
//             * stock_code : 603223
//             * stock_type : 1
//             * stock_rate : -1.0459587955626048
//             * stock_name : 恒通股份
//             * spell : HTGF
//             */
//
//            private double stock_price;
//            private String stock_code;
//            private int stock_type;
////            private double stock_rate;
//            private String stock_name;
//            private String spell;
//
//            public double getStock_price() {
//                return stock_price;
//            }
//
//            public void setStock_price(double stock_price) {
//                this.stock_price = stock_price;
//            }
//
//            public String getStock_code() {
//                return stock_code;
//            }
//
//            public void setStock_code(String stock_code) {
//                this.stock_code = stock_code;
//            }
//
//            public int getStock_type() {
//                return stock_type;
//            }
//
//            public void setStock_type(int stock_type) {
//                this.stock_type = stock_type;
//            }
//
////            public double getStock_rate() {
////                return stock_rate;
////            }
//
////            public void setStock_rate(double stock_rate) {
////                this.stock_rate = stock_rate;
////            }
//
//            public String getStock_name() {
//                return stock_name;
//            }
//
//            public void setStock_name(String stock_name) {
//                this.stock_name = stock_name;
//            }
//
//            public String getSpell() {
//                return spell;
//            }
//
//            public void setSpell(String spell) {
//                this.spell = spell;
//            }
//        }
//    }
//}
