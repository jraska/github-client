package com.jraska.github.client.http

import android.content.Context
import com.jraska.github.client.core.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

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
      .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
      .build()
  }

  @Provides
  @Singleton
  internal fun provideOkHttpClient(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
      val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("Network").v(message) }
      loggingInterceptor.level = Level.BASIC
      builder.addInterceptor(loggingInterceptor)
    }

    builder.addInterceptor {
      Timber.d("Network thread %s", Thread.currentThread().name)
      it.proceed(it.request())
    }

    val cacheDir = File(context.cacheDir, "network")
    val cache = Cache(cacheDir, 1024 * 1024 * 4)
    builder.cache(cache)

    return builder.build()
  }
}
