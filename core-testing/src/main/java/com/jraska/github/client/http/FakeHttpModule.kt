package com.jraska.github.client.http

import dagger.Module
import dagger.Provides
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
class FakeHttpModule {

  @Provides
  @Singleton
  fun provideRetrofit() = HttpTest.retrofit()
}
