package cn.gogoal.im.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.PhoneContact;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.ui.index.IndexBar;
import cn.gogoal.im.ui.view.TextDrawable;
import cn.gogoal.im.ui.view.XLayout;
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

    @BindView(R.id.xLayout)
    XLayout xLayout;

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

    @Override
    public int bindLayout() {
        return R.layout.activity_phone_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        init();
        initXLayout();

        BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.READ_CONTACTS}, new IPermissionListner() {
            @Override
            public void onUserAuthorize() {
                List<PhoneContact> contacts = getContacts();
                if (contacts.isEmpty()){
                    xLayout.setStatus(XLayout.Empty);
                }else {
                    xLayout.setStatus(XLayout.Success);
                    List<Map<String,String>> mapContacts=new ArrayList<>();
                    for (PhoneContact contact:contacts){
                        Map<String,String> map=new HashMap<>();
                        map.put("name",contact.getName());
                        map.put("mobile",contact.getMobile());
                        mapContacts.add(map);
                    }

                    KLog.e(JSONObject.toJSONString(mapContacts));

                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("token", UserUtils.getToken());
                    map.put("contacts", JSONObject.toJSONString(mapContacts));

                    new GGOKHTTP(map, GGOKHTTP.GET_CONTACTS, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            KLog.e(responseInfo);
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    }).startGet();
                }
            }

            @Override
            public void onRefusedAuthorize(List<String> deniedPermissions) {
            }
        });
    }

    private void initXLayout() {
        xLayout.setEmptyText("");
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
        TextView rightText = (TextView) xTitle.getViewByAction(textAction);
        rightText.setTextColor(getResColor(R.color.textColor_333333));

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


    }

    private List<PhoneContact> getContacts() {
        List<PhoneContact> contacts = new ArrayList<>();
        TextDrawable.IBuilder iBuilder = TextDrawable.builder().rect();
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
                    iBuilder.build(String.valueOf(contactName.charAt(0)), getContactBgColor());
                    contactAvatar = BitmapFactory.decodeResource(getResources(), R.mipmap.default_image);
                }

                if (!StringUtils.isActuallyEmpty(phoneNumber)) {
                    contacts.add(
                            new PhoneContact(
                                    contactName,
                                    phoneNumber.replaceAll("\\s","").replace("-",""),
                                    contactAvatar,
                                    contactid));
                }
            }
        }

        if (phoneCursor != null) {
            phoneCursor.close();
        }

        return contacts;
    }

    /**
     * 随机颜色背景
     */
    private int getContactBgColor() {
        int[] colors = {Color.rgb(180, 65, 56), Color.rgb(107, 112, 114),
                Color.rgb(112, 159, 167), Color.rgb(157, 198, 176), Color.rgb(201, 134, 107)};
        return colors[new Random().nextInt(5)];
    }
}
