package com.jraska.github.client

import com.jraska.github.client.android.FakeCoreAndroidModule
import com.jraska.github.client.http.FakeHttpModule
import dagger.Module

@Module(
  includes = [
    FakeCoreModule::class,
    FakeCoreAndroidModule::class,
    FakeHttpModule::class,
    FakeWebLinkModule::class
  ]
)
object FakeUnitTestModules
