package com.jraska.github.client.users

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.CoreComponent
import com.jraska.github.client.HasRetrofit
import com.jraska.github.client.PerApp
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.core.android.CoreAndroidModule
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.navigation.deeplink.DeepLinkNavigationModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl

@PerApp
@Component(
  modules = [
    AppModule::class,
    CoreAndroidModule::class,
    DeepLinkNavigationModule::class,
    UsersModule::class],
  dependencies = [
    HasRetrofit::class,
    CoreComponent::class
  ]
)
interface UsersAppComponent {
  fun onAppCreateActions(): Set<OnAppCreate>

  fun viewModelFactory(): ViewModelProvider.Factory
}

@Module
object AppModule {
  @Provides
  fun provideLinkLauncher(): WebLinkLauncher {
    return object : WebLinkLauncher {
      override fun launch(url: HttpUrl) {
        throw UnsupportedOperationException("Partial app tried to open $url")
      }
    }
  }

  @Provides
  @IntoSet
  fun setupFresco(): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) = Fresco.initialize(app)
    }
  }

  @Provides
  @IntoSet
  fun setupThreeTen(): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) = AndroidThreeTen.init(app)
    }
  }
}
