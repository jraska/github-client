package com.jraska.github.client.dynamicbase.internal

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.jraska.github.client.dynamicbase.DynamicFeatureInstaller
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

internal class PlayDynamicFeatureInstaller constructor(
  private val splitInstallManager: SplitInstallManager,
  private val context: Context
) : DynamicFeatureInstaller {

  private val installationRequests = mutableMapOf<String, Subject<Unit>>()

  override fun ensureInstalled(featureName: String): Completable {
    if (splitInstallManager.alreadyInstalled(featureName)) {
      return Completable.complete()
    }

    val existingRequest = installationRequests[featureName]
    if (existingRequest != null) {
      return existingRequest.ignoreElements()
    }

    val publishSubject = PublishSubject.create<Unit>()
    installationRequests[featureName] = publishSubject

    FeatureInstallActivity.start(context, featureName)

    return publishSubject.ignoreElements()
  }

  internal fun onFeatureInstalled(featureName: String) {
    val request = installationRequests[featureName] ?: return

    installationRequests.remove(featureName)
    request.onComplete()
  }

  internal fun onFeatureInstallError(featureName: String, error: Throwable) {
    val request = installationRequests[featureName] ?: return

    installationRequests.remove(featureName)
    request.onError(error)
  }
}
