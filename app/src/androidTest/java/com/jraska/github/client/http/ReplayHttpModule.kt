package com.jraska.github.client.http

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ReplayHttpModule {

  private val retrofit by lazy { HttpTest.retrofit() }

  @Provides
  fun retrofit(): Retrofit {
    return retrofit
  }
}
