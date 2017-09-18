package com.jiyun.asus.shixunlianxi;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.jiyun.asus.shixunlianxi.bean.Bean;
import com.jiyun.asus.shixunlianxi.overlay.DrivingRouteOverlay;
import com.jiyun.asus.shixunlianxi.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

public class TuActivity extends AppCompatActivity implements View.OnClickListener, UMShareListener {

    private MapView mMapView;
    private Button mFen;
    private MyLocationStyle myLocationStyle;
    private AMap map;
    private GeocodeSearch geocoderSearch;
    private Intent intent;
    private List<Bean.PositionBean> mList = new ArrayList<>();
    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu);
        initView();
        mMapView.onCreate(savedInstanceState);
        initIntent();
        initMapView();
        initShu();
        initData();
    }

    private void initShu() {
        String city = mList.get(0).getCity();
        String substring = city.substring(0, 2);
        String substring1 = city.substring(city.length()-2,city.length());
        Toast.makeText(this, ""+substring1, Toast.LENGTH_SHORT).show();
        query = new PoiSearch.Query(substring, "", substring1);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        map.clear();

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                for (PoiItem item : poiResult.getPois()) {
                    LatLonPoint latLonPoint = item.getLatLonPoint();
                    double latitude = latLonPoint.getLatitude();
                    double longitude = latLonPoint.getLongitude();
                    String adName = item.getAdName();
                    Log.e("TAG", adName);
                    addMarker(item);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }
    private void addMarker(PoiItem item) {
        MarkerOptions options = new MarkerOptions();
        LatLonPoint latLonPoint = item.getLatLonPoint();
        LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        options.position(latLng);
        options.title(item.getTitle());
        options.snippet(item.getSnippet());
        map.addMarker(options);
    }

    private void initIntent() {
        intent = getIntent();
        Bean.PositionBean list = intent.getParcelableExtra("list");
//        Toast.makeText(this, ""+list.getCity(), Toast.LENGTH_SHORT).show();
        mList.add(list);
    }

    private void initMapView() {
        map = mMapView.getMap();
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mMapView);
        mFen = (Button) findViewById(R.id.mFen);

        mFen.setOnClickListener(this);
//        map.setOnMarkerClickListener(this);
//        map.setOnInfoWindowClickListener(this);
//        map.setInfoWindowAdapter(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mFen:
                new ShareAction(TuActivity.this)
                        .setPlatform(SHARE_MEDIA.SINA)//传入平台
                        .withText("北京市昌平区吉利大学")//分享内容
                        .setCallback(this)//回调监听器
                        .share();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void initData() {
        map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                Location myLocation = map.getMyLocation();
                initLuJingGuiHua(myLocation, marker);
                return true;
            }

            private void initLuJingGuiHua(Location location, Marker marker) {
                final RouteSearch routeSearch = new RouteSearch(TuActivity.this);
                routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
                    @Override
                    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

                    }

                    @Override
                    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
                        map.clear();// 清理地图上的所有覆盖物
                        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                            if (result != null && result.getPaths() != null) {
                                if (result.getPaths().size() > 0) {
                                    DriveRouteResult mDriveRouteResult = result;
                                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                                            .get(0);
                                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                            TuActivity.this, map, drivePath,
                                            mDriveRouteResult.getStartPos(),
                                            mDriveRouteResult.getTargetPos(), null);
                                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                                    drivingRouteOverlay.removeFromMap();
                                    drivingRouteOverlay.addToMap();
                                    drivingRouteOverlay.zoomToSpan();
                                } else if (result != null ) {
                                    ToastUtil.show(TuActivity.this, R.string.no_result);
                                }

                            } else {
//                                ToastUtil.show(AtlasActivity.this, "对不起，没有搜索到相关数据！");
                            }
                        } else {
//                            ToastUtil.showerror(AtlasActivity.this.getApplicationContext(), errorCode);
                        }
                    }

                    @Override
                    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

                    }

                    @Override
                    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

                    }
                });
                LatLonPoint pointA = new LatLonPoint(location.getLatitude(), location.getLongitude());
                LatLng position = marker.getPosition();
                double latitude = position.latitude;
                String latitude1 = mList.get(0).getLatitude();
                String longitude = mList.get(0).getLongitude();
                double v = Double.parseDouble(latitude1);
                double v1 = Double.parseDouble(longitude);
                LatLonPoint pointB = new LatLonPoint(position.latitude, position.longitude);
                RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(pointA, pointB);
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fat, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SAVE_MONEY_SHORTEST, null, null, "");
                routeSearch.calculateDriveRouteAsyn(query);
            }
        });
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
