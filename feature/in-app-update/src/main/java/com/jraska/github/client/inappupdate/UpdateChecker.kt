package com.jraska.github.client.inappupdate

import android.view.View
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jraska.github.client.Config
import com.jraska.github.client.Owner
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay
import com.jraska.github.client.inappupdate.UpdateStrategyConfig.FLEXIBLE
import com.jraska.github.client.inappupdate.UpdateStrategyConfig.IMMEDIATE
import com.jraska.github.client.inappupdate.UpdateStrategyConfig.OFF
import com.jraska.github.client.inappupdate.UpdateStrategyConfig.UNKNOWN
import timber.log.Timber
import javax.inject.Inject

class UpdateChecker @Inject constructor(
  private val updateManagerFactory: UpdateManagerFactory,
  private val config: Config,
  private val topActivityProvider: TopActivityProvider,
  private val snackbarDisplay: SnackbarDisplay
) {

  fun checkForUpdates() {
    if (!shouldCheckForUpdate()) {
      Timber.d("Update check disabled")
      return
    }

    Timber.d("Checking for update...")

    val appUpdateManager = updateManagerFactory.create()
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
    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
      showUpdateSnackbar()
    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) && strategyForApp() == FLEXIBLE) {
      startFlexibleUpdate(appUpdateInfo)
    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) && strategyForApp() == IMMEDIATE) {
      startImmediateUpdate(appUpdateInfo)
    }
  }

  private fun strategyForApp(): UpdateStrategyConfig {
    return UpdateStrategyConfig.fromConfig(config.getString(KEY_UPDATE_STRATEGY))
  }

  private fun shouldCheckForUpdate(): Boolean {
    return when (strategyForApp()) {
      FLEXIBLE -> true
      IMMEDIATE -> true
      OFF -> false
      UNKNOWN -> false
    }
  }

  private fun startFlexibleUpdate(appUpdateInfo: AppUpdateInfo) {
    val appUpdateManager = updateManagerFactory.create()

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

  private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
    val appUpdateManager = updateManagerFactory.create()

    topActivityProvider.onTopActivity {
      appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, it, 0)
    }
  }

  private fun showUpdateSnackbar() {
    snackbarDisplay.showSnackbar(
      SnackbarData(
        text = R.string.update_available,
        length = -2,
        action = R.string.cta_install to { updateManagerFactory.create().completeUpdate() }
      )
    )
  }

  companion object {
    internal val KEY_UPDATE_STRATEGY = Config.Key("in_app_update_strategy", Owner.CORE_TEAM)
  }
}
