package com.jraska.github.client.android

import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Fakes
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.ui.SnackbarDisplay
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import javax.inject.Singleton

@Module
object FakeCoreAndroidModule {
  @Provides
  @Singleton
  fun schedulers(): AppSchedulers {
    RxJavaPlugins.setErrorHandler { /* empty for now */ } // TODO: 09/04/2021 Better test implementation https://github.com/jraska/github-client/pull/467/checks?check_run_id=2301305103

    return Fakes.trampoline()
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
