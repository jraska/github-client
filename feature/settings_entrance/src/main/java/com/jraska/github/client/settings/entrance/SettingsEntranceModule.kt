package com.jraska.github.client.settings.entrance

import android.content.Context
import com.jraska.github.client.about.entrance.internal.DynamicSettingsLinkLauncher
import com.jraska.github.client.about.entrance.internal.ReflectiveSettingsFeatureProvider
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.dynamicbase.DynamicFeatureSpec
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object SettingsEntranceModule {
  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideSettingsLauncher(launcher: DynamicSettingsLinkLauncher): LinkLauncher {
    return launcher
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun featureProvider(context: Context): DynamicFeatureSpec {
    val settingsFeatureName = context.getString(R.string.title_dynamic_feature_settings)
    return DynamicFeatureSpec(settingsFeatureName, ReflectiveSettingsFeatureProvider())
  }
}
