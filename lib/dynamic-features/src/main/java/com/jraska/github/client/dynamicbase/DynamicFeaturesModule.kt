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

  @Provides
  internal fun provideSplitInstaller(installer: PlayDynamicFeatureInstaller): DynamicFeatureInstaller {
    return installer
  }

  @Provides
  @PerApp
  internal fun provideSplitManager(context: Context): SplitInstallManager {
    return SplitInstallManagerFactory.create(context)
  }

  @Provides
  @IntoMap
  @ClassKey(PlayInstallViewModel::class)
  internal fun provideViewModel(viewModel: PlayInstallViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @PerApp
  internal fun providePlayDynamicFeatureInstaller(installManager: SplitInstallManager, context: Context): PlayDynamicFeatureInstaller {
    return PlayDynamicFeatureInstaller(installManager, context)
  }

  @Provides
  @PerApp
  internal fun provideFeatureInitializer(
    splitInstallManager: SplitInstallManager,
    specs: @JvmSuppressWildcards Set<DynamicFeatureSpec>
  ): DynamicFeatureInitializer {
    return DynamicFeatureInitializer.create(splitInstallManager, specs)
  }

  @Provides
  @IntoSet
  internal fun initializerOnCreate(initializer: DynamicFeatureInitializer): OnAppCreate {
    return initializer
  }
}
