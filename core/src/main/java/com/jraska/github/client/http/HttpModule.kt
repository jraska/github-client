package com.jraska.github.client.http

import android.content.Context
import com.jraska.github.client.AndroidAppVersion
import com.jraska.github.client.AppVersion
import com.jraska.github.client.core.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import javax.inject.Singleton

@Module
object HttpModule {
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .validateEagerly(BuildConfig.DEBUG)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  internal fun provideOkHttpClient(
    context: Context,
    appHeadersInterceptor: AppCommonHeadersInterceptor
  ): OkHttpClient {
    val builder = OkHttpClient.Builder()

    builder.addInterceptor(appHeadersInterceptor)

    if (BuildConfig.DEBUG) {
      val loggingInterceptor =
        HttpLoggingInterceptor { message -> Timber.tag("Network").v(message) }
      loggingInterceptor.level = Level.BASIC
      builder.addNetworkInterceptor(loggingInterceptor)
    }


    val cacheDir = File(context.cacheDir, "network")
    val cache = Cache(cacheDir, 1024 * 1024 * 4)
    builder.cache(cache)

    return builder.build()
  }

  @Provides
  @Singleton
  internal fun appHeadersInterceptor(appVersion: AppVersion): AppCommonHeadersInterceptor {
    return AppCommonHeadersInterceptor(appVersion)
  }

  @Provides
  internal fun appVersion(context: Context): AppVersion {
    return AndroidAppVersion(context)
  }
}
