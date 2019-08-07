package com.jraska.github.client.about.entrance

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.jraska.github.client.PerApp
import com.jraska.github.client.about.entrance.internal.PlayDynamicFeatureInstaller
import com.jraska.github.client.about.entrance.internal.PlayInstallViewModel
import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module
object DynamicFeaturesModule {

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(PlayInstallViewModel::class)
  internal fun provideViewModel(viewModel: PlayInstallViewModel): ViewModel {
    return viewModel
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideLauncher(): LinkLauncher {
    return DynamicAboutLinkLauncher()
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun provideSplitInstaller(installer: PlayDynamicFeatureInstaller): DynamicFeatureInstaller {
    return installer
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun provideSplitManager(context: Context): SplitInstallManager {
    return SplitInstallManagerFactory.create(context)
  }
}
