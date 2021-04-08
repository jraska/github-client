package com.jraska.github.client

import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.rx.AppSchedulers
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
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
    return object : Navigator {
      override fun launchOnWeb(httpUrl: HttpUrl) {
        TODO("Not yet implemented")
      }

      override fun startUserDetail(login: String) {
        TODO("Not yet implemented")
      }

      override fun startRepoDetail(fullPath: String) {
        TODO("Not yet implemented")
      }

      override fun showSettings() {
        TODO("Not yet implemented")
      }

      override fun showAbout() {
        TODO("Not yet implemented")
      }

      override fun startConsole() {
        TODO("Not yet implemented")
      }
    }
  }
}
