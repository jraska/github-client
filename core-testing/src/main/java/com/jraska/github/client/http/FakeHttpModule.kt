package com.jraska.github.client.http

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FakeHttpModule {

  @Provides
  @Singleton
  fun provideRetrofit() = HttpTest.retrofit()
}
