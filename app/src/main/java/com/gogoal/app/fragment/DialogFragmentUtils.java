package com.gogoal.app.fragment;

/**
 * author wangjd on 2017/2/24 0024.
 * Staff_id 1375
 * phone 18930640263
 */
public class DialogFragmentUtils {

    private DialogFragmentUtils() {
    }

    private static DialogFragmentUtils instance = null;

    public static DialogFragmentUtils getInstance() {
        if (instance == null) {
            synchronized (DialogFragmentUtils.class) {
                if (instance == null) {
                    instance = new DialogFragmentUtils();
                }
            }
        }
        return instance;
    }

}
