package com.jraska.github.client.dynamicbase.internal

import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest

internal fun SplitInstallManager.beginInstallation(featureName: String) {
  val request = SplitInstallRequest.newBuilder()
    .addModule(featureName)
    .build()

  startInstall(request)
}

internal fun SplitInstallManager.alreadyInstalled(featureName: String) = this.installedModules.contains(featureName)
