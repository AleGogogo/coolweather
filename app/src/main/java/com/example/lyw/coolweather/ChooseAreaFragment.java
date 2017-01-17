package com.example.lyw.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyw.coolweather.db.City;
import com.example.lyw.coolweather.db.County;
import com.example.lyw.coolweather.db.Province;
import com.example.lyw.coolweather.util.HttpUtil;
import com.example.lyw.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LYW on 2017/1/17.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static final String ADDRESS_PROVINCE = "http://guolin" +
            ".tech/api/china";
    public static final String ID_PROVINCE = " provinceid=? ";
    public static final String ID_CITY = " cityid = ? ";

    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private ListView listView;
    private Button backButton;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList = new ArrayList<>();
    /**
     * 市列表
     */
    private List<City> cityList = new ArrayList<>();
    /**
     * 县列表
     */
    private List<County> countyList = new ArrayList<>();

    /**
     * 选中的省市
     */
    private Province selectedProvince;

    /**
     * 选中的市
     */
    private City selectedCity;

    /**
     * 选中的县
     */
    private County selectedCounty;
    /**
     * 当前选中的级别
     */
    private int currentlevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.id_title_text);
        backButton = (Button) view.findViewById(R.id.id_button_back);
        listView = (ListView) view.findViewById(R.id.id_list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                .simple_list_item_1, dataList);

        return view;
    }

    /**
     * 为什么要iniListener()
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                if (currentlevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);
                    queryCities();
                } else if (currentlevel == LEVEL_CITY) {
                    selectedCity = cityList.get(i);
                    queryCounty();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentlevel == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });
    }

    private void queryCounty() {
        titleText.setText(selectedCounty.getCountyName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where(ID_CITY, String.valueOf(selectedCity
                .getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_COUNTY;
        } else {
            queryFromSever(ADDRESS_PROVINCE + "/" + selectedProvince
                            .getProvinceCode() + "/" + selectedCounty
                            .getCityId(),
                    "county");

        }
    }

    private void queryCities() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where(ID_PROVINCE, String.valueOf
                (selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_CITY;
        } else {
            queryFromSever(ADDRESS_PROVINCE + "/" + selectedProvince
                    .getProvinceCode(), "city");
        }

    }

    /**
     * 优先从数据库里查询，没有，再从服务器中查询
     */
    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel = LEVEL_PROVINCE;
        } else {
            queryFromSever(ADDRESS_PROVINCE, "province");
        }

    }

    private void queryFromSever(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast
                                .LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws
                    IOException {
                String responseText = response.body().toString();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText,
                            selectedCity.getId());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });

                }
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("正在加载.....");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
         mProgressDialog.show();
    }

    private void closeProgressDialog() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
