package com.jraska.github.client

import dagger.Module
import dagger.Provides

@Module
object FakeConfigModule {
  @Provides
  fun config(): Config = FakeCoreModule.config
}
