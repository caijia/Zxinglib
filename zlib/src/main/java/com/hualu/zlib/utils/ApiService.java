package com.hualu.zlib.utils;

import com.hualu.zlib.delegate.DiseaseResult;
import com.hualu.zlib.delegate.RestResult;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

  @Multipart
  @POST
  Call<RestResult> uploadFile(
      @Url String url,
      @Part MultipartBody.Part file
  );
}
