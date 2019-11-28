package com.jraska.github.client.http

import com.jraska.github.client.HasRetrofit
import dagger.BindsInstance
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File

@Http
@Component(modules = [HttpModule::class])
interface HttpComponent : HasRetrofit {
  override fun retrofit(): Retrofit

  @Component.Builder
  interface Builder {
    fun build(): HttpComponent

    @BindsInstance fun cacheDir(cacheDir: File): Builder
    @BindsInstance fun logger(logger: HttpLoggingInterceptor.Logger): Builder
  }
}
