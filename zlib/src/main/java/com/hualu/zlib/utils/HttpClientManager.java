package com.hualu.zlib.utils;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Created by cai.jia on 2017/9/1 0001. */
public class HttpClientManager {

  private static final String CACHE_DIR = "onHttpCache";
  private static final String BASE_URL = "http://www.test.com";
  private static volatile HttpClientManager instance;
  private Retrofit retrofit;
  private OkHttpClient client;

  private HttpClientManager() {
    client =
        new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),
                (X509TrustManager) SSLSocketClient.getTrustManager()[0]) // 配置
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier()) // 配置
            .build();

    retrofit =
        new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
  }

  public static HttpClientManager getInstance() {
    if (instance == null) {
      synchronized (HttpClientManager.class) {
        if (instance == null) {
          instance = new HttpClientManager();
        }
      }
    }
    return instance;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public OkHttpClient getOkHttpClient() {
    return client;
  }

  public <T> T create(final Class<T> service) {
    return retrofit.create(service);
  }
}
