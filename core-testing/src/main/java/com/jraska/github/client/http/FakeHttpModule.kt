package com.jraska.github.client.http

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class FakeHttpModule {

  @Provides
  @Singleton
  fun provideRetrofit() = HttpTest.retrofit()

  @Provides
  fun provideOkHttp(retrofit: Retrofit) = retrofit.callFactory() as OkHttpClient
}
