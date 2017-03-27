package cn.gogoal.im.ui.index;

/**
 * 索引类的标志位的实体基类
 */

public abstract class BaseIndexBean implements ISuspensionInterface {
    private String baseIndexTag;//所属的分类

    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    public BaseIndexBean setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
        return this;
    }

    @Override
    public String getSuspensionTag() {
        return baseIndexTag;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
