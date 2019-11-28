package com.jraska.github.client.users

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.FakeCoreComponent
import com.jraska.github.client.core.android.HasViewModelFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UsersApp : Application(), HasViewModelFactory {
  private val appComponent: UsersAppComponent by lazy { componentBuilder().build() }

  private fun componentBuilder(): DaggerUsersAppComponent.Builder {
    return DaggerUsersAppComponent.builder()
      .coreComponent(FakeCoreComponent())
      .hasRetrofit(SimpleRetrofitHolder(createRetrofit()))
  }

  override fun factory(): ViewModelProvider.Factory = appComponent.viewModelFactory()

  override fun onCreate() {
    super.onCreate()

    appComponent.onAppCreateActions()
      .sortedByDescending { it.priority() }
      .forEach {
        it.onCreate(this)
      }
  }

  private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .validateEagerly(BuildConfig.DEBUG)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }
}
