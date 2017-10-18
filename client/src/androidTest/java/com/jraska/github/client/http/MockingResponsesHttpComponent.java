package com.jraska.github.client.http;

import android.support.test.espresso.IdlingRegistry;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class MockingResponsesHttpComponent implements HttpComponent {
  private static final String NETWORK_ERROR_MESSAGE = "You are trying to do network requests in tests you naughty developer!";

  private final Retrofit retrofit;

  private MockingResponsesHttpComponent(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  @Override public Retrofit retrofit() {
    return retrofit;
  }

  public static MockingResponsesHttpComponent create() {
    RxHttpIdlingResourceFactory idlingResourceFactory = RxHttpIdlingResourceFactory.create();
    IdlingRegistry.getInstance().register(idlingResourceFactory.idlingResource());

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .addCallAdapterFactory(idlingResourceFactory)
      .addConverterFactory(GsonConverterFactory.create())
      .client(failingClient())
      .build();

    return new MockingResponsesHttpComponent(retrofit);
  }

  private static OkHttpClient failingClient() {
    return new OkHttpClient.Builder()
      .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
      .addInterceptor(new MockResponsesInterceptor())
      .addInterceptor(chain -> {
        throw new UnsupportedOperationException(NETWORK_ERROR_MESSAGE);
      })
      .build();
  }
}
