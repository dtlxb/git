package cn.gogoal.im.common.linkUtils;

/**
 * Created by dave.
 * Date: 2017/6/2.
 * Desc: 输入法状态 和 横竖屏和状态发生 改变后 的 回调
 */
public interface OnSoftInputWithDifferListener {

    void isPortraitInputSoftOpen(int differ);

    void isPortraitInputSoftClosed();

    void isLandscape();

    void isPortrait();

    void isLandscapeInputSoftOpen();

    void isLandscapeInputSoftClose();
}
