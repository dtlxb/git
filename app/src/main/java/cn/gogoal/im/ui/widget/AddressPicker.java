package cn.gogoal.im.ui.widget;

import android.view.View;
import android.widget.NumberPicker;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.AddressBean;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.StringPicker;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;


/**
 * author wangjd on 2017/4/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :自定义地址选择器
 */
public class AddressPicker extends BaseBottomDialog {

    private List<String> cityNameLists = new ArrayList<>();
    private StringPicker pickerCity;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_address_picker;
    }

    @Override
    public void bindView(View v) {

        final StringPicker pickerProvince = (StringPicker) v.findViewById(R.id.picker_province);
        pickerCity = (StringPicker) v.findViewById(R.id.picker_city);

        noInput(pickerProvince);
        noInput(pickerCity);

        String addressString = UIHelper.getRawString(getContext(), R.raw.address_juhe);
        AddressBean addressbean = JSONObject.parseObject(addressString, AddressBean.class);

        //省数据
        final List<AddressBean.ProvinceBean> provinceList = addressbean.getResult();
        List<String> provinceNameList = new ArrayList<>();
        for (AddressBean.ProvinceBean pro : provinceList) {
            provinceNameList.add(pro.getProvince());
        }
        pickerProvince.setMaxValue(provinceNameList.size() - 1);
        pickerProvince.setMinValue(0);
        pickerProvince.setDisplayedValues(provinceNameList.toArray(new String[provinceList.size()]));

        cityNameLists = new ArrayList<>();
        setCityData(provinceList.get(0).getCity());

        pickerProvince.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setCityData(provinceList.get(newVal).getCity());
            }
        });

        v.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String provinceChoose = provinceList.get(pickerProvince.getValue()).getProvince();
                final String cityChoose = provinceList.get(pickerProvince.getValue()).getCity().get(pickerCity.getValue()).getCity();
//                UIHelper.toast(v.getContext(),provinceChoose+" "+cityChoose);

                Map<String,String> addMap=new HashMap<>();
                addMap.put("province",provinceChoose);
                addMap.put("city",cityChoose);

                UserUtils.updataNetUserInfo(addMap, new UserUtils.UpdataListener() {
                    @Override
                    public void success(String responseInfo) {
                        //更新本地缓存
                        UserUtils.updataLocalUserInfo("organization_address",provinceChoose+" "+cityChoose);
                        //发消息更新当前列表
                        AppManager.getInstance().sendMessage("updata_userinfo","弹窗中更新");
                    }

                    @Override
                    public void failed(String errorMsg) {
                        UIHelper.toast(getActivity(),"更新出错");
                    }
                });


                AddressPicker.this.dismiss();

            }
        });
    }

    private void setCityData(List<AddressBean.ProvinceBean.CityBean> cityList) {
        //市数据
        cityNameLists.clear();
        for (AddressBean.ProvinceBean.CityBean cityBean : cityList) {
            cityNameLists.add(cityBean.getCity());
        }
        try {
            pickerCity.setMaxValue(cityNameLists.size() - 1);
        } catch (Exception e) {
            String eMessage = e.getMessage();
            KLog.e(eMessage);
        }
        pickerCity.setMinValue(0);
        pickerCity.setDisplayedValues(cityNameLists.toArray(new String[cityNameLists.size()]));

    }

    private void noInput(NumberPicker picker) {
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
    }

}