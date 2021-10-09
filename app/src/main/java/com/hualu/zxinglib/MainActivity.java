package com.hualu.zxinglib;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import com.hualu.zlib.activity.CaptureActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void scan(View view) {
    Intent i = new Intent(this, CaptureActivity.class) ;
    startActivity(i);
  }
}