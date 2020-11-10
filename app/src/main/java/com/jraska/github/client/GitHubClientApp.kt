package com.jraska.github.client

import android.app.Application
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.perf.metrics.AddTrace
import com.jraska.github.client.core.android.HasServiceModelFactory
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.core.android.ServiceModel
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

open class GitHubClientApp : Application(), HasViewModelFactory, HasServiceModelFactory {

  private val appComponent: AppComponent by lazy { createAppComponent() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  override fun serviceModelFactory(): ServiceModel.Factory {
    return appComponent.serviceModelFactory()
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    initRxAndroidMainThread() // Must be here, otherwise is fragile due to class loading

    appComponent.onAppCreateActions().forEach {
      it.onCreate(this)
    }
  }

  open fun createAppComponent(): AppComponent {
    return DaggerAppComponent.factory().create(this)
  }

  private fun initRxAndroidMainThread() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
      AndroidSchedulers.from(Looper.getMainLooper(), true)
    }
  }
}
