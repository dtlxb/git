package com.gogoal.app.fragment;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogoal.app.R;
import com.gogoal.app.adapter.ContactAdapter;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.ContactBean;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.ui.index.IndexBar;
import com.gogoal.app.ui.index.SuspendedDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 联系人
 */
public class ContactsFragment extends BaseFragment {

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;

    public ContactsFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("联系人");

        ViewGroup.LayoutParams tvParams = tvConstactsFlag.getLayoutParams();
        tvParams.width = AppDevice.getWidth(getContext()) / 4;
        tvParams.height = AppDevice.getWidth(getContext()) / 4;
        tvConstactsFlag.setLayoutParams(tvParams);

        //初始化
        LinearLayoutManager layoutManager;
        rvContacts.setLayoutManager(layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));

        //indexbar初始化
        indexBar.setmPressedShowTextView(tvConstactsFlag)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        List<ContactBean> contactBeanList = getConstactsData();

        ContactAdapter contactAdapter = new ContactAdapter(getContext(), contactBeanList);

        contactAdapter.notifyDataSetChanged();

        SuspendedDecoration mDecoration = new SuspendedDecoration(getContext(), contactBeanList);

        indexBar.setmSourceDatas(contactBeanList)//设置数据
                .invalidate();
        mDecoration.setmDatas(contactBeanList);

        rvContacts.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        rvContacts.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        rvContacts.setAdapter(contactAdapter);

    }

    //==========================================临时代码段--开始，模拟数据================================
    /**
     * 模拟好友数据 20组英文名，10个数字名字，50组中文名
     */
    private List<ContactBean> getConstactsData() {

        List<ContactBean> contacts = new ArrayList<>();

        contacts.add(addFunctionHead("新的朋友",R.mipmap.cache_img_contacts_0));
        contacts.add(addFunctionHead("群聊",R.mipmap.cache_img_contacts_1));
        contacts.add(addFunctionHead("标签",R.mipmap.cache_img_contacts_2));
        contacts.add(addFunctionHead("公众号",R.mipmap.cache_img_contacts_3));

        for (int i = 0; i < 20; i++) {
            ContactBean<String> contactBean = new ContactBean<>();
            contactBean.setmAccountId(String.valueOf(i));
            contactBean.setContactType(ContactBean.ContactType.PersionItem);
            contactBean.setmAvatar(i < 12 ? "http://hply.image.alimmdn.com/image/LolitaCheng_" + $(i) + ".jpg@!center_crop_80" : null);
            contactBean.setmName(getEnglishName());
            contacts.add(contactBean);
        }

        for (int i = 0; i < 10; i++) {
            ContactBean<String> contactBean = new ContactBean<>();
            contactBean.setmAccountId(String.valueOf(i));
            contactBean.setContactType(ContactBean.ContactType.PersionItem);
            contactBean.setmAvatar(i < 12 ? "http://hply.image.alimmdn.com/image/LolitaCheng_" + $(i) + ".jpg" : null);
            contactBean.setmName(String.valueOf((int) (Math.random() * 900000 + 100000)));
            contacts.add(contactBean);
        }

        for (int i = 0; i < 50; i++) {
            ContactBean<String> contactBean = new ContactBean<>();
            contactBean.setmAccountId(String.valueOf(i));
            contactBean.setContactType(ContactBean.ContactType.PersionItem);
            contactBean.setmAvatar(i < 12 ? "http://hply.image.alimmdn.com/image/LolitaCheng_" + $(i) + ".jpg" : null);
            contactBean.setmName(getChineseName());
            contacts.add(contactBean);
        }

        return contacts;
    }

    private ContactBean<Integer> addFunctionHead(String name,@DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setmName(name);
        bean.setBaseIndexTag("↑");
        bean.setContactType(ContactBean.ContactType.FunctionItem);
        bean.setmAvatar(iconId);
        return bean;
    }

    private String $(int i) {
        return i > 9 ? i + "" : "0" + i;
    }

    /**
     * 生成随机英文名
     */
    private String getEnglishName() {
        return String.valueOf((char) (Math.random() * 26 + 65))
                + (char) (Math.random() * 26 + 97)
                + (char) (Math.random() * 26 + 97)
                + (char) (Math.random() * 26 + 97)
                + (char) (Math.random() * 26 + 97);
    }

    /**
     * 生成随机中文名
     */
    private String getChineseName() {
        String chinese = "汉皇重色思倾国御宇多年求不得杨家有女初长成养在深闺人未识天生丽质难自弃一朝选在君王侧回眸一笑百媚生六宫粉黛无颜色春寒赐浴华清池温泉水滑洗凝脂侍儿扶起娇无力始是新承恩泽时云鬓花颜金步摇芙蓉帐暖度春宵春宵苦短日高起从此君王不早朝承欢侍宴无闲暇春从春游夜专夜后宫佳丽三千人三千宠爱在一身金屋妆成娇侍夜玉楼宴罢醉和春姊妹弟兄皆列土可怜光彩生门户遂令天下父母心不重生男重生女骊宫高处入青云仙乐风飘处处闻缓歌谩舞凝丝竹尽日君王看不足渔阳鼙鼓动地来惊破霓裳羽衣曲九重城阙烟尘生千乘万骑西南行翠华摇摇行复止西出都门百余里六军不发无奈何宛转蛾眉马前死花钿委地无人收翠翘金雀玉搔头君王掩面救不得回看血泪相和流黄埃散漫风萧索云栈萦纡登剑阁峨嵋山下少人行旌旗无光日色薄蜀江水碧蜀山青圣主朝朝暮暮情行宫见月伤心色夜雨闻铃肠断声天旋地转回龙驭到此踌躇不能去马嵬坡下泥土中不见玉颜空死处君臣相顾尽沾衣东望都门信马归归来池苑皆依旧太液芙蓉未央柳芙蓉如面柳如眉对此如何不泪垂春风桃李花开日秋雨梧桐叶落时西宫南内多秋草落叶满阶红不扫梨园弟子白发新椒房阿监青娥老夕殿萤飞思悄然孤灯挑尽未成眠迟迟钟鼓初长夜耿耿星河欲曙天鸳鸯瓦冷霜华重翡翠衾寒谁与共悠悠生死别经年魂魄不曾来入梦临邛道士鸿都客能以精诚致魂魄为感君王辗转思遂教方士殷勤觅排空驭气奔如电升天入地求之遍上穷碧落下黄泉两处茫茫皆不见忽闻海上有仙山山在虚无缥渺间楼阁玲珑五云起其中绰约多仙子中有一人字太真雪肤花貌参差是金阙西厢叩玉扃转教小玉报双成闻道汉家天子使九华帐里梦魂惊揽衣推枕起徘徊珠箔银屏迤逦开云鬓半偏新睡觉花冠不整下堂来风吹仙袂飘飘举犹似霓裳羽衣舞玉容寂寞泪阑干梨花一枝春带雨含情凝睇谢君王一别音容两渺茫昭阳殿里恩爱绝蓬莱宫中日月长回头下望人寰处不见长安见尘雾惟将旧物表深情钿合金钗寄将去钗留一股合一扇钗擘黄金合分钿但教心似金钿坚天上人间会相见临别殷勤重寄词词中有誓两心知七月七日长生殿夜半无人私语时在天愿作比翼鸟在地愿为连理枝天长地久有时尽此恨绵绵无绝期";
        char[] chArr = chinese.toCharArray();

        return String.valueOf(chArr[(int) (Math.random() * chArr.length)])
                + chArr[(int) (Math.random() * chArr.length)]
                + chArr[(int) (Math.random() * chArr.length)]
                + chArr[(int) (Math.random() * chArr.length)];
    }

    //==========================================临时代码段--结束，模拟数据================================
}
