package com.jraska.github.client.android.test

import android.app.Application
import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import com.squareup.rx3.idler.Rx3Idler
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import io.reactivex.rxjava3.plugins.RxJavaPlugins

@Module
object FakeAndroidCoreModule {

  @Provides
  @IntoMap
  @ClassKey(ServiceModel::class)
  internal fun provideServiceModel(): ServiceModel {
    return object : ServiceModel {} // Make sure the collection is not empty
  }

  @Provides
  @IntoMap
  @ClassKey(ViewModel::class)
  internal fun provideViewModel(): ViewModel {
    return object : ViewModel() {} // Make sure the collection is not empty
  }

  @Provides
  @IntoSet
  fun startupRxIdlers() : OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        RxJavaPlugins.setInitComputationSchedulerHandler(
          Rx3Idler.create("RxJava 3.x Computation Scheduler")
        )
        RxJavaPlugins.setInitIoSchedulerHandler(
          Rx3Idler.create("RxJava 3.x IO Scheduler")
        )
      }
    }
  }
}
