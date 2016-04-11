package com.jraska.github.client.network;

import android.content.Context;
import com.jraska.github.client.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module
public class NetworkModule {
  @Provides @PerApp OkHttpClient provideOkHttpClient() {
    HttpLoggingInterceptor.Logger logger = message -> Timber.tag("OkHttp").d(message);
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();

    return client;
  }
}
