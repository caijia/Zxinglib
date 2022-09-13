package com.hualu.zlib.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hualu.zlib.R;
import com.hualu.zlib.delegate.RestResult;
import com.hualu.zlib.response.RestResultVo;
import com.hualu.zlib.utils.ApiService;
import com.hualu.zlib.utils.HttpClientManager;
import com.hualu.zlib.utils.ImageCompressor;
import com.hualu.zlib.utils.ThreadSwitchHelper;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.support.v4.content.FileProvider.getUriForFile;

public class SignResultActivity extends Activity {

  private TextView tvBack;
  private TextView tvStructName;
  private ImageView ivStatusImage;
  private TextView tvStatusMessage;
  private TextView tvSignPerson;
  private TextView tvSignTime;
  private TextView tvSignCompany;
  private TextView tvPreSignTime;
  private TextView tvPreSignPerson;
  private TextView tvAdd;
  private TextView tvDiseaseHis;
  private ImageView ivCamera;
  private FrameLayout flCamera;

  public static final String ADD_CHECKER_LIST = "add_checker_list";
  public static final String GOTO_DISEASE_LIST = "goto_disease_list";
  public static final String EVENT_TYPE = "event_type";
  private static final String STRUCT_NAME = "struct_name";
  private static final String IS_SUCCESSFUL = "is_successful";
  private static final String SIGN_PERSON = "sign_person";
  private static final String SIGN_TIME = "sign_time";
  private static final String SIGN_COMPANY = "sign_company";
  private static final String PRE_SIGN_TIME = "pre_sign_time";
  private static final String PRE_SIGN_PERSON = "pre_sign_person";
  private static final String SIGN_MESSAGE = "sign_message";
  private static final String UPLOAD_FILE_URL = "extra:uploadFileUrl";
  private static final String UPLOAD_DC_CODE = "extra:dccode";
  private static final String ATTENTION_ID = "extra:attentionId";

  public static Intent getIntent(Activity activity, String structName, boolean isSuccessful,
      String signPerson, String signTime, String signCompany, String preSignTime,
      String preSignPerson, String signMessage, String uploadUrl, String uploadDccode,
      String attentionId) {
    Intent i = new Intent(activity, SignResultActivity.class);
    i.putExtra(STRUCT_NAME, structName);
    i.putExtra(IS_SUCCESSFUL, isSuccessful);
    i.putExtra(SIGN_PERSON, signPerson);
    i.putExtra(SIGN_TIME, signTime);
    i.putExtra(SIGN_COMPANY, signCompany);
    i.putExtra(PRE_SIGN_TIME, preSignTime);
    i.putExtra(PRE_SIGN_PERSON, preSignPerson);
    i.putExtra(SIGN_MESSAGE, signMessage);
    i.putExtra(UPLOAD_DC_CODE, uploadDccode);
    i.putExtra(UPLOAD_FILE_URL, uploadUrl);
    i.putExtra(ATTENTION_ID, attentionId);
    return i;
  }

  private String uploadFileUrl;

  private String uploadDccode;

  private String attentionId;

