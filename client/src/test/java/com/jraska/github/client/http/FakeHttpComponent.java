package com.jraska.github.client.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FakeHttpComponent implements HttpComponent {
  private static final String NETWORK_ERROR_MESSAGE = "You are trying to do network requests in tests you naughty developer!";

  private final Retrofit retrofit;

  private FakeHttpComponent(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  @Override public Retrofit retrofit() {
    return retrofit;
  }

  public static FakeHttpComponent create() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://localhost")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(failingClient())
        .build();

    return new FakeHttpComponent(retrofit);
  }

  private static OkHttpClient failingClient() {
    return new OkHttpClient.Builder()
        .addInterceptor(chain -> {
          throw new UnsupportedOperationException(NETWORK_ERROR_MESSAGE);
        })
        .build();
  }
}
