package com.jraska.github.client.about.entrance

import android.content.Context
import com.jraska.github.client.about.entrance.internal.DynamicAboutLinkLauncher
import com.jraska.github.client.about.entrance.internal.ReflectiveAboutFeatureProvider
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.dynamicbase.DynamicFeatureSpec
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object AboutFeatureEntranceModule {
  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideLauncher(launcher: DynamicAboutLinkLauncher): LinkLauncher {
    return launcher
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun featureProvider(context: Context): DynamicFeatureSpec {
    val aboutFeatureName = context.getString(R.string.title_dynamic_feature_about)
    return DynamicFeatureSpec(aboutFeatureName, ReflectiveAboutFeatureProvider())
  }
}
