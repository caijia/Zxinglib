package com.hualu.zlib.utils;

import com.hualu.zlib.delegate.DiseaseResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * <p>
 * ApiService
 * </p>
 *
 * @author cai.jia
 * @since 2022-07-11 11:27
 */
public interface ApiService {

  @GET
  Call<DiseaseResult> getDiseaseResult(
      @Url String url,
      @QueryMap Map<String, Object> params
  );
}
