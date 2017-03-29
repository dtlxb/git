package cn.gogoal.im.common;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

/**
 * author wangjd on 2017/3/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${检索逻辑算法}.
 */
public class CompareUtils {

    private String query;//朝，朝阳

    private CompareUtils() {
    }

    public static CompareUtils use(String query) {
        CompareUtils utils=new CompareUtils();
        utils.query = query;
        return utils;
    }

    /*
    *  final String targetZh = contactBean.getTarget();//原始文本

    final String targetEn = Pinyin.toPinyin(targetZh, "").toLowerCase();//

    final String simpleSpell = getSimpleSpell(contactBean.getTarget());

    if (targetZh.contains(query) ||
            targetEn.contains(query) ||
            simpleSpell.substring(0,
                    getSimpleSpell(query).length()>simpleSpell.length()?simpleSpell.length():getSimpleSpell(query).length())
                    .equals(getSimpleSpell(query))) {
    * */

    public boolean compareFrom(String objective) {
        return objective.contains(query) ||//检索被文本包含
                getSimpleSpell(objective).contains(query)//检索文字被文本简拼包含
                || arrayAppend(objective, query)

                ;
    }

    /**
     * 获取汉字的每个汉字的首字母简拼
     * 比如传入“朝阳永续”，返回zyyx
     */
    private String getSimpleSpell(String target) {
        char[] chars = target.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            builder.append(Pinyin.toPinyin(c).charAt(0));
        }
        return builder.toString().toLowerCase();
    }

    private String getPinyin(String s) {
        return Pinyin.toPinyin(s, "");
    }

    /**
     * 获取汉字的每个汉字的简拼
     * 比如传入“朝阳永续”，返回[zhao,yang,yong,xu]
     */
    private List<String> getSpellArray(String target) {
        List<String> list = new ArrayList<>();
        char[] chars = target.toCharArray();
        for (char c : chars) {
            list.add(Pinyin.toPinyin(c));
        }
        return list;
    }

    private boolean arrayAppend(String objective, String query) {
        List<String> array = getSpellArray(objective);

        int index = arrayAppendStart(array, query);

        StringBuilder builder = new StringBuilder();
        if (index != -1) {
            List<String> list = array.subList(index, array.size() - 1);
            for (String s : list) {
                builder.append(s);
            }
            return builder.toString().startsWith(query);
        }
        return false;
    }

    private int arrayAppendStart(List<String> array, String query) {
        for (int i = 0; i < array.size(); i++) {
            if (query.contains(array.get(i))) {
                return i;
            }
        }
        return -1;
    }


}

