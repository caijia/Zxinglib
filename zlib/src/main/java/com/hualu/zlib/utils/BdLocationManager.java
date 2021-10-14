package com.hualu.zlib.utils;

import android.app.Application;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * <p>
 * BdLocationManager
 * </p>
 *
 * @author cai.jia
 * @since 2021-10-10 12:03
 */
public class BdLocationManager {

  public static final double pi = 3.1415926535897932384626;//圆周率
  public static final double a = 6378245.0;//克拉索夫斯基椭球参数长半轴a
  public static final double ee = 0.00669342162296594323;//克拉索夫斯基椭球参数第一偏心率平方
  public AMapLocationClient mLocationClient = null;

  public BdLocationManager() {

  }

  public static BdLocationManager newInstance() {
    return new BdLocationManager();
  }

  public void getOnceLocation(Application application, String type, BdLocationListener listener) {
    mLocationClient = new AMapLocationClient(application);
    mLocationClient.setLocationListener(new AMapLocationListener() {
      @Override public void onLocationChanged(AMapLocation aMapLocation) {
        if (listener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0) {
          double lat = aMapLocation.getLatitude();
          double lng = aMapLocation.getLongitude();
          LocateInfo locateInfo = gcj02ToWgs84(lat, lng);
          if (locateInfo != null) {
            listener.onBdLocation(locateInfo.Latitude, locateInfo.longitude);
          } else {
            listener.onBdLocation(-1, -1);
          }
        } else {
          listener.onBdLocation(-1, -1);
        }
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient.stopLocation();
      }
    });
    AMapLocationClientOption option = new AMapLocationClientOption();
    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    option.setMockEnable(false);
    option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
    if (null != mLocationClient) {
      mLocationClient.setLocationOption(option);
      //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
      mLocationClient.stopLocation();
      mLocationClient.startLocation();
    }
  }

  //从高德转到GPS
  public LocateInfo gcj02ToWgs84(double lat, double lon) {
    LocateInfo info = new LocateInfo();
    LocateInfo gps = transform(lat, lon);
    double lontitude = lon * 2 - gps.getLongitude();
    double latitude = lat * 2 - gps.getLatitude();
    info.setChina(gps.isChina());
    info.setLatitude(latitude);
    info.setLongitude(lontitude);
    return info;
  }

  //转换
  private LocateInfo transform(double lat, double lon) {
    LocateInfo info = new LocateInfo();
    double dLat = transformLat(lon - 105.0, lat - 35.0);
    double dLon = transformLon(lon - 105.0, lat - 35.0);
    double radLat = lat / 180.0 * pi;
    double magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    double sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    double mgLat = lat + dLat;
    double mgLon = lon + dLon;
    info.setChina(true);
    info.setLatitude(mgLat);
    info.setLongitude(mgLon);

    return info;
  }

  //转换纬度所需
  private double transformLat(double x, double y) {
    double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
        + 0.2 * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
    return ret;
  }

  //转换经度所需
  private double transformLon(double x, double y) {
    double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
        * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
    return ret;
  }

  public interface BdLocationListener {

    void onBdLocation(double latitude, double longitude);
  }

  public static class LocateInfo {
    /**
     * 经度
     */
    private double longitude;
    /**
     * 维度
     */
    private double Latitude;
    /**
     * 是否在中国
     */
    private boolean isChina;

    public double getLongitude() {
      return longitude;
    }

    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

    public double getLatitude() {
      return Latitude;
    }

    public void setLatitude(double latitude) {
      Latitude = latitude;
    }

    public boolean isChina() {
      return isChina;
    }

    public void setChina(boolean china) {
      isChina = china;
    }
  }
}
