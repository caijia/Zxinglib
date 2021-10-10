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
import com.hualu.zlib.activity.CaptureActivity;
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
    //Intent i = new Intent(this, CaptureActivity.class);
    //startActivityForResult(i, 200);
    requestLocationPermissions();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 200) {
      tvResult.setText(data.getStringExtra(CaptureActivity.SCAN_RESULT));
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
      }, REQUEST_LOCATION);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_LOCATION: {
        boolean grantedCameraPermission = isGrantedCameraPermission(grantResults);
        if (grantedCameraPermission) {
          BdLocationManager.newInstance().getOnceLocation(getApplication(),
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