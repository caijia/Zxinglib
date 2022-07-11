package com.hualu.zlib.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.caijia.adapterdelegate.delegate.LoadMoreDelegate;
import com.hualu.zlib.R;
import com.hualu.zlib.delegate.Disease;
import com.hualu.zlib.delegate.DiseaseAdapter;
import com.hualu.zlib.delegate.DiseaseResult;
import com.hualu.zlib.utils.ApiService;
import com.hualu.zlib.utils.HttpClientManager;
import com.hualu.zlib.utils.StateView;
import com.hualu.zlib.utils.ThreadSwitchHelper;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import retrofit2.Call;

public class DiseaseListActivity extends Activity {

  private static final String EXTRA_DISEASE_URL = "extra:DiseaseUrl";
  private static final String EXTRA_STRUCT_ID = "extra:structId";
  private static final String EXTRA_USER_CODE = "extra:userCode";

  public static Intent getIntent(Context context, String url, String structId, String userCode) {
    Intent i = new Intent(context, DiseaseListActivity.class);
    i.putExtra(EXTRA_DISEASE_URL, url);
    i.putExtra(EXTRA_STRUCT_ID, structId);
    i.putExtra(EXTRA_USER_CODE, userCode);
    return i;
  }

  private String url;
  private String structId;

  private String userCode;

  private void handleIntent() {
    Intent intent = getIntent();
    if (intent != null && intent.getExtras() != null) {
      url = intent.getExtras().getString(EXTRA_DISEASE_URL);
      structId = intent.getExtras().getString(EXTRA_STRUCT_ID);
      userCode = intent.getExtras().getString(EXTRA_USER_CODE);
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_disease_list);
    handleIntent();
    setupView();
    setupRecyclerView();
    getDiseaseList();
  }

  private StateView stateView;

  private String startDate;
  private String endDate;

  private void setupView() {
    stateView = findViewById(R.id.stateView);
    tvStartDate = findViewById(R.id.tvStartDate);
    tvEndDate = findViewById(R.id.tvEndDate);
    findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });

    Calendar calendar = Calendar.getInstance();
    int currYear = calendar.get(Calendar.YEAR);
    int currMonth = calendar.get(Calendar.MONTH);
    int currDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    tvStartDate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
          new DatePickerDialog(DiseaseListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
              startDate = String.format("%d-%d-%d", i, i1 + 1, i2);
              tvStartDate.setText(startDate);
            }
          }, currYear, currMonth,currDayOfMonth).show();
        }
    });

    tvEndDate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        new DatePickerDialog(DiseaseListActivity.this, new DatePickerDialog.OnDateSetListener() {
          @Override public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            endDate = String.format("%d-%d-%d", i, i1 + 1, i2);
            tvEndDate.setText(endDate);
          }
        }, currYear, currMonth,currDayOfMonth).show();
      }
    });

    findViewById(R.id.tvSearch).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        page = 1;
        getDiseaseList();
      }
    });
  }

  private DiseaseAdapter diseaseAdapter;
  private RecyclerView recyclerView;

  private TextView tvStartDate;

  private TextView tvEndDate;

  private void setupRecyclerView() {
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    diseaseAdapter = new DiseaseAdapter(new LoadMoreDelegate.OnLoadMoreDelegateListener() {
      @Override public void onLoadMore(RecyclerView recyclerView) {
        page++;
        getDiseaseList();
      }

      @Override public void onLoadMoreClickRetry() {

      }
    });
    recyclerView.setAdapter(diseaseAdapter);
  }

  private int page = 1;

  //structId=C415EE40-AD0A-4BC0-B702-22E65CDFBD37&type=noxy&userCode=ny-admin
  //    "cat" to cat.filterNull(),
  //    "accept" to accept.filterNull(),
  //    "lineType" to lineType.filterNull(),
  //    "findDate_S" to startDate.filterNull(),
  //    "findDate_E" to endDate.filterNull(),
  //    "startStake" to startStake.filterNull(),
  //    "endStake" to endStake.filterNull(),
  //    "commonDssType" to commonDssType.filterNull(),
  //    "kw" to keyword.filterNull(),
  //    "pageNo" to pageNo,
  //    "pageSize" to pageSize
  private void getDiseaseList() {
    if (page == 1) {
      stateView.showView(StateView.STATE_LOADING);
    }
    ApiService apiService = HttpClientManager.getInstance().getRetrofit().create(ApiService.class);
    Map<String, Object> params = new HashMap<>();
    params.put("structId", structId == null ? "" : structId);
    params.put("type", "noxy");
    params.put("userCode", userCode == null ? "" : userCode);
    params.put("pageNo", page);
    params.put("pageSize", 15);
    if (!TextUtils.isEmpty(startDate)) {
      params.put("findDate_S", startDate);
    }
    if (!TextUtils.isEmpty(endDate)) {
      params.put("findDate_E", endDate);
    }
    Call<DiseaseResult> call = apiService.getDiseaseResult(url, params);
    if (call != null) {
      new ThreadSwitchHelper<DiseaseResult>()
          .task(new Callable<DiseaseResult>() {
            @Override public DiseaseResult call() throws Exception {
              return call.execute().body();
            }
          })
          .execute(new ThreadSwitchHelper.Callback<DiseaseResult>() {
            @Override public void onSuccess(DiseaseResult result) {
              if (result != null) {
                List<Disease> data = result.getData();
                diseaseAdapter.refreshOrLoadMoreItems(page, data);
                if (page == 1 && data == null || data.isEmpty()) {
                  stateView.showView(StateView.STATE_RETRY);
                } else {
                  stateView.hideAllView(recyclerView);
                }
              }
            }

            @Override public void onFailure(String error) {
              if (page == 1) {
                stateView.showView(StateView.STATE_RETRY);
              }
            }
          });
    }
  }
}
