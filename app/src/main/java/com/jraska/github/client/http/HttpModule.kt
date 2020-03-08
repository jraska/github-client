package com.jraska.github.client.http

import com.jraska.github.client.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class HttpModule {
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .validateEagerly(BuildConfig.DEBUG)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(cacheDir: File, logger: HttpLoggingInterceptor.Logger): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
      val loggingInterceptor = HttpLoggingInterceptor(logger).apply { level = Level.BASIC }
      builder.addInterceptor(loggingInterceptor)
    }

    val cache = Cache(cacheDir, 1024 * 1024 * 4)
    builder.cache(cache)

    return builder.build()
  }
}
