package com.jraska.github.client.android

import com.jraska.github.client.Fakes
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FakeCoreAndroidModule {
  @Provides
  @Singleton
  fun schedulers(): AppSchedulers {
    return Fakes.trampoline()
  }

  @Provides
  @Singleton
  fun provideNavigator(): Navigator {
    return Fakes.recordingNavigator()
  }

  @Provides
  @Singleton
  internal fun provideFakeSnackbarDisplay(): SnackbarDisplay {
    return FakeSnackbarDisplay()
  }
}
