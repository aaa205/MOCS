package com.mocs.record.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mocs.R;
import com.mocs.common.bean.Record;
import com.mocs.record.model.RecordModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * poi搜索：搜索栏中只有开头和关键字一样的项才能显示 --先不用
 */
public class MapActivity extends AppCompatActivity implements
        LocationSource, AMapLocationListener, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.toolbar_map)
    Toolbar toolbar;
    @BindView(R.id.search_view_map)
    MaterialSearchView searchView;
    @BindArray(R.array.record_type)
    String[] types;//种类
    private RecordModel mRecordModel;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mLocation;
    private PoiSearch mPoiSearch;
    private String mKeyWord;//保存搜索栏关键字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        checkPermission();
        mMapView.onCreate(savedInstanceState);
        initToolbar();
        mRecordModel = new RecordModel(this, getIntent().getParcelableExtra("local_user"));
        new NearByRecordAsyncTask().execute();//绘制附近的标记
    }

    /**
     * 执行POI搜索
     *
     * @param keyword
     */
    private void doPOISearch(String keyword) {
        if (null == mPoiSearch) {

            PoiSearch.Query query = new PoiSearch.Query(keyword, "", mLocation.getCity());
            query.setPageSize(7);//每页返回多少条结果
            query.setPageNum(0);//查询页码
            PoiSearch mPoiSearch = new PoiSearch(this, query);
            mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mLocation.getLatitude(),
                    mLocation.getLongitude()), 1500));//设置周边搜索的中心点以及半径
            mPoiSearch.setOnPoiSearchListener(this);

        }
        mPoiSearch.searchPOIAsyn();
    }

    /**
     * 获取输入提示搜索
     *
     * @param keyword
     */
    private void getInputTips(String keyword) {
        //设置输入提醒
        mKeyWord = keyword;
        InputtipsQuery inputtipsQuery = new InputtipsQuery(keyword, mLocation.getCity());
        inputtipsQuery.setCityLimit(true);//限制当前城市
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.menu_map);
        //设置searchView
        // searchView.setMenuItem(toolbar.getMenu().getItem(0));//监听搜索按钮的点击事件
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            /**
             * 请求输入提示
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                getInputTips(newText);
                Log.d("MAP_SEARCH", "TextChange: " + newText);
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_commit_map:
                    if (mLocation != null) {
                        //定位成功
                        if (mLocation.getErrorCode() == 0) {
                            Intent intent = new Intent();
                            intent.putExtra("record_form", createRecord());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(this, mLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                case R.id.actoin_search_map:
                    //打开搜索框
                    searchView.showSearch(true);
                    searchView.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        });
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    /**
     * 根据创建 AMapLocation 创建Bean
     *
     * @return bean中自定义的Record类，存储地址信息
     */
    private Record createRecord() {
        Record info = new Record();
        info.setAddress(mLocation.getAddress());
        info.setCity(mLocation.getCity());
        info.setCountry(mLocation.getCountry());
        info.setDistrict(mLocation.getDistrict());
        info.setStreet(mLocation.getStreet());
        info.setStreetNum(mLocation.getStreetNum());
        info.setLatitude(mLocation.getLatitude());
        info.setLongitude(mLocation.getLongitude());
        return info;
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置amap的属性
     */
    private void setUpMap() {
        //设置定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0);// 设置圆形的边框粗细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    /**
     * 检查并申请权限
     */
    private void checkPermission() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ||
                PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else
            initMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initMap();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
            mLocation = aMapLocation;//引用该对象，在用户点击√提交时再解析
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置需要地址信息
            mLocationOption.setNeedAddress(true);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒
            mLocationOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 解析POI搜索的结果
     *
     * @param poiResult
     * @param i         返回的结果码 1000表示成功
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i != 1000) {
            Toast.makeText(this, "搜索失败", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<PoiItem> results = poiResult.getPois();
        results.get(0).getSnippet();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (i != 1000)
            return;
        String[] addr = new String[list.size()];
        for (int k = 0; k < list.size(); k++) {
            addr[k] = list.get(k).getAddress();
        }
        searchView.setSuggestions(addr);
        searchView.showSuggestions();
        Log.d("MAP_SEARCH", "suggestions:" + Arrays.toString(addr));
    }


    private class NearByRecordAsyncTask extends AsyncTask<Void, Integer, List<Record>> {
        @Override
        protected List<Record> doInBackground(Void... voids) {
            while(mLocation==null) {
                try {
                    Thread.sleep(1500);//如果没获取到位置 先睡1.5s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                return mRecordModel.getNearbyRecordInfoList(mLocation.getLatitude(),mLocation.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        /**
         * 根据获取的Record把标记添加到地图上
         * @param records
         */
        @Override
        protected void onPostExecute(List<Record> records) {
            for (Record r : records) {
                aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(r.getLatitude(), r.getLongitude()))
                        .title(types[r.getType()])
                        .snippet(r.getDescription()));//添加描述
            }
        }
    }

}
