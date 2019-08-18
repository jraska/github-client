package com.jraska.github.client.about.entrance.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.rx.AppSchedulers
import timber.log.Timber
import javax.inject.Inject

internal class PlayInstallViewModel @Inject constructor(
  private val splitInstallManager: SplitInstallManager,
  private val featureInstaller: PlayDynamicFeatureInstaller
) : ViewModel() {
  private val liveDataMap = lazyMap(this::startInstalling)
  private val listeners = mutableListOf<SplitInstallStateUpdatedListener>()

  fun moduleInstallation(moduleName: String): LiveData<ViewState> {
    return liveDataMap.getValue(moduleName)
  }

  private fun startInstalling(moduleName: String): LiveData<ViewState> {
    val liveData = MutableLiveData<ViewState>()
    liveData.value = ViewState.Loading

    splitInstallManager.beginInstallation(moduleName)

    val listener = SplitInstallStateUpdatedListener {
      if (!it.moduleNames().contains(moduleName)) {
        return@SplitInstallStateUpdatedListener
      }

      Timber.d(it.toString())
      when (it.status()) {
        SplitInstallSessionStatus.DOWNLOADED -> {}
        SplitInstallSessionStatus.DOWNLOADING -> { }
        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
          /*
    This may occur when attempting to download a sufficiently large module.
    In order to see this, the application has to be uploaded to the Play Store.
    Then features can be requested until the confirmation path is triggered.
   */
//          splitInstallManager.startConfirmationDialogForResult(it, this, CONFIRMATION_REQUEST_CODE)
        }
        SplitInstallSessionStatus.INSTALLED -> {
          liveData.value = ViewState.Finish(moduleName)
          featureInstaller.onFeatureInstalled(moduleName)
        }
        SplitInstallSessionStatus.INSTALLING -> {
        }
        SplitInstallSessionStatus.FAILED -> {
          liveData.value = ViewState.Error(RuntimeException("Error downloaded"))
          featureInstaller.onFeatureInstallError(moduleName, RuntimeException("Error downloaded"))
        }
      }
    }

    splitInstallManager.registerListener(listener)

    return liveData
  }

  override fun onCleared() {
    listeners.forEach { splitInstallManager.unregisterListener(it) }
    super.onCleared()
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Finish(val moduleName: String) : ViewState()
    class Error(val error: Throwable) : ViewState()
  }
}
