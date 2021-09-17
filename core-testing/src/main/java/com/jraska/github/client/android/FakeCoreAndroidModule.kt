package com.jraska.github.client.android

import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Fakes
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.ui.SnackbarDisplay
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import javax.inject.Singleton

@Module
object FakeCoreAndroidModule {
  @Provides
  @Singleton
  fun dispatchers(): AppDispatchers {
    Dispatchers.setMain(Dispatchers.Unconfined)

    return Fakes.unconfined()
  }

  @Provides
  @Singleton
  fun provideNavigator(): DeepLinkLauncher {
    return Fakes.recordingDeepLinkLauncher()
  }

  @Provides
  @Singleton
  internal fun provideFakeSnackbarDisplay(): FakeSnackbarDisplay {
    return FakeSnackbarDisplay()
  }

  @Provides
  internal fun provideSnackbarDisplay(fake: FakeSnackbarDisplay): SnackbarDisplay = fake
}
