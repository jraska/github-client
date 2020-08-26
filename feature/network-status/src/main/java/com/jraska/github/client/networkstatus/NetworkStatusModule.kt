package com.jraska.github.client.networkstatus

import android.app.Application
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.networkstatus.internal.NetworkStatusDisplayer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object NetworkStatusModule {

  @Provides
  @IntoSet
  internal fun addNetworkStatusDisplayer(networkStatusDisplayer: NetworkStatusDisplayer): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        val callbacks = NetworkStatusDisplayer.Callbacks(networkStatusDisplayer)
        app.registerActivityLifecycleCallbacks(callbacks)
      }
    }
  }
}
