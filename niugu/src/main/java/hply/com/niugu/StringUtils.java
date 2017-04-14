package hply.com.niugu;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    // 判断一个字符是否是中文
    private static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }

    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 字符串去掉空格回车换行
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("").trim();
        }
        return dest;
    }

    public static double getDouble(String doubleString) {
        if (TextUtils.isEmpty(doubleString)) {
            return 0.00d;
        } else {
            try {
                return Double.parseDouble(doubleString);
            }catch (Exception e){
                return 0.00d;
            }
        }
    }

    public static double get2Double(String doubleString) {
        return getDouble(doubleString,2);
    }

    /**
     * @param unit 保留小数位
     */
    public static double getDouble(String doubleString, int unit) {
        return Double.parseDouble(saveSignificand(doubleString,unit));
    }

    /**
     * 字符串全角化
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        if (input != null && input.length() > 0) {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            return new String(c);
        }
        return "";
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 简单的异或加密
     */
    public static String encryption(String str) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ 'G');
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    /**
     * 对上面加密的解密
     */
    public static String decode(String str) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ 'w');
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    // 顺序表
    static String orderStr = "";

    static {
        for (int i = 33; i < 127; i++) {
            orderStr += Character.toChars(i)[0];
        }
    }

    /**
     * 判断字符串是否有顺序
     */
    public static boolean isOrder(EditText etPsw) {
        String str = etPsw.getText().toString();
        return str.matches("((\\d)|([a-z])|([A-Z]))+") && orderStr.contains(str);
    }

    /**
     * 判断字符串是否有相同
     */
    public static boolean isSame(String str) {
        String regex = str.substring(0, 1) + "{" + str.length() + "}";
        return str.matches(regex);
    }

    public static String reverseString(String orderStr) {
        return new StringBuilder(orderStr).reverse().toString();
    }

    /**
     * 输入的手机号是否合法
     */
    public static boolean checkPhoneString(String phoneNumber) {
        if (!phoneNumber.matches("^1[3-57-9]\\d{9}$")) {
            return false;
        }
        return true;
    }

    /**
     * 输入的邮箱格式是否合法
     */
    public static boolean checkEmail(String email) {
        if (!email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
            return false;
        }
        return true;
    }

    /**
     * 保留有效数字
     */
    public static String saveSignificand(double doubleData, int significand) {
        return String.format("%." + significand + "f", doubleData);
    }

    /**
     * 保留有效数字
     */
    public static String saveSignificand(float doubleData, int significand) {
        return String.format("%." + significand + "f", doubleData);
    }

    public static String getOnePointData(float floatData) {
        return String.format(Locale.CHINA,"%.1f", floatData);
    }

    /**
     * 保留2有效数字
     */
    public static String save2Significand(float doubleData) {
        return saveSignificand(doubleData,2);
    }

    public static String saveSignificand(String strDoubleData, int significand) {
        try {
            return String.format("%." + significand + "f", Double.parseDouble(strDoubleData));
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 取小数点后N位  例：format="%.2f","%.3f","%.4f"...
     */
    public static String getAnyPointFloat(String format, float floatData) {
        return String.format(format, floatData);
    }

    /**
     * 取小数点后N位 例：format="%.2f","%.3f","%.4f"...
     */
    public static String getAnyPointDouble(String format, double doubleData) {
        return String.format(format, doubleData);
    }

    /**
     * 取整
     */
    public static String getIntegerData(String floatData) {
        return floatData.substring(0, floatData.indexOf("."));
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    //格式化1-9这几个数字的为01,02等格式
    public static String formatInteger(int i) {
        return i > 9 ? (i + "") : ("0" + i);
    }

    /**
     * 语言时长格式化
     */
    public static String voidFormat(int voiceSecond) {
        if (voiceSecond < 60) {
            return "0:" + formatInteger(voiceSecond);// 返回56''
        } else if (voiceSecond >= 60 && voiceSecond < 3600) {
            return voiceSecond / 60 + ":" + formatInteger(voiceSecond % 60); //返回1:16,1:06
        } else if (voiceSecond >= 3600 && voiceSecond < 86400) {
            int h = voiceSecond / 3600;
            int min = (voiceSecond - h * 3600) / 60;
            int sec = voiceSecond % 60;
            return h + ":" + formatInteger(min) + ":" + formatInteger(sec);//返回1:02：06
        } else {
            return "语音时长错误";
        }
    }

    /**
     * 判断两字符串是否相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (TextUtils.isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (TextUtils.isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 字符串过滤器 删除@某人（@**** ）
     */
    public static String StringFilter(String str) {
        // 清除掉某人
        //String regEx = "@*[\\S]*[ \r\n]";
        String newString = "";
        if (str.endsWith(" ")) {
            int postionAt = str.lastIndexOf("@");
            if (postionAt != -1) {
                newString = str.substring(0, postionAt + 1);
            } else {
                newString = str;
            }
        } else {
            newString = str;
        }
        return newString;
    }


    /**
     * 除掉HTML里面所有标签
     */
    public static String removeTag(String htmlStr) {

        if (htmlStr == null) return "--";

        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // script
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // style
        String regEx_html = "<[^>]+>"; // HTML tag
        String regEx_space = "\\s+|\t|\r|\n|&nbsp;|&rdquo;|&ldquo;";// other characters

        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern
                .compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        Pattern p_space = Pattern
                .compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");

        return htmlStr;
    }

    public static int dp2px(Context context, int dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density + dpValue + 0.5f);
    }

    public static String save2Significand(double mMaxPrice) {
        return saveSignificand(mMaxPrice, 2);
    }

    public static String save2Significand(String mMaxPrice) {
        return saveSignificand(mMaxPrice, 2);
    }

    public static String getDateFormat(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getDateFormat(String format, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return getDateFormat(format, simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "日期格式出错";
        }
    }

    public static String getDateFormat(String format, long date) {
        return getDateFormat(format, new Date(date));
    }
}
