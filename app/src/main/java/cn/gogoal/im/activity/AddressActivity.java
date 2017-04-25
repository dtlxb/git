package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.AddressBean;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.index.IndexBar;


/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :地区选择，两级联动
 */
public class AddressActivity extends BaseActivity {
    @BindView(R.id.rv_contacts)
    RecyclerView rvAddress;

    @BindView(R.id.index_bar)
    IndexBar indexBar;

    @BindView(R.id.tv_constacts_flag)
    TextView tvConstactsFlag;

    @Override
    public int bindLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_choose_address, true);
        BaseActivity.initRecycleView(rvAddress, 0);

        int loadAddressType = getIntent().getIntExtra("load_address_type", 0);

        int provincePosition = getIntent().getIntExtra("province_position", 0);
        int cityPosition = getIntent().getIntExtra("city_position", 0);

        String addressString = UIHelper.getRawString(mContext, R.raw.address_juhe);
        AddressBean city = JSONObject.parseObject(addressString, AddressBean.class);

        //省
        List<AddressBean.ProvinceBean> provinces = new ArrayList<>();
        provinces.clear();
        provinces.addAll(city.getResult());

        if (loadAddressType == 0) {
            rvAddress.setAdapter(new ProvinceAdapter(provinces));
        } else if (loadAddressType == 1) {
            //市
            List<AddressBean.ProvinceBean.CityBean> citys = new ArrayList<>();
            citys.clear();
            citys.addAll(city.getResult().get(provincePosition).getCity());
            rvAddress.setAdapter(new CityAdapter(citys));
        } else {
            List<AddressBean.ProvinceBean.CityBean.DistrictBean> districts = new ArrayList<>();
            districts.clear();
            districts.addAll(city.getResult().get(provincePosition).getCity().get(cityPosition).getDistrict());

            rvAddress.setAdapter(new DistrictAdapter(districts));
        }
    }

    private class ProvinceAdapter extends CommonAdapter<AddressBean.ProvinceBean, BaseViewHolder> {

        public ProvinceAdapter(List<AddressBean.ProvinceBean> data) {
            super(R.layout.item_address, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AddressBean.ProvinceBean data, final int position) {
            TextView tvAddressName = holder.getView(R.id.tv_address);
            tvAddressName.setText(data.getProvince());

            tvAddressName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddressActivity.class);
                    intent.putExtra("load_address_type", 1);
                    intent.putExtra("province_position", position);

                    startActivity(intent);
                }
            });
        }
    }

    private class CityAdapter extends CommonAdapter<AddressBean.ProvinceBean.CityBean, BaseViewHolder> {

        public CityAdapter(List<AddressBean.ProvinceBean.CityBean> data) {
            super(R.layout.item_address, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AddressBean.ProvinceBean.CityBean data, final int position) {
            TextView tvAddressName = holder.getView(R.id.tv_address);
            tvAddressName.setText(data.getCity());

            tvAddressName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddressActivity.class);
                    intent.putExtra("load_address_type", 2);
                    intent.putExtra("province_position", getIntent().getIntExtra("province_position", 0));
                    intent.putExtra("city_position", position);
                    startActivity(intent);
                }
            });
        }
    }

    private class DistrictAdapter extends CommonAdapter<AddressBean.ProvinceBean.CityBean.DistrictBean, BaseViewHolder> {

        public DistrictAdapter(List<AddressBean.ProvinceBean.CityBean.DistrictBean> data) {
            super(R.layout.item_address, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AddressBean.ProvinceBean.CityBean.DistrictBean data, final int position) {
            TextView tvAddressName = holder.getView(R.id.tv_address);
            tvAddressName.setText(data.getDistrict());

            tvAddressName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UIHelper.toast(v.getContext(),);
                }
            });
        }
    }

}
