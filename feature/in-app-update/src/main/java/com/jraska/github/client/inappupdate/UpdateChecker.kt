package com.jraska.github.client.inappupdate

import android.content.Context
import android.view.View
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jraska.github.client.Config
import com.jraska.github.client.Owner
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.core.android.snackbar.SnackbarData
import com.jraska.github.client.core.android.snackbar.SnackbarDisplay
import timber.log.Timber
import javax.inject.Inject

internal class UpdateChecker @Inject constructor(
  private val context: Context,
  private val config: Config,
  private val topActivityProvider: TopActivityProvider,
  private val snackbarDisplay: SnackbarDisplay
) {

  fun checkForUpdates() {
//    if (!inAppUpdateEnabled()) {
//      return
//    }

    Timber.d("Checking for update...")

    val appUpdateManager = AppUpdateManagerFactory.create(context)
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    appUpdateInfoTask.addOnSuccessListener {
      val updateAvailability = it.updateAvailability()
      if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE || updateAvailability == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
        Timber.d("Update available: %s", it)
        onUpdateAvailable(it)
      } else {
        Timber.d("Update not available: %s", it)
      }
    }.addOnFailureListener {
      Timber.w(it, "Checking for update failed")
    }
  }

  private fun onUpdateAvailable(appUpdateInfo: AppUpdateInfo) {
    Timber.d("Immediate update allowed: %s", appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
    Timber.d("Flexible update allowed: %s", appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))

    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
      showUpdateSnackbar()
    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
      startFlexibleUpdate(appUpdateInfo)
    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
      startImmediateUpdate(appUpdateInfo)
    }
  }

  private fun inAppUpdateEnabled(): Boolean = config.getBoolean(UPDATE_STALE_DAYS_VERSION_KEY)

  private fun startFlexibleUpdate(appUpdateInfo: AppUpdateInfo) {
    val appUpdateManager = AppUpdateManagerFactory.create(context)

    topActivityProvider.onTopActivity {
      appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, it, 0)
    }

    appUpdateManager.registerListener { state ->
      Timber.d("State is %s", state)
      if (state.installStatus() == InstallStatus.DOWNLOADED) {
        showUpdateSnackbar()
      }
    }
  }

  private fun showUpdateSnackbar() {
    snackbarDisplay.showSnackbar(
      SnackbarData(
        text = R.string.update_available,
        length = -2,
        action = R.string.cta_install to View.OnClickListener { AppUpdateManagerFactory.create(context).completeUpdate() }
      )
    )
  }

  private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
    val appUpdateManager = AppUpdateManagerFactory.create(context)

    topActivityProvider.onTopActivity {
      appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, it, 0)
    }
  }

  companion object {
    val UPDATE_STALE_DAYS_VERSION_KEY = Config.Key("in_app_update_enabled", Owner.CORE_TEAM)
  }
}
