package com.example.dell.bzbp_frame;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dell.bzbp_frame.fragment.FriendsAddFragment;
import com.example.dell.bzbp_frame.fragment.FriendsApplyFragment;
import com.example.dell.bzbp_frame.fragment.FriendsFragment;
import com.example.dell.bzbp_frame.model.User;

public class Friends2Activity extends FragmentActivity {

    private Bundle bundle;
    private User user;
    private FriendsFragment friendsFragment;
    private FriendsAddFragment friendsAddFragment;
    private FriendsApplyFragment friendsApplyFragment;
    private Fragment[] fragments;
    private int lastShowFragment = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_friends_list:
                    if (lastShowFragment != 0) {
                        switchFrament(lastShowFragment, 0);
                        lastShowFragment = 0;
                    }
                    return true;
                case R.id.navigation_friends_add:
                    if (lastShowFragment != 1) {
                        switchFrament(lastShowFragment, 1);
                        lastShowFragment = 1;
                    }
                    return true;
                case R.id.navigation_friends_apply:
                    if (lastShowFragment != 2) {
                        switchFrament(lastShowFragment, 2);
                        lastShowFragment = 2;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends2);
        bundle = this.getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_friends);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragments();
    }

    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            fragments[index].setArguments(bundle);
            transaction.add(R.id.fragment_container_friends, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private void initFragments() {
        friendsFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        friendsFragment.setArguments(bundle);
        friendsAddFragment = new FriendsAddFragment();
        friendsApplyFragment = new FriendsApplyFragment();
        fragments = new Fragment[]{friendsFragment ,friendsAddFragment,friendsApplyFragment};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_friends,friendsFragment)
                .show(friendsFragment)
                .commit();
    }


}
