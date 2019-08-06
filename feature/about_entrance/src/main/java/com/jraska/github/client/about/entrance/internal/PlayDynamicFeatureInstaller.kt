package com.jraska.github.client.about.entrance.internal

import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.jraska.github.client.about.entrance.DynamicFeatureInstaller
import io.reactivex.Completable
import javax.inject.Inject

internal class PlayDynamicFeatureInstaller @Inject constructor(
  private val manager: SplitInstallManager
) : DynamicFeatureInstaller {
  override fun install(featureName: String): Completable {
    if (alreadyInstalled(featureName)) {
      return Completable.complete()
    }

    val request = SplitInstallRequest.newBuilder()
      .addModule(featureName)
      .build()

    return Completable.unsafeCreate { onSubscribe ->
      manager.startInstall(request)
        .addOnSuccessListener { onSubscribe.onComplete() }
        .addOnFailureListener { onSubscribe.onError(it) }
    }
  }

  private fun alreadyInstalled(featureName: String) = manager.installedModules.contains(featureName)
}
