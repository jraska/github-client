package com.jraska.github.client.http;

import com.jraska.github.client.common.AppBuildConfig;
import com.jraska.github.client.logging.VerboseLogger;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class HttpModule {
  @Provides @Http
  public static Retrofit provideRetrofit(OkHttpClient okHttpClient, AppBuildConfig config) {
    return new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .validateEagerly(config.debug)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  @Provides @Http
  public static OkHttpClient provideOkHttpClient(@Http File cacheDir, @Http VerboseLogger logger, AppBuildConfig config) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    if (config.debug) {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger::v)
          .setLevel(Level.BASIC);
      builder.addInterceptor(loggingInterceptor);
    }

    Cache cache = new Cache(cacheDir, 1024 * 1024 * 4);
    builder.cache(cache);

    return builder.build();
  }
}
