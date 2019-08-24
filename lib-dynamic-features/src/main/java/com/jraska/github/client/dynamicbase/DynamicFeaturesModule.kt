package com.jraska.github.client.dynamicbase

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.jraska.github.client.PerApp
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.dynamicbase.internal.DynamicFeatureInitializer
import com.jraska.github.client.dynamicbase.internal.PlayDynamicFeatureInstaller
import com.jraska.github.client.dynamicbase.internal.PlayInstallViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module
object DynamicFeaturesModule {

  @JvmStatic
  @Provides
  internal fun provideSplitInstaller(installer: PlayDynamicFeatureInstaller): DynamicFeatureInstaller {
    return installer
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun provideSplitManager(context: Context): SplitInstallManager {
    return SplitInstallManagerFactory.create(context)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(PlayInstallViewModel::class)
  internal fun provideViewModel(viewModel: PlayInstallViewModel): ViewModel {
    return viewModel
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun providePlayDynamicFeatureInstaller(installManager: SplitInstallManager, context: Context): PlayDynamicFeatureInstaller {
    return PlayDynamicFeatureInstaller(installManager, context)
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun provideFeatureInitializer(
    splitInstallManager: SplitInstallManager,
    specs: @JvmSuppressWildcards Set<DynamicFeatureSpec>
  ): DynamicFeatureInitializer {
    return DynamicFeatureInitializer.create(splitInstallManager, specs)
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun initializerOnCreate(initializer: DynamicFeatureInitializer): OnAppCreate {
    return initializer
  }
}
