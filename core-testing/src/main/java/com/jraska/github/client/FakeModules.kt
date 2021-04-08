package com.jraska.github.client

import com.jraska.github.client.http.FakeHttpModule
import dagger.Module

@Module(
  includes = arrayOf(
    FakeCoreModule::class,
    FakeCoreAndroidModule::class,
    FakeConfigModule::class,
    FakeHttpModule::class,
  )
)
object FakeModules
