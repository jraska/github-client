package com.jraska.github.client.http

import dagger.Component
import retrofit2.Retrofit

@Http
@Component(modules = arrayOf(HttpDependenciesModule::class, HttpModule::class))
interface HttpComponent {
  fun retrofit(): Retrofit
}
