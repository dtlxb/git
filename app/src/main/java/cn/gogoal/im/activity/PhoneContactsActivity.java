package cn.gogoal.im.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.ContactAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.index.SuspendedDecoration;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/5/8.
 */

public class PhoneContactsActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;

    // 库 phone表字段
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
    //联系人显示名称
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    //电话号码
    private static final int PHONES_NUMBER_INDEX = 1;
    //头像ID
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    //联系人的ID
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    private List<ContactBean> contactBeanList;
    private ContactAdapter contactAdapter;
    private boolean added;

    @Override
    public int bindLayout() {
        return R.layout.activity_phone_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        init();

        BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.READ_CONTACTS}, new IPermissionListner() {
            @Override
            public void onUserAuthorize() {
                getContacts();
                contactAdapter = new ContactAdapter(getActivity(), contactBeanList, 1);

                contactAdapter.notifyDataSetChanged();

                rvContacts.addItemDecoration(new NormalItemDecoration(getActivity(),
                        getResColor(R.color.contactDividerColor)));

                rvContacts.setAdapter(contactAdapter);
            }

            @Override
            public void onRefusedAuthorize(List<String> deniedPermissions) {
                UIHelper.toast(PhoneContactsActivity.this, "获取通讯录权限被拒绝,无法使用");
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PhoneContactsActivity.this.finish();
                    }
                }, 1000);
            }
        });
    }

    private void init() {
        xTitle = setMyTitle(R.string.title_contacts, true);
        XTitle.TextAction textAction = new XTitle.TextAction("添加") {
            @Override
            public void actionClick(View view) {
                /*Intent intent = new Intent(PhoneContactsActivity.this, SearchPersonSquareActivity.class);
                intent.putExtra("search_index", 0);
                startActivity(intent);*/
            }
        };
        xTitle.addAction(textAction, 0);
        TextView RightText = (TextView) xTitle.getViewByAction(textAction);
        RightText.setTextColor(getResColor(R.color.textColor_333333));

        ViewGroup.LayoutParams tvParams = tvConstactsFlag.getLayoutParams();
        tvParams.width = AppDevice.getWidth(getActivity()) / 4;
        tvParams.height = AppDevice.getWidth(getActivity()) / 4;
        tvConstactsFlag.setLayoutParams(tvParams);

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        contactBeanList = new ArrayList<>();
    }

    private void getContacts() {
        ContentResolver resolver = PhoneContactsActivity.this.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //联系人电话
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //联系人名字
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //联系人头像
                Bitmap contactAvatar;
                if (phoneCursor.getLong(PHONES_PHOTO_ID_INDEX) > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    contactAvatar = BitmapFactory.decodeStream(ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri));
                } else {
                    contactAvatar = BitmapFactory.decodeResource(getResources(), R.mipmap.default_image);
                }
                ContactBean contactBean = new ContactBean();
                contactBean.setPhone(phoneNumber);
                contactBean.setNickname(contactName);
                contactBean.setAvatar(contactAvatar);
                contactBean.setContactType(ContactBean.ContactType.PERSION_ITEM);
                contactBean.setFriend_id(contactid.intValue());
                contactBeanList.add(contactBean);
            }
            SuspendedDecoration mDecoration = new SuspendedDecoration(getActivity());

            mDecoration.setmDatas(contactBeanList);

            indexBar.setmSourceDatas(contactBeanList)//设置数据
                    .invalidate();

            if (added) {
                rvContacts.addItemDecoration(mDecoration);

                added = false;
            }
        }
    }
}
