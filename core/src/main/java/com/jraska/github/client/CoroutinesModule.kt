package com.jraska.github.client

import com.jraska.github.client.coroutines.AppDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object CoroutinesModule {
  @Provides
  @Singleton
  fun dispatchers(): AppDispatchers {
    return AppDispatchers(
      Dispatchers.Main,
      Dispatchers.IO
    )
  }
}
