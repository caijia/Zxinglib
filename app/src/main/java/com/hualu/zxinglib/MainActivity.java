package com.hualu.zxinglib;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.hualu.zlib.activity.DiseaseListActivity;
import com.hualu.zlib.activity.SignResultActivity;
import com.hualu.zlib.utils.BdLocationManager;

public class MainActivity extends Activity {

  private static final int REQUEST_LOCATION = 201;
  private TextView tvResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tvResult = findViewById(R.id.tvResult);

  }

  public void scan(View view) {
    //Intent i =  SignResultActivity.getIntent(this, "大埠河大桥(K289+678)", true,
    //        "何炜俊", "2021-12-17", "广东冠粤路桥有限公司",
    //    "2021-12-02", "何炜俊", "签到成功");
    //startActivityForResult(i, 2);
    //requestLocationPermissions();

    Intent i = DiseaseListActivity.getIntent(
        this,
        "https://125.89.150.12:7001/qrcode/app/struct/getDssInfo",
        "C415EE40-AD0A-4BC0-B702-22E65CDFBD37",
        "ny-admin","QL"
    );
    startActivity(i);
  }



  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK || data == null || data.getExtras() == null) {
      return;
    }

    Bundle extras = data.getExtras();
    String eventType = extras.getString(SignResultActivity.EVENT_TYPE);
    if (SignResultActivity.ADD_CHECKER_LIST.equals(eventType)) {

    } else if (SignResultActivity.GOTO_DISEASE_LIST.equals(eventType)) {

    }
  }

  private void requestLocationPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[] {
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_WIFI_STATE,
          Manifest.permission.ACCESS_NETWORK_STATE,
          Manifest.permission.CHANGE_WIFI_STATE,
          Manifest.permission.READ_PHONE_STATE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
      }, REQUEST_LOCATION);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_LOCATION: {
        boolean grantedCameraPermission = isGrantedCameraPermission(grantResults);
        if (grantedCameraPermission) {
          BdLocationManager.newInstance().getOnceLocation(getApplication(), "WGS84",
              (latitude, longitude) -> {
                Log.e("hh", latitude + "");
                Log.e("hh", longitude + "");
              });
        }
        break;
      }
    }
  }

  private boolean isGrantedCameraPermission(int[] grantResults) {
    if (grantResults.length <= 0) {
      return false;
    }

    boolean allow = true;
    for (int grantResult : grantResults) {
      if (grantResult != PackageManager.PERMISSION_GRANTED) {
        allow = false;
      }
    }
    return allow;
  }
}