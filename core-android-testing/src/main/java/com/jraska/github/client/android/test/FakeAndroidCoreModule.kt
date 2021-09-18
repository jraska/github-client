package com.jraska.github.client.android.test

import androidx.lifecycle.ViewModel
import androidx.test.espresso.IdlingRegistry
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.coroutines.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

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
  @Singleton
  fun appDispatchers(): AppDispatchers {
    val idlingDispatcher = IdlingDispatcher(Dispatchers.IO)
    IdlingRegistry.getInstance().register(idlingDispatcher)

    return AppDispatchers(Dispatchers.Main, idlingDispatcher)
  }
}
