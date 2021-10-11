package com.hualu.zlib.utils;

import android.app.Application;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * <p>
 * BdLocationManager
 * </p>
 *
 * @author cai.jia
 * @since 2021-10-10 12:03
 */
public class BdLocationManager {

  public  static BdLocationManager newInstance() {
    return new BdLocationManager();
  }

  public BdLocationManager() {

  }

  public LocationClient mLocationClient = null;

  public void getOnceLocation(Application application, String type, BdLocationListener listener) {
    mLocationClient = new LocationClient(application);
    mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
      @Override public void onReceiveLocation(BDLocation bdLocation) {
        if (listener != null) {
          listener.onBdLocation(
              bdLocation == null ? -1 : bdLocation.getLatitude(),
              bdLocation == null ? -1 : bdLocation.getLongitude()
          );
        }
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient.stop();
      }
    });
    LocationClientOption option = new LocationClientOption();
    option.setScanSpan(1000);
    //可选，设置发起定位请求的间隔，int类型，单位ms
    //如果设置为0，则代表单次定位，即仅定位一次，默认为0

    option.setOpenGps(true);
    //可选，设置是否使用gps，默认false
    //使用高精度和仅用设备两种定位模式的，参数必须设置为true

    //可选，设置返回经纬度坐标类型，默认GCJ02
    //GCJ02：国测局坐标；
    //BD09ll：百度经纬度坐标；
    //BD09：百度墨卡托坐标；
    //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
    option.setCoorType(type);
    option.setOnceLocation(true);
    option.setEnableSimulateGps(false);
    mLocationClient.setLocOption(option);
    mLocationClient.start();
  }

  public interface BdLocationListener {

    void onBdLocation(double latitude, double longitude);

  }
}
