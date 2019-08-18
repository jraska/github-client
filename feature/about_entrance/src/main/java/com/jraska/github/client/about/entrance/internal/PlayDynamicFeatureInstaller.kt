package com.jraska.github.client.about.entrance.internal

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.jraska.github.client.about.entrance.DynamicFeatureActivity
import com.jraska.github.client.about.entrance.DynamicFeatureInstaller
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

internal class PlayDynamicFeatureInstaller constructor(
  private val splitInstallManager: SplitInstallManager,
  private val context: Context
) : DynamicFeatureInstaller {
  private val installationRequests = mutableMapOf<String, PublishSubject<Unit>>()

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

    DynamicFeatureActivity.start(context, featureName)

    return publishSubject.ignoreElements()
  }

  fun onFeatureInstalled(featureName: String) {
    val request = installationRequests[featureName] ?: return

    installationRequests.remove(featureName)
    request.onComplete()
  }

  fun onFeatureInstallError(featureName: String, error: Throwable) {
    val request = installationRequests[featureName] ?: return

    installationRequests.remove(featureName)
    request.onError(error)
  }
}
