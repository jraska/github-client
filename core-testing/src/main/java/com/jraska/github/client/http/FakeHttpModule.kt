package com.jraska.github.client.http

import dagger.Module
import dagger.Provides
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
class FakeHttpModule {
  val mockWebServer = MockWebServer()

  @Provides // will be singleton anyway as we have field
  fun mockWebServer() = mockWebServer

  @Provides
  @Singleton
  fun provideRetrofit() = HttpTest.retrofit(mockWebServer.url("/"))
}