  public static final String FILE_ID = "FILE_ID";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_result);

    Bundle extras = getIntent().getExtras();

    tvBack = findViewById(R.id.tv_back);
    flCamera = findViewById(R.id.fl_camera);
    ivCamera = findViewById(R.id.iv_camara);
    tvStructName = findViewById(R.id.tv_struct_name);
    ivStatusImage = findViewById(R.id.iv_sign_status_image);
    tvStatusMessage = findViewById(R.id.tv_sign_status_message);
    tvSignPerson = findViewById(R.id.tv_sign_person);
    tvSignTime = findViewById(R.id.tv_sign_time);
    tvSignCompany = findViewById(R.id.tv_sign_company);
    tvPreSignTime = findViewById(R.id.tv_pre_sign_time);
    tvPreSignPerson = findViewById(R.id.tv_pre_sign_person);
    tvAdd = findViewById(R.id.tv_add);
    tvDiseaseHis = findViewById(R.id.tv_disease_his);

    tvBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });

    tvAdd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (saveImagePath == null || !new File(saveImagePath).exists()) {
          Toast.makeText(getApplicationContext(), "请先上传现场照片", Toast.LENGTH_LONG).show();
        } else {
          Intent i = new Intent();
          i.putExtra(EVENT_TYPE, ADD_CHECKER_LIST);
          i.putExtra(FILE_ID, uploadFileId);
          setResult(RESULT_OK, i);
          onBackPressed();
        }
      }
    });

    tvDiseaseHis.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent i = new Intent();
        i.putExtra(EVENT_TYPE, GOTO_DISEASE_LIST);
        setResult(RESULT_OK, i);
        onBackPressed();
      }
    });

    flCamera.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        reqCameraPermissions();
      }
    });

    if (null != extras) {
      String structName = extras.getString(STRUCT_NAME);
      boolean isSuccessful = extras.getBoolean(IS_SUCCESSFUL);
      String signPerson = extras.getString(SIGN_PERSON);
      String signTime = extras.getString(SIGN_TIME);
      String signCompany = extras.getString(SIGN_COMPANY);
      String preSignTime = extras.getString(PRE_SIGN_TIME);
      String preSignPerson = extras.getString(PRE_SIGN_PERSON);
      String signMessage = extras.getString(SIGN_MESSAGE);
      uploadFileUrl = extras.getString(UPLOAD_FILE_URL);
      uploadDccode = extras.getString(UPLOAD_DC_CODE);
      attentionId = extras.getString(ATTENTION_ID);

      tvStructName.setText(orEmpty(structName));
      tvSignPerson.setText(orEmpty(signPerson));
      tvSignTime.setText(orEmpty(signTime));
      tvSignCompany.setText(orEmpty(signCompany));
      tvPreSignTime.setText(orEmpty(preSignTime));
      tvPreSignPerson.setText(orEmpty(preSignPerson));

      tvStatusMessage.setText(orEmpty(signMessage));
      if (isSuccessful) {
        ivStatusImage.setImageResource(R.drawable.sign_ok);
      } else {
        ivStatusImage.setImageResource(R.drawable.sign_error);
      }
    }
  }

  private static final int REQ_CAMERA = 201;
  private String saveImagePath = null;

  private void reqCameraPermissions() {
    requestLocationPermissions();
  }

  private void takePicture() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    File dir = getExternalCacheDir();
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir, UUID.randomUUID().toString() + ".jpg");
    saveImagePath = file.getAbsolutePath();
    int sdkInt = Build.VERSION.SDK_INT;
    Uri uri;
    if (sdkInt < Build.VERSION_CODES.N) {
      uri = Uri.fromFile(file);
    } else {
      uri = getUriForFile(this, "com.caijia.scancamera.provider", file);
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    startActivityForResult(intent, REQ_CAMERA);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    if (requestCode == REQ_CAMERA) {
      uploadFileId = null;
      //显示图片
      Bitmap bitmap = ImageCompressor.getInstance()
          .compressBitmap(saveImagePath, Bitmap.Config.RGB_565, 1080, 1440);
      if (bitmap != null) {
        ImageCompressor.getInstance().saveBitmapToFile(this, bitmap, new File(saveImagePath), 100);
      }
      uploadImage(new File(saveImagePath));
      loadImage(ivCamera, saveImagePath, R.drawable.ic_scan_camera, 0, 0);
    }
  }

  private void uploadImage(File file) {
    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.show();
    ApiService apiService = HttpClientManager.getInstance().getRetrofit().create(ApiService.class);
    String url = uploadFileUrl + "/mongo/uploadFile?dccode=" + uploadDccode;
    RequestBody requestBody =
        RequestBody.create(MediaType.parse("application/octet-stream"), file);
    MultipartBody.Part part = null;
    try {
      part =
          MultipartBody.Part.createFormData(
              "file", URLEncoder.encode(file.getName(), "utf-8"), requestBody);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    Call<RestResult> call = apiService.uploadFile(url, part);
    if (call != null) {
      new ThreadSwitchHelper<RestResult>()
          .task(new Callable<RestResult>() {
            @Override public RestResult call() throws Exception {
              return call.execute().body();
            }
          })
          .execute(new ThreadSwitchHelper.Callback<RestResult>() {
            @Override public void onSuccess(RestResult result) {
              if (result != null && result.getFileId() != null) {
                uploadFileId = result.getFileId();
                setAttentionImage(attentionId, uploadFileId, progressDialog);
              } else {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
              }
            }

            @Override public void onFailure(String error) {
              progressDialog.dismiss();
              Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
            }
          });
    }
  }

  private void setAttentionImage(String attentionId, String uploadFileId,
      ProgressDialog progressDialog) {
    ApiService apiService = HttpClientManager.getInstance().getRetrofit().create(ApiService.class);
    String url = uploadFileUrl + "/qrcode/attendance/setAttentionImage";
    Map<String, Object> params = new HashMap<>();
    params.put("attendanceId", attentionId);
    params.put("fileId", uploadFileId);
    Call<RestResultVo<String>> call = apiService.sendAttendanceImage(url, params);
    if (call != null) {
      new ThreadSwitchHelper<RestResultVo<String>>()
          .task(new Callable<RestResultVo<String>>() {
            @Override public RestResultVo<String> call() throws Exception {
              return call.execute().body();
            }
          })
          .execute(new ThreadSwitchHelper.Callback<RestResultVo<String>>() {
            @Override public void onSuccess(RestResultVo<String> result) {
              String message = result.getStatus() == 200 ? "上传成功" : "上传失败";
              Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
              progressDialog.dismiss();
            }

            @Override public void onFailure(String error) {
              progressDialog.dismiss();
              Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
            }
          });
    }
  }

  private String uploadFileId = null;

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (requestCode == REQ_PERMISSION_CAMERA) {
      boolean grantedCameraPermission = isGrantedCameraPermission(grantResults);
      if (grantedCameraPermission) {
        takePicture();
      } else {
        Toast.makeText(getApplicationContext(), "没有拍照权限", Toast.LENGTH_LONG).show();
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
        break;
      }
    }
    return allow;
  }

  private static final int REQ_PERMISSION_CAMERA = 202;

  private void requestLocationPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA
      }, REQ_PERMISSION_CAMERA);
    }
  }

  public void loadImage(
      ImageView imageView, String url, @DrawableRes int defaultResId, int width, int height) {
    RequestOptions options =
        new RequestOptions()
            .priority(Priority.HIGH)
            .error(defaultResId)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(defaultResId);

    if (width > 0 && height > 0) {
      options = options.override(width, height);
    }

    RequestBuilder<Bitmap> builder =
        Glide.with(imageView.getContext()).asBitmap().load(url).apply(options);
    builder.into(imageView);
  }

  private String orEmpty(String s) {
    return s != null ? s : "";
  }
}
