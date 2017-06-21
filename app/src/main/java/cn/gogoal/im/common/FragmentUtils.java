package cn.gogoal.im.common;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * author wangjd on 2017/6/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :Fragmetn utils
 */
public class FragmentUtils {

    private FragmentManager manager;

    private int containerViewId;

    private FragmentUtils(FragmentManager manager, @IdRes int containerViewId) {
        this.manager = manager;
        this.containerViewId = containerViewId;
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    public void addFragment(Fragment fragment,
                            String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(containerViewId, fragment, tag);
        transaction.commit();
    }

    public void shaowSingleFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }

    public void showFragment() {

    }

}
