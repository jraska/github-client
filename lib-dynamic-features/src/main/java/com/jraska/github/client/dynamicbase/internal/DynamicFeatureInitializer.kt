package com.jraska.github.client.dynamicbase.internal

import android.app.Application
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.dynamicbase.DynamicFeature
import com.jraska.github.client.dynamicbase.DynamicFeatureSpec
import javax.inject.Provider

internal class DynamicFeatureInitializer constructor(
  private val featureSpecs: Map<String, Provider<DynamicFeature>>,
  private val splitInstallManager: SplitInstallManager
) : OnAppCreate {

  val listener = InstallationsListener(this)

  override fun onCreate(app: Application) {
    loadModules(splitInstallManager.installedModules)

    splitInstallManager.registerListener(listener)
  }

  private fun onModulesInstalled(moduleNames: List<String>) {
    loadModules(moduleNames)
  }

  private fun loadModules(moduleNames: Iterable<String>) {
    moduleNames.forEach { installedFeature ->
      val dynamicFeature = featureSpecs.getValue(installedFeature).get()
      dynamicFeature.onLoad()
    }
  }

  class InstallationsListener(val initializer: DynamicFeatureInitializer) : SplitInstallStateUpdatedListener {
    override fun onStateUpdate(state: SplitInstallSessionState) {
      when (state.status()) {
        SplitInstallSessionStatus.INSTALLED -> initializer.onModulesInstalled(state.moduleNames())
      }
    }
  }

  companion object {
    fun create(splitInstallManager: SplitInstallManager, specs: Set<DynamicFeatureSpec>): DynamicFeatureInitializer {
      val map = specs.associate { it.featureName to it.featureProvider }
      return DynamicFeatureInitializer(map, splitInstallManager)
    }
  }
}
