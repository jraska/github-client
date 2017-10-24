package com.jraska.github.client.http

import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.logging.VerboseLogger
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

@Module
object HttpModule {
  @Provides
  @Http
  fun provideRetrofit(okHttpClient: OkHttpClient, config: AppBuildConfig): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .validateEagerly(config.debug)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }

  @Provides
  @Http
  fun provideOkHttpClient(@Http cacheDir: File, @Http logger: VerboseLogger, config: AppBuildConfig): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (config.debug) {
      val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { logger.v(it) })
        .setLevel(Level.BASIC)
      builder.addInterceptor(loggingInterceptor)
    }

    val cache = Cache(cacheDir, 1024 * 1024 * 4)
    builder.cache(cache)

    return builder.build()
  }
}
